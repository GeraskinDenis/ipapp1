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

public class SDUtils {

private static final Log log = Log.getLogger(SDUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void CreateSDObjectRelation(EntityDTO record1, EntityDTO record2,String type) throws Exception{
	EntityDTO relation = new EntityDTO("SD_OBJRELATION");
	relation.set("ENTITYTYPE1", record1.getEntityTypeId());
	relation.set("ENTITYTYPE2", record2.getEntityTypeId());
	relation.set("ENTITYID1", record1.getKeyValue());
	relation.set("ENTITYID2", record2.getKeyValue());
	relation.set("RELATION_TYPE",type);
	if (!QueryUtils.isExist("SD_OBJRELATION","ENTITYTYPE1="+record1.getEntityTypeId()+" and ENTITYTYPE2="+record2.getEntityTypeId()+
		" and ENTITYID1="+record1.getKeyValue()+" and ENTITYID2 = "+record2.getKeyValue()))
		relation.doInsert();
	return;
}

public static boolean CheckAllTicketRequirementsComplete(EntityDTO ticket) throws Exception{
	List<EntityDTO> reqsList = QueryUtils.getRecordList("SD_REQUIREMENT","ticketid="+ticket.getKeyValue());
	for (EntityDTO req:reqsList){
		if (!req.getAsString("WORKFLOWSTEPNAME").equals("completed"))
			return false;
	}
	return true;
}

public static boolean CheckOneTicketRequirementInWork(EntityDTO ticket) throws Exception{
	List<EntityDTO> reqsList = QueryUtils.getRecordList("SD_REQUIREMENT","ticketid="+ticket.getKeyValue());
	for (EntityDTO req:reqsList){
		if ("inwork".equals(req.getAsString("WORKFLOWSTEPNAME")))
			return true;
	}
	return false;
}

public static void CreateSDFollowerInitiatorRelation(EntityDTO sdObj) throws Exception{
	EntityDTO initiator = new EntityDTO("SD_FOLLOWERS");
	initiator.set("ENTITYTYPEID", sdObj.getEntityTypeId());
	initiator.set("ENTITYID", sdObj.getKeyValue());
	initiator.set("USERID", sdObj.getAsInteger("INITIATOR"));
	if (!QueryUtils.isExist("SD_FOLLOWERS","ENTITYTYPEID="+sdObj.getEntityTypeId()+" and ENTITYID="+sdObj.getKeyValue()+ " and USERID = "+sdObj.getAsInteger("INITIATOR")))
		initiator.doInsert();
	return;
}

public static void CreateSDFollowerAssignedToRelation(EntityDTO sdObj) throws Exception{
	EntityDTO assignedTo = new EntityDTO("SD_FOLLOWERS");
	assignedTo.set("ENTITYTYPEID", sdObj.getEntityTypeId());
	assignedTo.set("ENTITYID", sdObj.getKeyValue());
	assignedTo.set("USERID", sdObj.getAsInteger("ASSIGNEDTO"));
	if (!QueryUtils.isExist("SD_FOLLOWERS","ENTITYTYPEID="+sdObj.getEntityTypeId()+" and ENTITYID="+sdObj.getKeyValue()+	" and USERID = "+sdObj.getAsInteger("ASSIGNEDTO")))
		assignedTo.doInsert();
	return;
}


public static boolean checkAllRequirementTasksEqualStatus(EntityDTO req, String status) throws Exception{
	List<EntityDTO> tasksList = QueryUtils.getRecordList("SD_TASK", "REQUIREMENTID="+req.getKeyValue());
	for (EntityDTO task:tasksList){
		if (!task.getAsString("WORKFLOWSTEPNAME").equals(status)) {
			log.info(req.getAsBooleanNullSub("ENABLEAUTOTRANSITION", true) + "<<<<<<<<<<<<<<<<<<<<<") ;
			return false;
		}
	}
	return true;
}

public static boolean checkAllRequirementTasksInActive(EntityDTO req) throws Exception{
	List<EntityDTO> tasksList = QueryUtils.getRecordList("SD_TASK", "REQUIREMENTID="+req.getKeyValue());
	for (EntityDTO task:tasksList){
		if (!Arrays.asList("completed", "closed","declined").contains(task.getAsString("WORKFLOWSTEPNAME")))
			return false;
	}
	return true;
}

public static boolean checkOneRequirementTasksEqualStatus(EntityDTO req, String status) throws Exception{
	List<EntityDTO> tasksList = QueryUtils.getRecordList("SD_TASK", "REQUIREMENTID="+req.getKeyValue());
	for (EntityDTO task:tasksList){
		if (task.getAsString("WORKFLOWSTEPNAME").equals(status))
			return true;
	}
	return false;
}

public static boolean checkAllRequirementTasksCompletedTransition(EntityDTO req, String transitionName) throws Exception{
	List<EntityDTO> tasksList = QueryUtils.getRecordList("SD_TASK", "REQUIREMENTID="+req.getKeyValue());
	for (EntityDTO task:tasksList){
		EntityDTO transition= QueryUtils.getRecord("WORKFLOWTRANSITION","WORKFLOWID="+task.getWorkflowId()+" and SHORTNAME='"+transitionName+"'");
		if (transition !=null){
			if (!QueryUtils.isExist("EntityTransitionLog", "ENTITYTYPEID="+task.getEntityTypeId()+" and ENTITYID ="+task.getKeyValue()+" and TRANSITIONID="+transition.getKeyValue())){
				return false;
			}
		}
		else 
			return false;
	}
	return true;
}

public static boolean enableSupportTaskAutoTransition(EntityDTO task) throws Exception{
	if (task.get("REQUIREMENTID")!=null){
		EntityDTO req = new EntityDTO("SD_REQUIREMENT", task.getAsInteger("REQUIREMENTID"));
		if (req!=null)
			return !req.getAsBooleanNullSub("ENABLEAUTOTRANSITION",true);
	}
	return false;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}