package Lib;

import java.util.*;
import java.math.*;
import java.util.concurrent.*;
import java.sql.Timestamp;
import org.joda.time.*;
import ru.ip.server.logging.Log;
import ru.ip.server.logic.message.MessageType;
import ru.ip.server.entity.EntityDTO;
import ru.ip.server.utils.*;
import ru.ip.server.utils.http.*;
import ru.ip.server.exception.*;
import ru.ip.server.rest.client.EntityRestClient;
import ru.ip.server.database.sql.SQLUtils;
import ru.ip.server.database.sql.SimpleQuery;
import ru.ip.server.database.sql.ScrollQuery;
import ru.ip.server.threadpool.*;
import ru.ip.server.integration.v2.*;
import ru.ip.server.integration.v2.elements.*;
import ru.ip.server.office.loop.ExcelDataHolder;
import ru.ip.server.measureunit.*;
import ru.ip.server.module.*;
import ru.ip.server.module.cms.*;
import ru.ip.server.module.cms.model.*;
import ru.ip.server.module.itsm.*;
import ru.ip.server.module.itsm.model.*;
import ru.ip.server.module.planning.*;
import ru.ip.server.integration.mdm.*;
import ru.ip.server.integration.mdm.model.*;

/**
* Данный класс сформирован автоматически в результате экспорта библиотеки классов.
* Код библиотеки классов содержится между строками 'START_CLASS_LIBRARY_CODE' и 'END_CLASS_LIBRARY_CODE'.
* Код вне этого блока НЕ будет сохранен при импорте в систему.
*/

public class KafkaIntegration {

private static final Log log = Log.getLogger(KafkaIntegration.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static final String KAFKA_MESSAGES_URL = "http://178.154.206.31:6789/kafka/messages/";
    private static final String DATA_ACTION_FIELD = "action";
    private static final String DATA_TIME_FIELD = "time";
    private static final String DATA_TABLE_FIELD = "table";
    private static final String DATA_ID_FIELD = "id";
    private static final String DATA_FIELDS_FIELD = "fields";

    public static void sendData() throws Exception {

        // Выбираем таблицы, в которых есть измененные объекты
        String whereQuery = "EXISTS (SELECT 1 FROM SYSTEM.v_dataforkafka AS DFK "
            + "WHERE DFK.recordtable = tablename AND DFK.isactive = 1)";
        List<EntityDTO> tables = QueryUtils.getRecordList("entitytype", whereQuery);
        if (tables.isEmpty()) return;
        // Создаем соответствие пользователя и топика
        List<EntityDTO> usersTopicsList = QueryUtils.getRecordList("kafkatopic", "isactive = 1");
        Map<Integer, List<String>> usersTopics = new HashMap<>();
        for (EntityDTO userTopic : usersTopicsList) {
            Integer userId = userTopic.getAsInteger("userid");
            List<String> topics = usersTopics.computeIfAbsent(userId, k -> new ArrayList<>());
            topics.add(userTopic.getAsString("shortname"));
        }
        // Делаем цикл по полученным таблицам
        for (EntityDTO table : tables) {
            // Запоминаем данные на отправку
            String tableName = table.getAsString("tablename");
            whereQuery = String.format("recordtable = '%s' AND isactive = 1", tableName);
            List<EntityDTO> updatedRecords = QueryUtils.getRecordList("dataforkafka", whereQuery);
            if (updatedRecords.size() == 0) continue;
            String updatedRecordsIds = updatedRecords.stream().map(e -> e.getKeyValue().toString())
                .collect(java.util.stream.Collectors.joining(", ", "(", ")"));
            // Начинаем собирать запрос
            StringBuilder query = new StringBuilder(
                "WITH updated_records AS "
                    + "(SELECT dataforkafkaid, recordid, displayname, action, datafield FROM SYSTEM.v_dataforkafka "
                    + "WHERE dataforkafkaid IN " + updatedRecordsIds + "),\n"
                    + "sending_records AS (\n");
            String workspace = table.getAsString("workspaceid->shortname");
            String keyField = table.getAsString("pkfieldname");
            boolean isFirstRule = true;
            // Выбираем отдельные правила по таблице
            whereQuery = "isactive = 1 AND tablename = '" + tableName + "'";
            List<EntityDTO> rules = QueryUtils.getRecordList("kafkadatasettings", whereQuery);
            if (rules.isEmpty()) {
                markSentRecords(updatedRecords);
                continue;
            }
            for (EntityDTO rule : rules) {
                if (isFirstRule)
                    isFirstRule = false;
                else
                    query.append("UNION\n");
                // Строим запрос с учетом правила и набора измененных объектов в таблице
                List<Integer> users = AccessGroupUtils.getUserIds(rule.getAsString("usergroup"));
                String usersIds = users.stream().map(Object::toString)
                    .collect(java.util.stream.Collectors.joining(", ", "(", ")"));
                // Собираем соединения по полям с пользователями
                List<String> userJoinConditions = new ArrayList<>();
                List<String> workspaceUserJoinConditions = new ArrayList<>();
                String linkUserFields = rule.getAsString("linkuserfields");
                if (!CommonUtils.isEmpty(linkUserFields)) {
                    for (String fieldName: linkUserFields.split(" ")) {
                        String userTableName = getFieldNameReferenceTableName(table.getKeyValue(), fieldName);
                        if ("user".equals(userTableName)) {
                            userJoinConditions.add("tab." + fieldName + " = " + "users.userid");
                        } else if ("workspace_user".equals(userTableName)) {
                            workspaceUserJoinConditions.add("tab." + fieldName + " = " + "ws_users.workspace_userid");
                        }
                    }
                }
                // Подгатавливаем строку соединения с таблицей user
                String userJoinConditionString = "1 = 1";
                if (!userJoinConditions.isEmpty()) {
                    userJoinConditionString = userJoinConditions.stream()
                        .collect(java.util.stream.Collectors.joining(" OR ", "(", ")"));
                }
                // Подгатавливаем строку соединения с таблицей workspace_user
                String workspaceUserJoinConditionString = "";
                if (!workspaceUserJoinConditions.isEmpty()) {
                    workspaceUserJoinConditionString = workspaceUserJoinConditions.stream()
                        .collect(java.util.stream.Collectors.joining(" OR ", "(", ")"));
                }
                // Сборка запроса по правилу
                query.append(String.format(
                    "SELECT tab.id, users.userid FROM "
                        + "(SELECT tab.%s id, %s FROM %s.v_%s AS tab WHERE EXISTS "
                        + "     (SELECT 1 FROM updated_records AS upd WHERE upd.recordid = tab.%s) AND (%s)\n) AS tab "
                        + "INNER JOIN system.v_user AS users ON users.userid IN %s AND %s ",
                    keyField,
                    !CommonUtils.isEmpty(linkUserFields) ? linkUserFields.replace(" ", ",") : "0",
                    workspace,
                    tableName,
                    keyField,
                    CommonUtils.nullSub(rule.getAsString("datafilter"), "1 = 1"),
                    usersIds,
                    userJoinConditionString));
                if (!workspaceUserJoinConditions.isEmpty()) {
                    query.append(String.format(
                        "INNER JOIN %s.v_workspace_user AS ws_users ON users.userid = ws_users.userid AND %s",
                        workspace,
                        workspaceUserJoinConditionString));
                }
                query.append(" UNION\n");
                query.append(String.format(
                    "SELECT upd.recordid, users.userid FROM updated_records AS upd "
                        + "INNER JOIN system.v_user AS users ON users.userid IN %s "
                        + "WHERE upd.action = 'delete'\n",
                    usersIds));
            }
            query.append(")\n");
            query.append(
                "SELECT"
                    + "    sending_records.userid,"
                    + "    updated_records.displayname,"
                    + "    updated_records.datafield"
                    + " FROM"
                    + "    sending_records"
                    + "    INNER JOIN updated_records ON sending_records.id = updated_records.recordid"
                    + " ORDER BY"
                    + "    updated_records.dataforkafkaid");
            List<EntityDTO> records = QueryUtils.getRecordList(query.toString());
            for (EntityDTO record : records) {
                // По каждому пользователю получаем топик и отправляем туда данные измененного объекта
                List<String> topicsNames = usersTopics.get(record.getAsInteger("userId"));
                if (topicsNames == null) continue;
                String msgId = record.getAsString("displayname");
                String msg = record.getAsString("datafield");
                for (String topic : topicsNames)
                    sendRecord(topic, msgId, msg);
            }
            // В конце цикла по таблице удаляем изменения объектов из специальной таблицы
            markSentRecords(updatedRecords);
        }
    }

    public static void sendInitialData(String topicName) throws Exception {

        //Получаем ид групп доступа пользователя переданного топика
        EntityDTO topic = QueryUtils.getRecordByShortName("kafkatopic", topicName);
        ru.ip.server.security.User topicUser = new ru.ip.server.security.User(topic.getAsInteger("userid"));
        String userGroupsIds = topicUser.getUserGroups().stream().map(String::valueOf)
            .collect(java.util.stream.Collectors.joining(", ", "(", ")"));

        //Получим список из уникальных рабочих пространств, к которым может относиться пользователь
        List<Integer> workspaceIds = java.util.stream.Stream.of(
            ru.ip.server.Constants.SYSTEM_WORKSPACE_ID,
            ru.ip.server.Constants.TEMPLATE_WORKSPACE_ID,
            topicUser.getWorkspaceId()
        ).distinct().collect(java.util.stream.Collectors.toList());

        //Пройдемся по каждому пространству отдельно
        Integer defaultWorkspaceId = ru.ip.server.security.SessionContextHolder.getWorkspaceId();
        for (Integer workspaceId: workspaceIds) {
            String workspace = ru.ip.server.cache.CacheManager.getInstance().getWorkspace(workspaceId).getShortName();

            // Выбираем таблицы, которые должны выгружаться в этот топик
            String whereQuery = "workspaceid = " + workspaceId + " AND tablename IN\n" +
                "(SELECT kafkasettings.tablename\n" +
                "FROM system.v_kafkadatasettings AS kafkasettings\n" +
                "INNER JOIN system.v_accessgroup AS accessgroup ON kafkasettings.usergroup = accessgroup.shortname\n" +
                "WHERE accessgroup.accessgroupid IN " + userGroupsIds + "\n" +
                "    AND kafkasettings.isactive = 1)";
            List<EntityDTO> tables = QueryUtils.getRecordList("entitytype", whereQuery);
            if (tables.isEmpty()) continue;

            // Делаем цикл по полученным таблицам
            for (EntityDTO table : tables) {
                String tableName = table.getAsString("tablename");
                String keyField = table.getAsString("pkfieldname");
                boolean isFirstRule = true;
                // Выбираем отдельные правила по таблице
                whereQuery = "isactive = 1 AND tablename = '" + tableName + "'";
                List<EntityDTO> rules = QueryUtils.getRecordList("kafkadatasettings", whereQuery);
                if (rules.isEmpty()) {
                    continue;
                }
                Integer ws_user_id = getWorkspaceUserId(
                    topic.getAsInteger("userid"),
                    topic.getAsInteger("workspaceid"));
                StringBuilder query = new StringBuilder();
                for (EntityDTO rule : rules) {
                    if (isFirstRule)
                        isFirstRule = false;
                    else
                        query.append("UNION ALL\n");
                    // Собираем условие по полям с пользователем
                    String userCondition = "1 = 1";
                    List<String> userConditions = new ArrayList<>();
                    String linkUserFields = rule.getAsString("linkuserfields");
                    if (!CommonUtils.isEmpty(linkUserFields)) {
                        for (String fieldName : linkUserFields.split(" ")) {
                            String userTableName = getFieldNameReferenceTableName(table.getId(), fieldName);
                            if ("user".equals(userTableName)) {
                                userConditions.add("tab." + fieldName + " = " + topic.getAsString("userid"));
                            } else if ("workspace_user".equals(userTableName)) {
                                userConditions.add("tab." + fieldName + " = " + ws_user_id);
                            }
                        }
                        if (!userConditions.isEmpty()) {
                            userCondition = userConditions.stream()
                                .collect(java.util.stream.Collectors.joining(" OR ", "(", ")"));
                        }
                    }
                    // Строим запрос с учетом правила и всех объектов в таблице
                    query.append(String.format(
                        "SELECT tab.%s id FROM %s.v_%s AS tab WHERE (%s) AND %s\n",
                        keyField,
                        workspace,
                        tableName,
                        CommonUtils.nullSub(rule.getAsString("datafilter"), "1 = 1"),
                        userCondition));
                }
                ru.ip.server.security.SessionContextHolder.setSystemContext(workspaceId);
                whereQuery = String.format("%s IN (%s)", keyField, query);
                List<EntityDTO> records = QueryUtils.getRecordList(tableName, whereQuery);
                for (EntityDTO record : records) {

                    Map<String, Object> data = new HashMap<>();
                    data.put(DATA_ACTION_FIELD, "create");
                    data.put(DATA_TIME_FIELD, System.currentTimeMillis());
                    data.put(DATA_TABLE_FIELD, tableName);
                    data.put(DATA_ID_FIELD, record.getId());
                    data.put(DATA_FIELDS_FIELD, record.getFieldList());

                    String msgId = tableName + ":" + record.getId();
                    String msg = JSONUtils.map2json(data);
                    sendRecord(topicName, msgId, msg);
                }
            }
        }
        ru.ip.server.security.SessionContextHolder.setSystemContext(defaultWorkspaceId);
    }

    public static void deleteOldSentData() {

        int daysOfWaiting = 30; // Число дней, по истечению которых записи удаляются
        String whereQuery = "createdtime < (NOW() - interval '" + daysOfWaiting + " day')";
        QueryUtils.deleteRecords("dataforkafka", whereQuery);
    }

    private static void sendRecord(String topic, String messageId, String message) throws Exception {

        Map<String, Object> payload = new HashMap<>();
        payload.put("msgId", messageId);
        payload.put("msg", message);

        HttpPostRequest request = new HttpPostRequest(KAFKA_MESSAGES_URL + topic);
        request.setPayload(payload);
        request.execute();
    }

    private static void markSentRecords(List <EntityDTO> updatedRecords) {
        for (EntityDTO record : updatedRecords) {
            record.set("isactive", false);
            record.doUpdate();
        }
    }

    private static String getFieldNameReferenceTableName(Integer tableId, String fieldName){

        try{
            ru.ip.server.datasource.EntityDescription entityDescription =
                ru.ip.server.cache.CacheManager.getInstance().getEntityDescription(tableId);
            ru.ip.server.database.table.single.EntityField entityField = entityDescription.getEntityField(fieldName);
            if (entityField == null){
                return null;
            }

            return entityField.getFieldFormat().getLookupEntity();
        }
        catch(Exception e){
            return null;
        }

    }

    public static Integer getWorkspaceUserId(Integer userId, Integer workspaceId){

        if (workspaceId == null || userId == null){
            return null;
        }

        try{
            ru.ip.server.datasource.EntityDescription entityDescription =
                ru.ip.server.cache.CacheManager.getInstance().getEntityDescription(workspaceId,
                    ru.ip.server.EntityNameConstants.WORKSPACE_USER_NAME);
            EntityDTO workspaceUser = QueryUtils.select(Collections.singletonList(entityDescription.getKeyFieldName()))
                .from(entityDescription)
                .where("userId=:_u_id").bind("_u_id", userId)
                .getRecord();

            if (workspaceUser != null){
                return workspaceUser.getAsInteger(entityDescription.getKeyFieldName());
            }
        }
        catch (Exception ignored){

        }

        return null;
    }


// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}