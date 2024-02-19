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

public class TaskTemplateUtils {

private static final Log log = Log.getLogger(TaskTemplateUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static DateTime addWorkHoursToDate(Date startDate, Integer durationInHours, Integer calendarId) throws Exception{        
    DateTime finishDate = null;
    //EntityDTO cal = getRequestCalendar(inter);
    EntityDTO cal = new EntityDTO("calendar", calendarId);
    if (cal!=null)
        finishDate = CalendarUtils.addPeriod(cal.getAsString("SHORTNAME"), CastUtils.getDateTime(startDate), Period.hours(durationInHours)); 
    return finishDate;
}

public static DateTime addWorkHoursToDate(DateTime startDate, Integer durationInHours, String calendarName) throws Exception{        
    DateTime finishDate = null;
    finishDate = CalendarUtils.addPeriod(calendarName, startDate, Period.hours(durationInHours)); 
    return finishDate;
}

public static EntityDTO getTaskCalendar(EntityDTO task) throws Exception{
    if (!task.isFieldEmpty("parent_entitytype")) {
        
        if ( CommonUtils.isSame("plannedactivity_template", task.getAsString("parent_entitytype"))
        && !task.isFieldEmpty("parent_entityid")
        ) {
            EntityDTO pTemplate = new EntityDTO("plannedactivity_template", task.getAsInteger("parent_entityid"));
            if(!pTemplate.isEmpty()) {
                
                EntityDTO cal = QueryUtils.getRecord("calendar", "shortname = '" + pTemplate.get("calendarname") + "'");
                if (cal != null && !cal.isEmpty()) return cal;
            
            }
        }
        else if ( CommonUtils.isSame("plannedactivity", task.getAsString("parent_entitytype"))
        && !task.isFieldEmpty("parent_entityid")
        ) {
            EntityDTO pTemplate = new EntityDTO("plannedactivity", task.getAsInteger("parent_entityid"));
            if(!pTemplate.isEmpty()) {
                
                EntityDTO cal = QueryUtils.getRecord("calendar", "shortname = '" + pTemplate.get("calendarname") + "'");
                if (cal != null && !cal.isEmpty()) return cal;
                
            }
        }
        
        else if ( CommonUtils.isSame("interaction_template", task.getAsString("parent_entitytype"))
        && !task.isFieldEmpty("parent_entityid") && !task.isFieldEmpty("workgroup")
        ) {
            EntityDTO cal = Lib.ITSMIncidentUtils.getTaskCalendar(task);
            return cal;
        }

    } 
    else {
    
        EntityDTO workspace = new EntityDTO("workspace", ContextUtils.getWorkspaceId());
        if ((workspace!=null) && (workspace.get("calendar")!=null)){
            EntityDTO cal = new EntityDTO("calendar", workspace.getAsInteger("calendar"));
            return cal;
        }
    }
    return null;
}

/* Метод для создания списка Задач из Шаблонов задач, привязанных к шаблону Работы
 * Должен вызываться в случае, если у Работы выбран Шаблон работы.
*/
public static void createPlannedActivityTasks(EntityDTO pActivity) throws Exception{
    if (pActivity.isFieldEmpty("templateId")){
        log.error("plannedActivity has not templateID value!");
        return;
    }
    /*
    if (pActivity.isFieldEmpty("plannedStartTime")){
        log.error("plannedActivity has not plannedStartTime value!");
        return;
    }
    */
    DateTime startTime = new DateTime().withSecondOfMinute(0);
    List<EntityDTO> taskTemplList = QueryUtils.getRecordList("task_template", 
        "parent_entitytype ='plannedactivity_template' and parent_entityid = " + pActivity.getAsInteger("templateId"));
    for (EntityDTO taskTempl:taskTemplList){
        if (taskTempl.get("predecessor")==null)
            //addPlannedActivityTask(taskTempl, pActivity, taskTemplList, startTime /* pActivity.getDateTime("plannedStartTime") */, null, true, null);
            addPlannedActivityTask(taskTempl, pActivity, taskTemplList, pActivity.getDateTime("plannedStartTime"), null, true, null);
    }
}

/* Метод для добавления одной Задачи к родительской Работе из Шаблона задачи
 * В конце метода рекурсивный вызов метода для последующих в цепочке Задач,
 * поэтому метод должен явно вызываться только для задач, у которых нет предшествующей Задачи
*/
public static /*DateTime*/void addPlannedActivityTask(EntityDTO taskTempl, 
                                              EntityDTO service, 
                                              List<EntityDTO> taskList, 
                                              DateTime plannedStart, 
                                              EntityDTO predecessor, 
                                              boolean isCategory, 
                                              EntityDTO orgunit ) throws Exception {      
    
    
    EntityDTO task = QueryUtils.getRecord("task", "template=" + taskTempl.getId()
        + " and parent_entitytype = '" + service.getTableName() + "'"
        + " and parent_entityid = " + service.getId());
    
    if(task != null && !task.isEmpty()) {
        log.warn("task with template = " + taskTempl.getId()
            + " and parent_entitytype = '" + service.getTableName() + "'"
            + " and parent_entityid = " + service.getId() + " already exists");
        return;
    }
    
    task = new EntityDTO("task");
    if (taskTempl.get("DISPLAYNAME")!=null)
        task.set("DISPLAYNAME", taskTempl.getAsString("DISPLAYNAME"));
    if (taskTempl.get("DESCRIPTION")!=null)
         task.set("DESCRIPTION", taskTempl.getAsString("DESCRIPTION"));
        
    if (!taskTempl.isFieldEmpty("calendarname"))
         task.set("calendarname", taskTempl.getAsString("calendarname"));
    
    if (taskTempl.get("workgroup")!=null)
        task.set("workgroup", taskTempl.getAsString("workgroup"));
    
    task.set("parent_entitytype",service.getTableName());
    task.set("parent_entityid", service.getKeyValue());
    if ("request".equalsIgnoreCase(service.getTableName()))
        task.set("request", service.getKeyValue());
    
    //расчет планового начала Задачи
    EntityDTO cal = getTaskCalendar(taskTempl);
    if(cal == null)
        cal = QueryUtils.getRecordByShortName("calendar", "24x7");
    
    Integer gap = 0;
    if (taskTempl.get("gap")!=null)
        gap = taskTempl.getAsInteger("gap");
    if (cal!=null){
        //plannedStart = getNextWorkHour(plannedStart, cal.getAsString("shortname"));
        plannedStart = calcDateWithDurationHuman(plannedStart, gap, "minutes", cal);
    }
    if (plannedStart != null){
        plannedStart = getNextWorkHour(plannedStart, cal.getAsString("shortname"));
        task.set("planned_start", plannedStart);
    }
    log.info("request task creation cal="+cal.get("displayname"));
    log.info("request task creation planned_start="+plannedStart);
    if (!taskTempl.isFieldEmpty("duration"))
        task.set("duration", taskTempl.getAsDouble("duration"));
    DateTime plannedFinish = null;
    if ( !taskTempl.isFieldEmpty("duration") &&  !taskTempl.isFieldEmpty("duruation_unitofmeasure") && (cal!=null)){                
        plannedFinish = calcDateWithDurationHuman(plannedStart,taskTempl.getAsInteger("duration"),taskTempl.getAsString("duruation_unitofmeasure"), cal);
    }
    log.info("request task creation planned_finis="+plannedFinish);
    if (plannedFinish!=null)
        task.set("planned_finish", plannedFinish);
    else
        task.set("planned_finish", plannedStart);
    
    if (!taskTempl.isFieldEmpty("deadline_duration") && !taskTempl.isFieldEmpty("duruation_unitofmeasure") && (cal!=null))
        task.set("deadline", calcDateWithDurationHuman(plannedStart, taskTempl.getAsInteger("deadline_duration"), 
                                                               taskTempl.getAsString("duruation_unitofmeasure"), cal));
    if (predecessor != null)
        task.set("predecessor", predecessor.getKeyValue());
    else
        task.set("enableautotransition",true);
    
     if (!service.isFieldEmpty("citypeid"))
         task.set("citype", service.getAsInteger("citypeid"));
     if (!service.isFieldEmpty("ci_shortname"))
         task.set("ci_shortname", service.getAsString("ci_shortname"));
     if (!service.isFieldEmpty("ci_shortname_format"))
         task.set("ci_shortname_format", service.getAsString("ci_shortname_format"));
    if (!service.isFieldEmpty("ciId"))
         task.set("ci", service.get("ciId"));
    
    task.set("is_autogenerated", true);
    task.set("template", taskTempl.getId());
    task.set("tasksCreation", true);

    if (isCategory || (!CommonUtils.nullSub(taskTempl.getAsBoolean("iscreatedbyscript"),false))) 
        task.doInsert();  
    if (predecessor==null)
        task.processTransitions();
    for (EntityDTO childTask:taskList){
        if ((childTask.get("predecessor")!=null) && (taskTempl.getKeyValue().equals(childTask.getAsInteger("predecessor")))){
            addPlannedActivityTask(childTask,  service, taskList, task.getDateTime("planned_finish"), task, isCategory, orgunit);
        }
    }
    // return plannedFinish;   
}

public static DateTime calcPaFinish(EntityDTO pActivity, DateTime startTime) throws Exception {
    List<EntityDTO> taskList = QueryUtils.getRecordList("task", 
        "parent_entitytype ='plannedactivity' and parent_entityid = " + pActivity.getId());
    
    if (startTime == null && !pActivity.isFieldEmpty("plannedStartTime")) {
        startTime = pActivity.getDateTime("plannedStartTime");
    }
    
    DateTime plannedFinish = new DateTime(startTime);
    
    if(taskList != null && startTime != null) {

        for (EntityDTO task : taskList){
            
            if (task.isFieldEmpty("predecessor") && !task.isFieldEmpty("calendarname")) {
                EntityDTO cal = QueryUtils.getRecordByShortName("calendar", task.getAsString("calendarname"));
                
                DateTime currPlannedFinish = calcPATaskFinish(task, taskList, startTime, null, cal);
                if (currPlannedFinish.isAfter(plannedFinish))
                    plannedFinish = currPlannedFinish;
            }
        }
    }
    
    return plannedFinish;
}


public static DateTime calcPaDeadline(EntityDTO pActivity, DateTime startTime) throws Exception {
    List<EntityDTO> taskList = QueryUtils.getRecordList("task", 
        "parent_entitytype ='plannedactivity' and parent_entityid = " + pActivity.getId()
        + " and (isnt_nec_for_closing is null or isnt_nec_for_closing = 0)");
    
    if (startTime == null && !pActivity.isFieldEmpty("plannedStartTime")) {
        startTime = pActivity.getDateTime("plannedStartTime");
    }
    
    DateTime plannedFinish = new DateTime(startTime);
    
    if(taskList != null && startTime != null) {

        for (EntityDTO task : taskList){
            
            if (task.isFieldEmpty("predecessor") && !task.isFieldEmpty("calendarname")) {
                EntityDTO cal = QueryUtils.getRecordByShortName("calendar", task.getAsString("calendarname"));
                
                DateTime currPlannedFinish = calcPATaskFinish(task, taskList, startTime, null, cal);
                if (currPlannedFinish.isAfter(plannedFinish))
                    plannedFinish = currPlannedFinish;
            }
        }
    }
    
    return plannedFinish;
}


public static DateTime calcPATaskFinish(EntityDTO taskTempl, List<EntityDTO> taskList, DateTime plannedStart, EntityDTO predecessor, EntityDTO cal) throws Exception{      
    
    DateTime plannedFinish = null;
    // EntityDTO cal = getTaskCalendar(taskTempl);
    if (!taskTempl.isFieldEmpty("planned_start")){
        plannedStart = taskTempl.getDateTime("planned_start");
    }
    
    // calculate plannedStart with gap
    if (plannedStart != null && predecessor != null && !predecessor.isEmpty() && !taskTempl.isFieldEmpty("gap")) {
        log.info("plannedStart before calc = " + plannedStart + ", gap = " + taskTempl.getAsInteger("gap") + " minutes");
        plannedStart = calcDateWithDurationHuman(plannedStart, taskTempl.getAsInteger("gap"), "minutes", cal);
        log.info("plannedStart after calc = " + plannedStart);
    }
    
    if (taskTempl.get("planned_finish")!=null)
        plannedFinish = new DateTime(taskTempl.getAsDateTime("planned_finish"));
    else{
        if ((taskTempl.get("duration")!=null) && (taskTempl.get("duruation_unitofmeasure")!=null)){
            log.info("plannedFinish before calc = " + plannedFinish + ", dur = " + taskTempl.getAsInteger("duration") + taskTempl.getAsString("duruation_unitofmeasure"));
            plannedFinish = calcDateWithDurationHuman(plannedStart, taskTempl.getAsInteger("duration"), taskTempl.getAsString("duruation_unitofmeasure"), cal);
            log.info("plannedFinish after calc = " + plannedFinish);
            // taskTempl.set("duration", taskTempl.get("duration"));
        }    
    }
    if (plannedFinish == null)
        plannedFinish = plannedStart;
    
    
    for (EntityDTO childTask:taskList){
        
        if ((childTask.get("predecessor")!=null) && (taskTempl.getKeyValue().equals(childTask.getAsInteger("predecessor")))){
            
            DateTime currPlannedFinish = calcPATaskFinish(childTask,  taskList, plannedFinish, taskTempl, cal);
            if (currPlannedFinish.isAfter(plannedFinish))
                plannedFinish = currPlannedFinish;
        }
    }
    return plannedFinish;   
}


public static void deletePlannedActivityTasks(EntityDTO pActivity) {
    if (CommonUtils.isEmpty(pActivity.getId())){
        log.error("plannedActivity has not ID value!");
        return;
    }
    List<EntityDTO> taskList = QueryUtils.getRecordList("task", 
        "parent_entitytype = 'plannedactivity' and parent_entityid = " + pActivity.getId());
    for (EntityDTO task: taskList){
        task.doDelete();
    }
}

public static void updateNextTasks(EntityDTO task, Date lastDate) throws Exception{
    //log.info("начало метода updateNextTasks, tasid="+task.getKeyValue());
    EntityDTO calendar = getTaskCalendar(task);
    DateTime plannedStart = CastUtils.getDateTime(lastDate);
    if (calendar!=null)
        plannedStart = getNextWorkHour(plannedStart, calendar.getAsString("shortname"));
    //log.info("посчитано плановое начало="+plannedStart);
    List<EntityDTO> childTaskList = QueryUtils.getRecordList("TASK","PREDECESSOR = "+task.getKeyValue());
    for (EntityDTO childTask:childTaskList)
        updatTaskDates(childTask,calendar, plannedStart);
      
    if ((childTaskList.size()==0) && (task.get("parent_entityid")!=null)){
    EntityDTO proj = new EntityDTO("project", task.getAsInteger("parent_entityid"));
    if ((proj!=null) && (task.getAsDateTime("planned_finish").after(proj.getAsDateTime("planned_finish")))){
        proj.set("planned_finish", task.getAsDateTime("planned_finish"));
        proj.doUpdate();
    }
} 
    //log.info("конец метода updateNextTasks");
}

public static DateTime getNextWorkHour(DateTime startDate, String calendarName) throws Exception{   
    DateTime nextDate = CalendarUtils.addPeriod(calendarName, startDate, Period.seconds(1));
    nextDate = nextDate.minusSeconds(1);
    return nextDate;
}

public static Integer getWorkHoursBtwnDates(String calendarName, DateTime stDateTime, DateTime enDateTime) throws Exception{ 
    Period p = CalendarUtils.getPeriod(calendarName, stDateTime, enDateTime);
    Integer duration = 0;
    if (p!=null){
        if(p.getDays() > 0) 
            duration = p.getDays()*24;
        if (p.getHours() > 0) 
            duration += p.getHours();
    }
    //log.info("длительность в часах ="+duration);
    return duration;
}

public static void updatTaskDates(EntityDTO task, EntityDTO calendar, DateTime plannedStart) throws Exception{
    task.set("planned_start", plannedStart);
    task.set("planned_finish", addWorkHoursToDate(task.getAsDateTime("planned_start"), task.getAsInteger("planned_effort"), calendar.getKeyValue()));
    task.doUpdate();   
}

public static void reopenNextTasks(EntityDTO task, Date lastDate) throws Exception{
    EntityDTO calendar = getTaskCalendar(task);
    DateTime plannedStart = CastUtils.getDateTime(lastDate);
   //plannedStart = Lib.ProjectTemplateUtils.getNextWorkHour(plannedStart, calendar.getAsString("SHORTNAME"));
    List<EntityDTO> taskList = QueryUtils.getRecordList("TASK","PREDECESSOR = "+task.getKeyValue()+" and workflowstepname='completed'");
    for (EntityDTO nextTask:taskList){
        //log.info("обновляю задачу"+nextTask.getKeyValue());
        nextTask.set("enableautotransition", true);
        nextTask.set("planned_start", plannedStart);
        nextTask.set("planned_finish", addWorkHoursToDate(nextTask.getAsDateTime("planned_start"), nextTask.getAsInteger("planned_effort"), calendar.getKeyValue()));
        nextTask.processTransitions();
    } 
}

public static boolean checkAllOneStatusTasksCompleted(EntityDTO service, String category) throws Exception{
    List<EntityDTO> taskList = QueryUtils.getRecordList("task","parent_entitytype ='"+service.getTableName()+"' and parent_entityid ="+service.getKeyValue() + " and category = '"+category+"'");
    log.info("checkAllOneStatusTasksCompleted, категория = "+category+", кол-во задача="+taskList.size());
    for (EntityDTO task:taskList){
        if (!task.getAsString("WORKFLOWSTEPNAME").equals("completed"))
            return false;
    }
    return true;
}

public static boolean checkAllServiceTasksCompleted(EntityDTO service, String category) throws Exception{
    List<EntityDTO> reqList = QueryUtils.getRecordList("request","service ="+service.getKeyValue());
    for (EntityDTO req:reqList){
        if (!checkAllOneStatusTasksCompleted(req, category))
            return false;
    }
    return true;
}

public static EntityDTO calcTaskOrgunit(Integer parentOU, Integer workgroup) throws Exception{
    //log.info("parentOU="+parentOU+", workgroup="+workgroup);
    List<EntityDTO> itemList = QueryUtils.selectSql("WITH RECURSIVE r AS (SELECT r_orgunitid,workgroup FROM v_r_orgunit WHERE r_orgunitid ="+ parentOU+" UNION SELECT ou.r_orgunitid,ou.workgroup FROM v_r_orgunit ou  join r on ou.parent_orgunit = r.r_orgunitid WHERE ou.isactive = 1) SELECT r_orgunitid FROM r where workgroup="+workgroup).getRecordList();
    //log.info("query=WITH RECURSIVE r AS (SELECT r_orgunitid,workgroup FROM v_r_orgunit WHERE r_orgunitid ="+ parentOU+" UNION SELECT ou.r_orgunitid,ou.workgroup FROM v_r_orgunit ou  join r on ou.parent_orgunit = r.r_orgunitid WHERE ou.isactive = 1) SELECT r_orgunitid FROM r where workgroup="+workgroup);
    if (itemList.size()>0){
        Integer pid = itemList.get(0).getAsInteger("r_orgunitid");
        EntityDTO ou = QueryUtils.getRecordById("r_orgunit",pid);
        return ou;
    }
    else
        return null;
}

public static DateTime calcDateWithDurationHuman(DateTime plannedStart, Integer duration, String durationType, EntityDTO cal) throws Exception{
    if ("days".equals(durationType))
        return plannedStart.plusDays(duration)/*.withTime(18,0,0,0)*/;
    else if ("hours".equals(durationType))
        return plannedStart.plusHours(duration);
    else if ("workhours".equals(durationType))
        return addWorkHoursToDate(plannedStart.toDate(), duration, cal.getKeyValue());
    else if ("workdays".equals(durationType))
        return CalendarUtils.addWorkDays(cal.getAsString("SHORTNAME"), plannedStart, duration);
    else if ("minutes".equals(durationType))
        return CalendarUtils.addPeriod(cal.getAsString("SHORTNAME"), plannedStart, Period.minutes(duration)); //plannedStart.plusMinutes(duration);
    else
        return null;
}

public static DateTime calcDateWithDurationHuman3Measures(DateTime plannedStart, Integer duration, String durationType, String calName) throws Exception{
    Period dPeriod = CalendarUtils.createPeriodByTimeUnit(durationType, duration);
    if (dPeriod == null)
        return null;
    return CalendarUtils.addPeriod(calName, plannedStart, dPeriod);

}

public static DateTime calcDateWithDuration(DateTime plannedStart, Integer duration, String durationType, EntityDTO cal) throws Exception{
    if ("days".equals(durationType))
        return plannedStart.plusDays(duration-1).withTime(18,0,0,0);
    else if ("hours".equals(durationType))
        return plannedStart.plusHours(duration-1);
    else if ("workhours".equals(durationType))
        return addWorkHoursToDate(plannedStart.toDate(), duration, cal.getKeyValue());
    else if ("workdays".equals(durationType))
        return CalendarUtils.addWorkDays(cal.getAsString("SHORTNAME"), plannedStart, duration);
    else if ("minutes".equals(durationType))
        return plannedStart.plusMinutes(duration);
    else
        return null;
}

public static boolean checkStatusTasksExistCompleted(EntityDTO service, String category) throws Exception{
    //List<EntityDTO> taskList = QueryUtils.getRecordList("task","parent_entitytype ='"+service.getTableName()+"' and parent_entityid ="+service.getKeyValue() + " and category = '"+category+"'");
    List<EntityDTO> taskList = QueryUtils.getRecordList("task","request ="+service.getKeyValue() + " and category = '"+category+"'");
    if (taskList.size()==0)
        return false;
    for (EntityDTO task:taskList){
        if (!task.getAsString("WORKFLOWSTEPNAME").equals("completed"))
            return false;
    }
    return true;
}

public static boolean checkServStatusTasksExistCompleted(EntityDTO service, String category) throws Exception{
    List<EntityDTO> taskList = QueryUtils.getRecordList("task","parent_entitytype ='"+service.getTableName()+"' and parent_entityid ="+service.getKeyValue() + " and category = '"+category+"'");
    if (taskList.size()==0)
        return true;
    for (EntityDTO task:taskList){
        if (!task.getAsString("WORKFLOWSTEPNAME").equals("completed"))
            return false;
    }
    return true;
}

public static EntityDTO calcTaskDatesAfterObj(DateTime plannedStart, EntityDTO task, EntityDTO taskTempl, Integer year) throws Exception{
    DateTime now = new DateTime();
    if (year==null)
        year = now.getYear();
    DateTime plannedFinish = null;
    EntityDTO cal = getTaskCalendar(task);
    if (taskTempl.get("planned_start")!=null){
        plannedStart = new DateTime(taskTempl.getAsDateTime("planned_start")).withYear(year);
        task.set("planned_start", plannedStart);
    }
    else{
        if (cal!=null){
            plannedStart = getNextWorkHour(plannedStart, cal.getAsString("shortname"));
            task.set("planned_start", plannedStart);
        }
    }
    if (taskTempl.get("planned_finish")!=null)
        plannedFinish =  new DateTime(taskTempl.getAsDateTime("planned_finish")).withYear(year);
    else{
        if ((taskTempl.get("duration")!=null) && (taskTempl.get("duruation_unitofmeasure")!=null)){
            plannedFinish = calcDateWithDuration(plannedStart, taskTempl.getAsInteger("duration"), taskTempl.getAsString("duruation_unitofmeasure"), cal);
            task.set("duration", taskTempl.get("duration"));
        }    
    }
    if (plannedFinish == null)
        plannedFinish = plannedStart.withTime(18,0,0,0);
    task.set("planned_finish", plannedFinish);
    if (taskTempl.get("deadline")!=null){
        if (CommonUtils.isSame(taskTempl.getAsBoolean("isnextyeardeadline"),true))
            task.set("deadline",new DateTime(taskTempl.getAsDateTime("deadline")).withYear(year+1));
        else
            task.set("deadline",new DateTime(taskTempl.getAsDateTime("deadline")).withYear(year));
    }
    else{
        if (taskTempl.get("deadline_duration")!=null)
            task.set("deadline", plannedStart.plusDays(taskTempl.getAsInteger("deadline_duration")).withTime(18,0,0,0));
        else
            task.set("deadline", plannedFinish);
    }
    return task;
}

public static EntityDTO getWorkspaceCalendar(Integer workspaceId){ 
    EntityDTO workspace = new EntityDTO("workspace", workspaceId);
    if ((workspace!=null) && (workspace.get("calendar")!=null)){
        EntityDTO cal = new EntityDTO("calendar", workspace.getAsInteger("calendar"));
        return cal;
    }
    else
        return QueryUtils.getRecordById("calendar",1);
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}