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

public class DisplayQueryUtils {

private static final Log log = Log.getLogger(DisplayQueryUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static String calcInteractionStatisticsQuery(String p_param){
    ContextUtils.addMessage("парам = "+p_param);
    return 
    "(lower('"+p_param+"')='создано' and  createdtime between date_trunc('month', LOCALTIMESTAMP) and (date_trunc('month', LOCALTIMESTAMP) + interval '1 month')) or ('@p_param'='выполнено' and fact_finish between date_trunc('month', LOCALTIMESTAMP) and (date_trunc('month', LOCALTIMESTAMP) + interval '1 month') and workflowstepname in ('completed','closed','declined'))"+
    " or "+
    "(lower('"+p_param+"')='выполнено' and fact_finish between date_trunc('month', LOCALTIMESTAMP) and (date_trunc('month', LOCALTIMESTAMP) + interval '1 month') and workflowstepname in ('completed','closed','declined'))"+
    " or "+
    "(lower('"+p_param+"')='выполнено c нарушением срока' and fact_finish between date_trunc('month', LOCALTIMESTAMP) and (date_trunc('month', LOCALTIMESTAMP) + interval '1 month') and workflowstepname in ('completed','closed','declined','cancelled') and deadline<fact_finish)"+
    " or "+
    "(lower('"+p_param+"')='невыполненные с нарушением срока' and not workflowstepname in ('completed','closed','declined','cancelled') and deadline<localtimestamp)"+
    " or "+
    "(lower('"+p_param+"')='возвращено на доработку' and exists ( SELECT  1 FROM fkv39o1z.interaction_history h where h.entity_id = interactionid and h.created_time between date_trunc('month', LOCALTIMESTAMP) and (date_trunc('month', LOCALTIMESTAMP) + interval '1 month') and h.new_value = 'waiting_inc_reopen'))";
}

public static String calcInteractionStatisticsDispatcherIds(String p_activitytype, Integer p_wsuser) {
    String sqlQuery;
    switch (p_activitytype) {
        case "Зарегистрировал":
            sqlQuery = "select interaction.interactionid entity_id\n" +
                    "from fkv39o1z.v_interaction interaction\n" +
                    "join fkv39o1z.v_workspace_user ws_u\n" +
                    "on interaction.createdbyid = ws_u.userid\n" +
                    "where ws_u.workspace_userid =:_p_wsuser";
            break;
        case "Выполнил":
            sqlQuery = "select interaction.interactionid\n" +
                    "from fkv39o1z.v_interaction interaction\n" +
                    "join(\n" +
                    "--Выполнил самостоятельно\n" +
                    "select elog.entity_id, ws_u.fullname\n" +
                    "from system.ipentitytransitionlog elog\n" +
                    "join fkv39o1z.v_workspace_user ws_u\n" +
                    "on elog.created_by_id = ws_u.userid\n" +
                    "where elog.entity_type_id = 2200 and elog.transition_id = 296\n" +
                    "and ws_u.workspace_userid = :_p_wsuser\n" +
                    ") completed \n" +
                    "on interaction.interactionid = completed.entity_id";
            break;
        case "Подтвердил выполнение":
            sqlQuery = "select interaction.interactionid\n" +
                    "from fkv39o1z.v_interaction interaction\n" +
                    "join(\n" +
                    "--Выполнил самостоятельно\n" +
                    "select elog.entity_id, ws_u.fullname\n" +
                    "from system.ipentitytransitionlog elog\n" +
                    "join fkv39o1z.v_workspace_user ws_u\n" +
                    "on elog.created_by_id = ws_u.userid\n" +
                    "where elog.entity_type_id = 2200 and elog.transition_id = 297\n" +
                    "and ws_u.workspace_userid =:_p_wsuser\n" +
                    ") completed \n" +
                    "on interaction.interactionid = completed.entity_id";
            break;
        default:
            return "1 = 1";
    }

    List<Integer> idList = QueryUtils.selectSql(sqlQuery).bind("_p_wsuser", p_wsuser).getIntegerList();
    String result;

    if (idList.size() > 0) {
        StringBuilder ids = new StringBuilder();
        for (Integer id : idList) {
            ids.append(id).append(", ");
        }
        result = ids.toString().replaceAll(", $", "");
    } else {
        result = "-1";
    }

    return "interactionid in (" + result + ")";
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}