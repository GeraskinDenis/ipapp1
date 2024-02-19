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

public class ITSMRequestUtils {

private static final Log log = Log.getLogger(ITSMRequestUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static EntityDTO getTemplateObject(EntityDTO request) throws Exception{
    if (request.get("template")!=null){
        EntityDTO reqTempl = new EntityDTO("request_template", request.getAsInteger("template"));
        return reqTempl;
    }
    return null;
}

public static EntityDTO getRequestCalendar(EntityDTO request) throws Exception{
    EntityDTO workspace = new EntityDTO("workspace", ContextUtils.getWorkspaceId());
    if ((workspace!=null) && (workspace.get("calendar")!=null)){
        EntityDTO cal = new EntityDTO("calendar", workspace.getAsInteger("calendar"));
        return cal;
    }
    return null;
}

public static boolean checkTasksCompleted(EntityDTO request) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("TASK","PARENT_ENTITYTYPE ='request' and parent_entityid ="+request.getId()+" and not coalesce(workflowstepname,'123') in ('cancelled')");
	for (EntityDTO task:taskList){
		if (!task.getAsString("WORKFLOWSTEPNAME").equals("completed"))
			return false;
	}
	return true;
}

public static boolean checkTaskStatus(EntityDTO request, String requestStatus, String status) throws Exception{
	EntityDTO task = QueryUtils.getRecord("SM_TASK","PARENT_ENTITYTYPE ='SM_REQUEST' and ENTITYID ="+request.get("SM_REQUESTID")+ " and REQUESTSTATUS = '"+requestStatus+"'");
	if (task == null)
		return false;
	else{ 
		if (task.getAsString("WORKFLOWSTEPNAME").equals(status)){
			return true;	
		}
		else
			return false;
	}
}

public static boolean checkOneStatusTasksEqualStatus(EntityDTO request, String requestStatus,String status) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("SM_TASK","PARENT_ENTITYTYPE ='SM_REQUEST' and ENTITYID ="+request.get("SM_REQUESTID")+ " and REQUESTSTATUS = '"+requestStatus+"'");
	for (EntityDTO task:taskList){
		if (task.getAsString("WORKFLOWSTEPNAME").equals(status))
			return true;
	}
	return false;
}

public static boolean checkNoOneStatusTasksEqualStatus(EntityDTO request, String requestStatus,String status) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("SM_TASK","PARENT_ENTITYTYPE ='SM_REQUEST' and ENTITYID ="+request.get("SM_REQUESTID")+ " and REQUESTSTATUS = '"+requestStatus+"'");
	for (EntityDTO task:taskList){
		if (task.getAsString("WORKFLOWSTEPNAME").equals(status))
			return false;
	}
	return true;
}

public static List<EntityDTO> getActiveTaskList(EntityDTO request) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("TASK","PARENT_ENTITYTYPE ='request' and parent_entityid ="+request.getId()+" and not coalesce(workflowstepname,'123') in ('cancelled')");
	return taskList;
}

public static boolean checkAllRequestCompleted(EntityDTO interaction) throws Exception{
    List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='request' and entityid1="+interaction.getKeyValue());
	for (EntityDTO rel:relList){
		EntityDTO request = new EntityDTO("request", rel.getAsInteger("ENTITYID2"));
		if (request!=null){
			if (!Arrays.asList("completed","cancelled").contains(request.getAsString("WORKFLOWSTEPNAME")))
				return false;
		}
	}
	return true;
}

public static List<EntityDTO> getActiveInteractionReqList(EntityDTO interaction) throws Exception{
    List<EntityDTO> reqList = new ArrayList<EntityDTO>();
    List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='request' and entityid1="+interaction.getKeyValue());
	log.info("связи найдены-"+relList.size());
	for (EntityDTO rel:relList){
		EntityDTO request = QueryUtils.getRecordById("request", rel.getAsInteger("ENTITYID2"));
		if (!CommonUtils.isEmpty(request)){
			if (!Arrays.asList("cancelled").contains(request.getAsString("WORKFLOWSTEPNAME")))
				reqList.add(request);
		}
	}
    return reqList;
}

public static void createRequestTasksFromTemplate(EntityDTO request) throws Exception{
    if (request.isFieldEmpty("template")){
        log.error("plannedActivity has not template value!");
        return;
    }
    DateTime startTime = new DateTime().withSecondOfMinute(0);
    List<EntityDTO> taskTemplList = QueryUtils.getRecordList("task_template", "parent_entitytype ='interaction_template' and parent_entityid = " + request.getAsInteger("template"));
    //log.info("найдено шаблонов задач = "+taskTemplList.size());
    for (EntityDTO taskTempl:taskTemplList){
        if (taskTempl.get("predecessor")==null)
            Lib.TaskTemplateUtils.addPlannedActivityTask(taskTempl, request, taskTemplList, startTime, null, true, null);
    }
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}