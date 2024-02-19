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

public class ITSMIncidentUtils {

private static final Log log = Log.getLogger(ITSMIncidentUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static EntityDTO getWorkspaceTable(String tableName) throws Exception{
    EntityDTO entityType = QueryUtils.getRecord("ENTITYTYPE", "TABLENAME='"+tableName+ "' and WORKSPACEID = "+ContextUtils.getCurrentUser().getWorkspaceId());
    if (entityType!=null)
        return entityType;
    else
        return null;
}

public static void DeleteTasksFromIncident(EntityDTO incident) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("SM_TASK","PARENT_ENTITYTYPE='SM_INCIDENT' AND ENTITYID="+incident.getKeyValue());
	for (EntityDTO task:taskList){
		List<EntityDTO> histList =  QueryUtils.getRecordList("HISTORY", "ENTITYTYPEID = 255 AND ENTITYID = "+task.getAsString("SM_TASKID"));
		for (EntityDTO hist:histList)
			hist.doDelete();
		List<EntityDTO> alertList = QueryUtils.getRecordList("ALERTLOG","ENTITYTYPEID = 255 AND ENTITYID = "+task.getAsString("SM_TASKID"));
		for (EntityDTO alert:alertList)
			alert.doDelete();
		List<EntityDTO> commentList = QueryUtils.getRecordList("COMMENT","ENTITYTYPEID = 255 AND ENTITYID = "+task.getAsString("SM_TASKID"));
		for (EntityDTO comment:commentList)
			comment.doDelete();
		List<EntityDTO> paramList = QueryUtils.getRecordList("SM_CITASKPARAM","TASK = "+task.getAsString("SM_TASKID"));
		for (EntityDTO param:paramList)
			param.doDelete();
		List<EntityDTO> jobList = QueryUtils.getRecordList("DEAMONJOB","ENTITYTAG = '"+task.getTag()+"'");
		for (EntityDTO job:jobList)
			job.doDelete();
		task.doDelete();
	}
}

public static void DeleteIncident(EntityDTO incident) throws Exception{
	List<EntityDTO> histList =  QueryUtils.getRecordList("HISTORY", "ENTITYTYPEID = 313 AND ENTITYID = "+incident.getKeyValue());
	for (EntityDTO hist:histList)
		hist.doDelete();
	List<EntityDTO> alertList = QueryUtils.getRecordList("ALERTLOG","ENTITYTYPEID = 313 AND ENTITYID = "+incident.getKeyValue());
	for (EntityDTO alert:alertList)
		alert.doDelete();
	List<EntityDTO> commentList = QueryUtils.getRecordList("COMMENT","ENTITYTYPEID = 313 AND ENTITYID = "+incident.getKeyValue());
	for (EntityDTO comment:commentList)
		comment.doDelete();
	List<EntityDTO> jobList = QueryUtils.getRecordList("DEAMONJOB","ENTITYTAG = '"+incident.getTag()+"'");
	for (EntityDTO job:jobList)
		job.doDelete();
	List<EntityDTO> relList = QueryUtils.getRecordList("SM_OBJRELATION","ENTITYTYPE2 =313  AND ENTITYID2="+incident.getKeyValue());
	for (EntityDTO rel:relList)
		rel.doDelete();
	DeleteTasksFromIncident(incident);
	incident.doDelete();
}

public static boolean checkAllIncidentsCompleted(EntityDTO interaction) throws Exception{
	EntityDTO entityType = getWorkspaceTable("incident");
    if (entityType!=null){
        log.info("описание таблицы найдено");
        List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='incident' and entityid1="+interaction.getKeyValue());
    	log.info("связи найдены-"+relList.size());
    	for (EntityDTO rel:relList){
    		EntityDTO incident = new EntityDTO("INCIDENT", rel.getAsInteger("ENTITYID2"));
    		if (incident!=null){
    			log.info("инидент найден");
    			if (!Arrays.asList("completed","cancelled").contains(incident.getAsString("WORKFLOWSTEPNAME")))
    				return false;
    		}
    	}
    }
	return true;
}
public static EntityDTO createIncidentFromInteraction(EntityDTO interaction) throws Exception{
	//log.info("createIncidentFromInteraction start");
	EntityDTO incident = new EntityDTO("INCIDENT");
	String machineModel = "";
	String machineNum = "";
	String ouName = "";
	if (interaction.get("CI")!=null)
		incident.set("CI", interaction.getAsInteger("CI"));
	if (interaction.get("CITYPE")!=null)
	    incident.set("CITYPE", interaction.getAsInteger("CITYPE"));
	if (interaction.get("DISPLAYNAME")!=null)
	    incident.set("DISPLAYNAME",  interaction.getAsString("DISPLAYNAME"));
	if (interaction.get("PRIORITY")!=null)
	    incident.set("PRIORITY",interaction.getAsInteger("PRIORITY"));
	if (interaction.get("DESCRIPTION")!=null)
	    incident.set("DESCRIPTION", CommonUtils.nullSub(interaction.getAsString("DESCRIPTION"),"-"));
	if (interaction.get("coordinator")!=null)
	    incident.set("coordinator", interaction.getAsInteger("coordinator"));
	if (interaction.get("WORKGROUP")!=null){
	    incident.set("WORKGROUP", interaction.getAsInteger("WORKGROUP"));
	    EntityDTO workgroup = QueryUtils.getRecordById("workgroup", interaction.getAsInteger("WORKGROUP"));
	    if (!CommonUtils.isEmpty(workgroup) && !CommonUtils.isEmpty(workgroup.get("manager")) && CommonUtils.isEmpty(incident.get("coordinator")))
	        incident.set("coordinator",workgroup.get("manager"));
	}
	//log.info("createIncidentFromInteraction до заполнения шаблона");
	if (interaction.get("template")!=null)
	    incident.set("template", interaction.getAsInteger("template"));
	incident.set("INTERACTION",interaction.getKeyValue());
	if (interaction.get("DEADLINE")!=null)
	    incident.set("DEADLINE", new Timestamp(interaction.getAsDateTime("DEADLINE").getTime()));
	//log.info("перед созданием инцидента");
	if (interaction.get("service")!=null)
	    incident.set("service", interaction.getAsInteger("service"));
	if (interaction.get("location")!=null)
	    incident.set("location", interaction.getAsInteger("location"));    
	incident.doInsert();
	if (incident.getKeyValue()!=null)
		return incident;
	else
		return null;
}


public static List<EntityDTO> getActiveInteractionIncList(EntityDTO interaction) throws Exception{
    List<EntityDTO> incList = new ArrayList<EntityDTO>();
    List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='incident' and entityid1="+interaction.getKeyValue());
	log.info("связи найдены-"+relList.size());
	for (EntityDTO rel:relList){
		EntityDTO incident = QueryUtils.getRecordById("INCIDENT", rel.getAsInteger("ENTITYID2"));
		if (!CommonUtils.isEmpty(incident)){
			if (!Arrays.asList("cancelled").contains(incident.getAsString("WORKFLOWSTEPNAME")))
				incList.add(incident);
		}
	}
    return incList;
}

public static EntityDTO createTaskFromIncident(EntityDTO incident){
    EntityDTO task = new EntityDTO("task");
    if (incident.get("DISPLAYNAME")!=null)
        task.set("DISPLAYNAME", incident.getAsString("DISPLAYNAME"));
    if (incident.get("DESCRIPTION")!=null)
         task.set("DESCRIPTION", incident.getAsString("DESCRIPTION"));
    if (incident.get("workgroup")!=null)
        task.set("workgroup", incident.getAsInteger("workgroup"));
    task.set("parent_entitytype",incident.getTableName());
    task.set("parent_entityid", incident.getKeyValue());
    EntityDTO cal = getTaskCalendar(task);
    if (cal == null)
        cal = QueryUtils.getRecordByShortName("calendar", "24x7");
    task.set("calendarname", cal.getAsString("shortname"));
    if (!incident.isFieldEmpty("deadline"))
        task.set("deadline", incident.getAsDateTime("deadline"));
    if (!incident.isFieldEmpty("citype"))
         task.set("citype", incident.getAsInteger("citype"));
    if (!incident.isFieldEmpty("ci"))
        task.set("ci", incident.getAsInteger("ci"));
    if (!incident.isFieldEmpty("ci_format"))
        task.set("ci_format", incident.getAsInteger("ci_format"));
    task.doInsert();  
    task.processTransitions();
    return task;
}

public static EntityDTO getTaskCalendar(EntityDTO task){
    Integer calendarId = null;
    if (!task.isFieldEmpty("workgroup")){
        EntityDTO workgroup = QueryUtils.getRecordById("workgroup",task.getAsInteger("workgroup"));
        if (!CommonUtils.isEmpty(workgroup) && !CommonUtils.isEmpty(workgroup.get("calendar")))
            calendarId = workgroup.getAsInteger("calendar");

    } 
    if (calendarId==null){
        EntityDTO workspace = new EntityDTO("workspace", ContextUtils.getWorkspaceId());
        if ((workspace!=null) && (workspace.get("calendar")!=null))
            calendarId = workspace.getAsInteger("calendar");
    }
    if (calendarId!=null){
        EntityDTO cal = QueryUtils.getRecordById("calendar", calendarId);
        return cal;
    }
    return null;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}