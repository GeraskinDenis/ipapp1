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

public class PlannedActivityUtils {

private static final Log log = Log.getLogger(PlannedActivityUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

// проверка на то, что ППР для того же КЕ на предыдущие даты отсутствуют или взяты в работу
public static boolean canTakePAToWork(EntityDTO pa) {
    boolean result = false;
    
    if(!pa.isEmpty() && !pa.isFieldEmpty("plannedactivityId") && pa.isFieldEmpty("templateId"))
        result = true;
        
    if(!pa.isFieldEmpty("deadline")
        && !pa.isFieldEmpty("plannedStartTime")
        && !pa.isFieldEmpty("plannedEndTime")
        && !pa.isFieldEmpty("initiator")
        && !pa.isFieldEmpty("workgroup")
        && !pa.isFieldEmpty("coordinator")
        && pa.isFieldEmpty("templateid"))
        result = true;
    
    if(pa.getId() != null
        && !pa.isFieldEmpty("deadline")
        && !pa.isFieldEmpty("plannedStartTime")
        && !pa.isFieldEmpty("plannedEndTime")
        && !pa.isFieldEmpty("initiator")
        && !pa.isFieldEmpty("workgroup")
        && !pa.isFieldEmpty("coordinator")
        && pa.getAsBooleanNullSub("isActive", false)
        && pa.get("templateId") != null
        && pa.get("plannedStartTime") != null
        && pa.get("ciTypeId") != null
        && pa.get("ciId") != null
    ) {
        Integer notTakenCount = QueryUtils.getCount("plannedactivity", "isActive = 1"
            + " and templateId = " + pa.get("templateId") // если необходимо применить только к ППР, созданным по текущему расписанию
            + " and ciTypeId = " + pa.get("ciTypeId")
            + " and ciId = " + pa.get("ciId")
            + " and plannedStartTime < '" + pa.getAsTimestamp("plannedStartTime")
            + "' and workflowStepName not in ('completed', 'closed', 'cancelled')");
        if(notTakenCount != null && notTakenCount < 1) {
            result = true;
        }
    }
    return result;
}

public static boolean canPATaskPlanAuto(EntityDTO task){
    return !task.isEmpty() && !task.isFieldEmpty("taskId") && !task.isFieldEmpty("parent_entitytype") 
        && CommonUtils.isSame("plannedactivity", task.getAsString("parent_entitytype")) 
        && (!task.isFieldEmpty("orgUnit") || !task.isFieldEmpty("assignedTo"));
}

// получить родительский ППР.
// если у задачи родитель не ППР, то вернет null
// если такого ППР нет, то вернет пустой объект типа EntityDTO
// проверять результат метода нужно с помощью кода: entity != null && !entity.isEmpty()
public static EntityDTO getTaskParentPA(EntityDTO task) {
    
    if (task.isFieldEmpty("parent_entitytype")
        || !CommonUtils.isSame("plannedactivity", task.getAsString("parent_entitytype"))
        || task.isFieldEmpty("parent_entityid"))
        return null;
    
    return new EntityDTO("plannedactivity", task.getAsInteger("parent_entityid"));
}

// получить родительский объект.
// если такого объекта нет, то вернет пустой объект типа EntityDTO
// проверять результат метода нужно с помощью кода: entity != null && !entity.isEmpty()
public static EntityDTO getTaskParentObject(EntityDTO task) {
    
    if (task.isFieldEmpty("parent_entitytype")
        || task.isFieldEmpty("parent_entityid"))
        return null;
    
    return new EntityDTO(task.getAsString("parent_entitytype"), task.getAsInteger("parent_entityid"));
}

// возвращает true, если все задачи, дочерние к ППР в параметре pa, отменены или отсутствуют, 
// и falsе, если есть еще неотмененные задачи
public static boolean canAutoCancelPA(EntityDTO pa){
    
    if(pa.isEmpty() || pa.isFieldEmpty("plannedactivityId")
    || !pa.getAsBooleanNullSub("consists_of_tasks", false))
        return false;
    
    Integer notCancelledCount = QueryUtils.getCount("task", 
        "workflowstepname <> 'cancelled'"
        + " and parent_entitytype = 'plannedactivity'"
        + " and parent_entityid = " + pa.getId());
    
    if(notCancelledCount != null && notCancelledCount < 1)
        return true;
    
    return false;
}


// возвращает true, если все задачи, дочерние к ППР в параметре pa, выполнены или отсутствуют, 
// и falsе, если есть еще невыполненные задачи
public static boolean canAutoCompletePA(EntityDTO pa){
    
    if(pa.isEmpty() || pa.isFieldEmpty("plannedactivityId")
    || !pa.getAsBooleanNullSub("consists_of_tasks", false))
        return false;
    
    Integer notCancelledCount = QueryUtils.getCount("task", 
        "workflowstepname <> 'completed'"
        + " and parent_entitytype = 'plannedactivity'"
        + " and parent_entityid = " + pa.getId());
    
    if(notCancelledCount != null && notCancelledCount < 1)
        return true;
    
    return false;
}

public static boolean canUserCancelPA(EntityDTO pa){
    boolean result = false;
    
    // if (!pa.isFieldEmpty("initiator")
    //     && CommonUtils.isSame(pa.getAsInteger("initiator"), ContextUtils.getCurrentUserId()))
    
    if (!pa.isFieldEmpty("workflowstepname") 
        && Arrays.asList("new", "planned", "appointed").contains(
                pa.getAsString("workflowstepname"))
        && !pa.isFieldEmpty("initiator")
        && (CommonUtils.isSame(pa.getAsInteger("initiator"), ContextUtils.getCurrentUserId())
            || ContextUtils.getCurrentUser().isInAccessGroup(2))) {
        
        result = true;
    }
    
    return result;
}

public static boolean canUserSavePA(EntityDTO pa) {
    boolean result = false;
    
    if(pa.isFieldEmpty("workflowstepname")) {
        result = true;
    }
    else{
        if(CommonUtils.isSame("new", pa.getAsString("workflowstepname"))
            && (pa.isFieldEmpty("initiator")
                || CommonUtils.isSame(pa.getAsInteger("initiator"),  ContextUtils.getCurrentUserId()))
                || ContextUtils.getCurrentUser().isInAccessGroup(2))
            result = true;
        else
        if (Arrays.asList("planned", "appointed", "in_work").contains(pa.getAsString("workflowstepname"))
            && ((!pa.isFieldEmpty("initiator")
                && CommonUtils.isSame(pa.getAsInteger("initiator"), 
                    ContextUtils.getCurrentUserId()))
                ||  (!pa.isFieldEmpty("workgroup->manager")
                    && CommonUtils.isSame(pa.getAsInteger("workgroup->manager"), 
                        ContextUtils.getCurrentUserId()))
                || ContextUtils.getCurrentUser().isInAccessGroup(2)
                // || ContextUtils.getCurrentUser().isInAccessGroup(655)
                ) 
            )
            result = true;
    }
    
    return result;
}

public static boolean canReappointPAGroup(EntityDTO pa) {
    boolean result = false;
    
    if(!pa.isFieldEmpty("workflowstepname")) 
    {
        result = CommonUtils.isSame("planned", pa.getAsString("workflowstepname"))
            && ((!pa.isFieldEmpty("initiator")
                && CommonUtils.isSame(pa.getAsInteger("initiator"), 
                    ContextUtils.getCurrentUserId()))
                || (!pa.isFieldEmpty("workgroup->manager")
                    && CommonUtils.isSame(pa.getAsInteger("workgroup->manager"), 
                        ContextUtils.getCurrentUserId()))
                || ContextUtils.getCurrentUser().isInAccessGroup(2)
                // || ContextUtils.getCurrentUser().isInAccessGroup(655)
                );
    }
    
    return result;
}

public static boolean canDeclinePAGroup(EntityDTO pa) {
    boolean result = false;
    
    if(!pa.isFieldEmpty("workflowstepname")) 
    {
        result = CommonUtils.isSame("planned", pa.getAsString("workflowstepname"))
            && ((!pa.isFieldEmpty("workgroup->manager")
                    && CommonUtils.isSame(pa.getAsInteger("workgroup->manager"), 
                        ContextUtils.getCurrentUserId()))
                || ContextUtils.getCurrentUser().isInAccessGroup(2)
                // || ContextUtils.getCurrentUser().isInAccessGroup(655)
                );
    }
    
    return result;
}

public static boolean canReappointPACoordinator(EntityDTO pa) {
    boolean result = false;
    
    if(!pa.isFieldEmpty("workflowstepname")) 
    {
        result = (CommonUtils.isSame("appointed", pa.getAsString("workflowstepname"))
            || CommonUtils.isSame("in_work", pa.getAsString("workflowstepname")))
            && ((!pa.isFieldEmpty("initiator")
                && CommonUtils.isSame(pa.getAsInteger("initiator"), 
                    ContextUtils.getCurrentUserId()))
                || (!pa.isFieldEmpty("workgroup->manager")
                    && CommonUtils.isSame(pa.getAsInteger("workgroup->manager"), 
                        ContextUtils.getCurrentUserId()))
                || ContextUtils.getCurrentUser().isInAccessGroup(2)
                // || ContextUtils.getCurrentUser().isInAccessGroup(655)
                );
    }
    
    return result;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}