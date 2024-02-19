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

public class NotificationUtils {

private static final Log log = Log.getLogger(NotificationUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static String getRecordName(EntityDTO record) throws Exception{
     return record.getTableName(); 
}

public static String getRequirementInitiatorFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_REQUIREMENT", "SD_REQUIREMENTID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("INITIATOR")!=null){
		EntityDTO initiatorUser = QueryUtils.getRecord("USER", "USERID = " + req.getAsInteger("INITIATOR"));
		return initiatorUser.getAsString("FULLNAME");
	}
	return "не указан";
}

public static String getObjectDisplayNameFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = new EntityDTO(comment.getAsInteger("ENTITYTYPEID"), comment.getAsInteger("ENTITYID"));
	if ((req!=null) && (req.get("DISPLAYNAME")!=null))
		return req.getAsString("DISPLAYNAME");
	return "не указано";
}

public static String getObjectDescriptionFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = new EntityDTO(comment.getAsInteger("ENTITYTYPEID"), comment.getAsInteger("ENTITYID"));
	if ((req!=null) && (req.get("DESCRIPTION")!=null))
		return req.getAsString("DESCRIPTION");
	return "не указано";
}


public static String getRequirementPriorityFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_REQUIREMENT", "SD_REQUIREMENTID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("PRIORITY")!=null){
		switch (req.getAsString("PRIORITY")){
			case "1":
				return "Низкий";
			case "2": 
				return "Средний";
			case "3":
				return "Высокий";
			default: 
				return "не указан"; 
		}
	}
	return "не указан";
}

public static String getRequirementAssignedToFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_REQUIREMENT", "SD_REQUIREMENTID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("ASSIGNEDTO")!=null){
		EntityDTO assignedUser = QueryUtils.getRecord("USER", "USERID = " + req.getAsInteger("ASSIGNEDTO"));
		return assignedUser.getAsString("FULLNAME");
	}
	return "не указан";
}

public static String getTaskInitiatorFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TASK", "SD_TASKID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("INITIATOR")!=null){
		EntityDTO initiatorUser = QueryUtils.getRecord("USER", "USERID = " + req.getAsInteger("INITIATOR"));
		return initiatorUser.getAsString("FULLNAME");
	}
	return "не указан";
}

public static String getTaskDisplayNameFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TASK", "SD_TASKID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("DISPLAYNAME")!=null)
		return req.getAsString("DISPLAYNAME");
	return "не указано";
}

public static String getTaskDescriptionFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TASK", "SD_TASKID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("DESCRIPTION")!=null)
		return req.getAsString("DESCRIPTION");
	return "не указано";
}

public static String getTaskTypeFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TASK", "SD_TASKID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("TYPE")!=null){
		switch (req.getAsString("TYPE")){
			case "newfeature":
				return "Доработка";
			case "bug": 
				return "Баг";
			default: 
				return "не указан"; 
		}
	}
	return "не указан";
}

public static String getTaskAssignedToFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TASK", "SD_TASKID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("ASSIGNEDTO")!=null){
		EntityDTO initiatorUser = QueryUtils.getRecord("USER", "USERID = " + req.getAsInteger("ASSIGNEDTO"));
		return initiatorUser.getAsString("FULLNAME");
	}
	return "не указан";
}

public static String getTicketInitiatorFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TICKET", "SD_TICKETID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("INITIATOR")!=null){
		EntityDTO initiatorUser = QueryUtils.getRecord("USER", "USERID = " + req.getAsInteger("INITIATOR"));
		return initiatorUser.getAsString("FULLNAME");
	}
	return "не указан";
}

public static String getTicketDisplayNameFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TICKET", "SD_TICKETID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("DISPLAYNAME")!=null)
		return req.getAsString("DISPLAYNAME");
	return "не указано";
}

public static String getTicketDescriptionFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TICKET", "SD_TICKETID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("DESCRIPTION")!=null)
		return req.getAsString("DESCRIPTION");
	return "не указано";
}

public static String getTicketImpactFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TICKET", "SD_TICKETID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("IMPACT")!=null){
		switch (req.getAsString("IMPACT")){
			case "1":
				return "Один пользователь";
			case "2": 
				return "Группа пользователей";
			case "3":
				return "Все пользователи";
			default: 
				return "не указано"; 
		}
	}
	return "не указано";
}

public static String getTicketUrgencyFromComment(EntityDTO comment) throws Exception{
	EntityDTO req = QueryUtils.getRecord("SD_TICKET", "SD_TICKETID = " + comment.getAsInteger("ENTITYID"));
	if (req.get("URGENCY")!=null){
		switch (req.getAsString("URGENCY")){
			case "1":
				return "3-Низкая";
			case "2": 
				return "2-Средняя";
			case "3":
				return "1-Высокая";
			default: 
				return "не указана"; 
		}
	}
	return "не указана";
}

public static String dateToString(Date d) throws Exception{
    java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
    return dateFormat.format(d);
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}