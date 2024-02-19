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

public class PlannedActivityTemplateUtils {

private static final Log log = Log.getLogger(PlannedActivityTemplateUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

// Выполняет проверку на наличие уже созданных ППР на текущий горизонт планирования
public static Boolean hasPlannedActivities(EntityDTO pTemplate){
    
    if(CommonUtils.isEmpty(pTemplate.getId())){
        log.error("plannedActivityTemplate has not 'id' value!");
        return false;
    }
    if(pTemplate.isFieldEmpty("nextPlannedTime")){
        log.error("plannedActivityTemplate has not 'nextPlannedTime' value!");
        return false;
    }
    if(pTemplate.isFieldEmpty("repeatEndDate")){
        log.error("plannedActivityTemplate has not 'repeatEndDate' value!");
        return false;
    }
    if(pTemplate.isFieldEmpty("plannedTimeType")){
        log.error("plannedActivityTemplate has not 'plannedTimeType' value!");
        return false;
    }
    
    Timestamp stTime = pTemplate.getAsTimestamp("nextPlannedTime");
    Timestamp endTime = pTemplate.getAsTimestamp("repeatEndDate");
    
    String plannedTimeField = pTemplate.getAsString("plannedTimeType");
    
    // Добавлена проверка на то, что ППР была взята в работу и т.д.
    String query = "templateId=:_template_id and " + plannedTimeField + ">=:_st_time and " + plannedTimeField + "<=:_end_time"
        + " and workflowstepname not in ('in_work', 'completed', 'closed', 'cancelled')"; // added by ae7f7e1
    int cnt = QueryUtils.select().from("plannedactivity").where(query).bind("_template_id", pTemplate.getId())
        .bind("_st_time", stTime).bind("_end_time",endTime).getCount();
    
    return cnt > 0;
}

// Отменяет запланированные ППР на текущий горизонт планирования
public static void cancelPlannedActivities(EntityDTO pTemplate){

    if(CommonUtils.isEmpty(pTemplate.getId())){
        log.error("plannedActivityTemplate has not 'id' value!");
        return;
    }
    if(pTemplate.isFieldEmpty("nextPlannedTime")){
        log.error("plannedActivityTemplate has not 'nextPlannedTime' value!");
        return;
    }
    if(pTemplate.isFieldEmpty("repeatEndDate")){
        log.error("plannedActivityTemplate has not 'repeatEndDate' value!");
        return;
    }
    if(pTemplate.isFieldEmpty("plannedTimeType")){
        log.error("plannedActivityTemplate has not 'plannedTimeType' value!");
        return;
    }

    // by ae7f7e1 to add/dec millis to timestamps
    Timestamp stTime = new Timestamp(pTemplate.getAsTimestamp("nextPlannedTime").getTime());
    Timestamp endTime = new Timestamp(pTemplate.getAsTimestamp("repeatEndDate").getTime() + 1); // to delete PA's with time bugs
    
    String plannedTimeField = pTemplate.getAsString("plannedTimeType");
    
    // Добавлена проверка на то, что ППР была взята в работу и т.д.
    String query = "templateId=:_template_id and " + plannedTimeField + ">=:_st_time and " + plannedTimeField + "<=:_end_time"
        + " and workflowstepname not in ('in_work', 'completed')"; // added by ae7f7e1
    long cnt = 0L;
    
    ScrollQuery scrollQuery = QueryUtils.scroll().from("plannedactivity").where(query).bind("_template_id", pTemplate.getId())
        .bind("_st_time", stTime).bind("_end_time", endTime).getScroll();
    while(scrollQuery.hasNext()){
        EntityDTO pa = scrollQuery.getNext();
        pa.doDelete();
        cnt++;
    }
    scrollQuery.close();
    
    ContextUtils.addMessage(cnt + " ППР отменено");
    
}

// Создает ППР на массив КЕ и согласно расписанию на основании шаблона
public static void createPlannedActivities(EntityDTO pTemplate){

    if (hasPlannedActivities(pTemplate)){
        throw new ValidationException("Для текущей настройки ППР на заданный период уже созданы ППР");
    }
    
    List<Integer> ciIds = getCiIds(pTemplate);
    List<DateTime> plannedDates = getPlannedDates(pTemplate);
    log.info("###ciIds " + ciIds.size());
    log.info("###plannedDates " + plannedDates.size());
    log.info("###plannedDates x ciIds " + (plannedDates.size()*ciIds.size()));
    Long cnt=0L;
    for (DateTime pDate : plannedDates){
        for (Integer ciId : ciIds){
            if (createRecordByTemplate(pTemplate, ciId, pDate)) {
                cnt++;
                log.info("###COUNT: " + cnt);
            }
        }
    }
    
    ContextUtils.addMessage(cnt + " ППР создано");
}

// Вычисляет плановые даты на основании расписания
public static List<DateTime> getPlannedDates(EntityDTO pTemplate){
    if(pTemplate.isFieldEmpty("nextPlannedTime")){
        log.error("plannedActivityTemplate has not 'nextPlannedTime' value!");
        return new ArrayList<DateTime>();
    }
    if(pTemplate.isFieldEmpty("repeatEndDate")){
        log.error("plannedActivityTemplate has not 'repeatEndDate' value!");
        return new ArrayList<DateTime>();
    }
    PlanningService planningService = Lib.PlanningServiceFactory.createByPlannedActivityTemplate(pTemplate);
    return planningService.getNextTimeList(pTemplate.getDateTime("nextPlannedTime"), pTemplate.getDateTime("repeatEndDate"));

}

// Вычисляет плановую дату окончения вычисления
public static DateTime getLastPlannedDate(EntityDTO pTemplate){

    if (pTemplate.isFieldEmpty("planningTimeUnit") || pTemplate.isFieldEmpty("nextPlannedTime")){
        return null;
    }

    DateTime startDateTime = pTemplate.getDateTime("nextPlannedTime");
    TimeUnits timeUnit = CommonUtils.searchEnumItemByName(TimeUnits.class, pTemplate.getAsString("planningTimeUnit"));
    DateTime planningEndTime = timeUnit.plusInterval(startDateTime, 1);

    PlanningService planningService = Lib.PlanningServiceFactory.createByPlannedActivityTemplate(pTemplate);
    List<DateTime> plannedDateTimes = planningService.getNextTimeList(startDateTime, planningEndTime);

    if (CommonUtils.isEmpty(plannedDateTimes)){
        return null;
    }

    return Collections.max(plannedDateTimes);
}

// Вычисляет длительность шаблона ППР, как сумму длительностей входящих в шаблон ППР шаблонов задач
public static void calcTasksSumDuration(EntityDTO pTemplate) throws Exception {
    // code with TaskTemplateUtils
    if(pTemplate.isFieldEmpty("nextPlannedTime")) {
        log.warn("Шаблон ППР №" + pTemplate.getId() + " не задано время первого срабатывания!");
        pTemplate.set("deadlineTimeUnit", "HOUR");
        return;
    }
    
    DateTime startDateTime = pTemplate.getDateTime("nextPlannedTime");
    DateTime endDateTime = pTemplate.getDateTime("nextPlannedTime");
    
    List<EntityDTO> taskTemplateList = QueryUtils.getRecordList("task_template", 
        "parent_entitytype = 'plannedactivity_template' and parent_entityid = " + pTemplate.getId());
    
    String paTimeUnits = "HOUR";
    long paUnitCount = 0;
    
    if (taskTemplateList != null && !pTemplate.isFieldEmpty("calendarname")) {
        EntityDTO cal = QueryUtils.getRecord("calendar", "shortname='" + pTemplate.getAsString("calendarname") + "'");
        if(cal != null) {
            for (EntityDTO taskTempl : taskTemplateList) {
                try {
                    if (taskTempl.get("predecessor")==null) {
                        DateTime dt = Lib.TaskTemplateUtils.calcPATaskFinish(taskTempl, taskTemplateList, startDateTime, null, cal);
                        if (dt.isAfter(endDateTime))
                            endDateTime = dt;
                    }
                } catch(Exception exc){
                    log.error("unable to calculate date with duration for pTemplate = " + pTemplate +
                    "and taskTemplate = " + taskTempl);
                }
            }
        }
    }
    
    Integer daysBetween = CalendarUtils.getDaysBetween(startDateTime, endDateTime);
    if (pTemplate.getAsString("calendarName").equals("24x7") && daysBetween > 0) {
        endDateTime = endDateTime.plusSeconds(daysBetween);
    }
    
    Period p = CalendarUtils.getPeriod(pTemplate.getAsString("calendarName"), startDateTime, endDateTime);
    p = p.normalizedStandard();
    
    // Duration d = new Duration(startDateTime, endDateTime);
    // log.info("duruation seconds = " + d.getStandardSeconds());
    // if(d.getStandardSeconds() % 60 != 0) {
    //     paTimeUnits = "SECOND";
    //     paUnitCount = d.getStandardSeconds();
    // } else if(d.getStandardMinutes() % 60 != 0) {
    //     paTimeUnits = "MINUTE";
    //     paUnitCount = d.getStandardMinutes();
    // } else paUnitCount = d.getStandardHours();
    
    paUnitCount = Lib.CalendarCalcUtils.getWorkHoursBtwnDates(
        pTemplate.getAsString("calendarName"), 
        startDateTime, 
        endDateTime);

    if (p.getMinutes() > 0) {
            
        int minutes = p.getMinutes();
        
        if (minutes == 59) {
            paUnitCount ++;
        }
        else {
            paUnitCount *= 60;
            paUnitCount += minutes;
            paTimeUnits = "MINUTE";
        }
    }
    
    // end code with TaskTemplateUtils
    // log.info("timeunits is: '" + paTimeUnits + "', unitcount is " + paUnitCount);
    
   
    pTemplate.set("deadlineUnitCount", paUnitCount);
    
}

// Вычисляет след последнюю дату срабатывания на основании расписания генерации (горизонта планирования)
public static void recalcNextRepeatEndDate(EntityDTO pTemplate){
    
    if (pTemplate.isFieldEmpty("planningTimeUnit") || pTemplate.isFieldEmpty("repeatEndDate")){
        log.error("Отсутствуют значения полей для шаблона генерации ППР№" + pTemplate.getId());
        return;
    }
    
    DateTime currentRepeatEndDate = pTemplate.getDateTime("repeatEndDate");
    
    if (DateTime.now().isBefore(currentRepeatEndDate)){
        log.warn("Дата окончания генерации на текущий период уже рассчитана!");
        return;
    }

    pTemplate.set("nextPlannedTime", currentRepeatEndDate);

    DateTime planningEndTime = getLastPlannedDate(pTemplate);
    
    // проверка на выход за заданное значение
    DateTime generateToDate = pTemplate.getDateTime("generateToDate");
    if(generateToDate != null && generateToDate.isBefore(planningEndTime)) 
        planningEndTime = generateToDate;
    
    pTemplate.set("repeatEndDate", planningEndTime);

    List<DateTime> nextTimes = getPlannedDates(pTemplate);
    if(CommonUtils.isEmpty(nextTimes) && nextTimes.size() < 2) {
        log.error("Невозможно рассчитать дату следующего начала генерации для генератора ППР: " + pTemplate);
        return;
    }
    
    pTemplate.set("nextPlannedTime", nextTimes.get(1));

    pTemplate.set("autoUpdated", true); // Чтобы не зациклилось из-за правила
    
    pTemplate.doUpdate();
}

// Вычисляет массив ID КЕ 
public static List<Integer> getCiIds(EntityDTO pTemplate){
    List<Integer> result = new ArrayList<>();
    
    if (!pTemplate.isFieldEmpty("ciQuery")){
        String tableName = CIType.getCiTypeTableNameById(pTemplate.getAsInteger("ciTypeId"));
        result.addAll( QueryUtils.select()
                                 .from(tableName)
                                 .where(pTemplate.getAsString("ciQuery"))
                                 .getIds() );
    }
    
    if (!pTemplate.isFieldEmpty("ciIds")) {
        result.addAll( pTemplate.getIntegerArray("ciIds") );
    }
    
    if (!pTemplate.isFieldEmpty("citypeid") && !pTemplate.isFieldEmpty("ci_shortname")) {
        String tableName = CIType.getCiTypeTableNameById(pTemplate.getAsInteger("ciTypeId"));
        if(tableName != null) {
            EntityDTO paObj = QueryUtils.getRecordByShortName(tableName, pTemplate.getAsString("ci_shortname"));
            
            if (paObj != null && !paObj.isEmpty())
                result.add(paObj.getId());
        }
    }
    
    Set<Integer> set = new HashSet<Integer>(result);
    result.clear();
    result.addAll(set);
    return result;
}

// Ф-я генерирует ППР на вновь созданные КЕ на регулярной основе (из шедула)
public static void generatePlanedActivities(EntityDTO pTemplate){

    PlanningService planningService = Lib.PlanningServiceFactory.createByPlannedActivityTemplate(pTemplate);

    Integer ciTypeId = pTemplate.getAsInteger("ciTypeId");
    DateTime nowDateTime = DateTime.now();
    DateTime startDateTime = pTemplate.getDateTime("nextPlannedTime");
    DateTime endDateTime = pTemplate.getDateTime("repeatEndDate");

    List<DateTime> plannedDates = planningService.getNextTimeList(startDateTime, endDateTime.plusMinutes(1));
    List<DateTime> futureDates = plannedDates.stream().filter(dateTime -> dateTime.isAfter(nowDateTime)).collect(java.util.stream.Collectors.toList());
    
    if(futureDates == null || (futureDates != null && futureDates.isEmpty())) {
        log.warn("Невозможно рассчитать дату следующего начала генерации для генератора ППР№" + pTemplate.getKeyValue());
    }
    ScrollQuery scrollQuery = PlanningActivityUtils.getUnplannedCIsQuery(pTemplate, startDateTime, endDateTime);
    scrollQuery.getScroll();
    while (scrollQuery.hasNext()){
        EntityDTO ci = scrollQuery.getNext();
        for (DateTime plannedDate : futureDates){
            log.info("createRecordByTemplate " + pTemplate.getId() + " for ciId=" + ci.getId() + " on plannedDate=" + plannedDate);
            createRecordByTemplate(pTemplate, ci.getId(), plannedDate);
        }
        
        PlanningActivityUtils.updateCiLastPlannedDate(ciTypeId, ci.getId(), pTemplate.getId(), endDateTime);

    }

    scrollQuery.close();
}

// Создает ППР на конкретную КЕ и на конкретную Дату
public static boolean createRecordByTemplate(EntityDTO pTemplate, Integer ciId, DateTime plannedDateTime){
    final String units = "deadlinetimeunit", unitCount = "deadlineunitcount";
    EntityDTO plannedActivity = new EntityDTO("plannedactivity");
    // start - установить дату как начало работ, end - установить дату, как крайний срок
    String plannedTimeType = pTemplate.getAsString("plannedTimeType");

    if ("plannedEndTime".equalsIgnoreCase(plannedTimeType)){
        plannedActivity.set("plannedEndTime", plannedDateTime);
        if (!pTemplate.isFieldEmpty(units) && !pTemplate.isFieldEmpty(unitCount)){
            Period deadlinePeriod = CalendarUtils.createPeriodByTimeUnit(pTemplate.getAsString(units), pTemplate.getAsInteger(unitCount));
            plannedActivity.set("plannedStartTime", CalendarUtils.minusPeriod(pTemplate.getAsString("calendarName"), plannedDateTime, deadlinePeriod));
        }
    }
    else{
        plannedActivity.set("plannedStartTime", plannedDateTime);
        if (!pTemplate.isFieldEmpty(units) && !pTemplate.isFieldEmpty(unitCount)){
            Period deadlinePeriod = CalendarUtils.createPeriodByTimeUnit(pTemplate.getAsString(units), pTemplate.getAsInteger(unitCount));
            plannedActivity.set("plannedEndTime", CalendarUtils.addPeriod(pTemplate.getAsString("calendarName"), plannedDateTime, deadlinePeriod));
        }
    }
    
    Period dPeriod = CalendarUtils.createPeriodByTimeUnit(pTemplate.getAsString(units), pTemplate.getAsInteger("deadline_duration"));
    plannedActivity.set("deadline", CalendarUtils.addPeriod(pTemplate.getAsString("calendarName"), plannedActivity.getDateTime("plannedStartTime"), dPeriod));
    
    // Если уже взяты в работу ППР после текущего дня, то не cоздавать ППР!
    Integer recCnt = QueryUtils.getCount("plannedactivity", "ciid = " + ciId
        + " and citypeid = " + pTemplate.get("ciTypeId")
        + " and templateid = " + pTemplate.getId()
        + " and plannedstarttime >= '" + plannedActivity.getAsTimestamp("plannedStartTime")
        + "' and workflowstepname in ('in_work', 'completed', 'closed', 'cancelled')");
    if(recCnt != null && recCnt > 0){
        log.warn("Уже есть ППР для КЕ №" + ciId + " типа " + pTemplate.get("ciTypeId") 
            + ", взятые в работу после даты: " + plannedActivity.get("plannedStartTime"));
        return false;
    }
    
    // Если уже есть ППР с таким же шаблоном на такую же дату, то не создавать ППР!
    recCnt = QueryUtils.getCount("plannedactivity", "ciid = " + ciId
        + " and citypeid = " + pTemplate.get("ciTypeId")
        + " and templateid = " + pTemplate.getId()
        + " and plannedstarttime = '" + plannedActivity.getAsTimestamp("plannedStartTime")
        + "'");
    if(recCnt != null && recCnt > 0){
        log.warn("Уже есть ППР для КЕ №" + ciId + " типа " + pTemplate.get("ciTypeId") 
            + " на дату: " + plannedActivity.get("plannedStartTime"));
        return false;
    }
    
    plannedActivity.set("templateid", pTemplate.getId());
    plannedActivity.set("citypeid", pTemplate.get("ciTypeId"));
    plannedActivity.set("ciId", ciId);
    
    // if(!pTemplate.isFieldEmpty("ci_shortname"))
    //     plannedActivity.set("ci_shortname", pTemplate.get("ci_shortname"));
    // if(!pTemplate.isFieldEmpty("ci_shortname_format"))
    //     plannedActivity.set("ci_shortname_format", pTemplate.get("ci_shortname_format"));
        
    // if(!pTemplate.isFieldEmpty("calendarname"))
    //     plannedActivity.set("calendarname", pTemplate.get("calendarname"));
    
    // if(!pTemplate.isFieldEmpty("category"))
    //     plannedActivity.set("category", pTemplate.getAsString("category"));
    
    // plannedActivity.set("consists_of_tasks", pTemplate.getAsBooleanNullSub("consists_of_tasks", false));
    plannedActivity.set("is_autogenerated", true);
    
    EntityDTO sysUser = QueryUtils.getRecord("user", "fullname = 'Система'");
        if(sysUser != null && !sysUser.isEmpty())
            plannedActivity.set("createdById", sysUser.getId());
        
    // if(!pTemplate.isFieldEmpty("workgroup"))
    //     plannedActivity.set("workgroup", pTemplate.get("workgroup"));
    // if(!pTemplate.isFieldEmpty("coordinator"))
    //     plannedActivity.set("coordinator", pTemplate.get("coordinator"));
    
    // if(!pTemplate.isFieldEmpty("plannedlaborintensity"))
    //     plannedActivity.set("plannedlaborintensity", pTemplate.get("plannedlaborintensity"));
    
    // plannedActivity.set("displayName", pTemplate.getAsString("activityName") + " для " + ciId + " " + CastUtils.getStringValue(plannedDateTime));
    plannedActivity.doInsert();
    return true;
}

public static boolean isTasksTemplateListEmpty(EntityDTO record) {
    Integer TaskTemplateCount = QueryUtils.getCount("task_template", "parent_entityid ="+record.getId());
    if (TaskTemplateCount > 0) {
        return false;
        }
    else {
        return true;
        }
        }

public static boolean isAnyActiveTasks(EntityDTO record) {
    List<EntityDTO> eTasksTemplate = QueryUtils.getRecordList("task_template", "parent_entityid ="+record.getId());
    boolean isAnyActiveTasks = false;
    for (EntityDTO eTask : eTasksTemplate) {
        if ("activated".equals(eTask.getAsString("workflowstepname"))) {
            isAnyActiveTasks = true;
            break;
        } else {
            isAnyActiveTasks = false;
        }
    }
    return isAnyActiveTasks;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}