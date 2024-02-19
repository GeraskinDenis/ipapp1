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

public class TaskUtils {

private static final Log log = Log.getLogger(TaskUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static boolean checkTasksCompleted(EntityDTO request) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("TASK","PARENT_ENTITYTYPE ='"+request.getTableName()+"' and parent_entityid ="+request.getId()+" and not coalesce(workflowstepname,'123') in ('cancelled')");
	for (EntityDTO task:taskList){
		if (!task.getAsString("WORKFLOWSTEPNAME").equals("completed"))
			return false;
	}
	return true;
}

public static boolean checkActiveTasksExist(EntityDTO request){
	Integer taskSize = QueryUtils.getCount("TASK","PARENT_ENTITYTYPE ='"+request.getTableName()+"' and parent_entityid ="+request.getId()+" and not coalesce(workflowstepname,'123') in ('cancelled','completed')");
	log.info("checkActiveTasksExist taskSize="+taskSize);
	if (taskSize>0)
	    return true;
	else
	    return false;
}

public static boolean checkJustOneTaskInWork(EntityDTO request) throws Exception{
    Integer taskSize = QueryUtils.getCount("TASK","PARENT_ENTITYTYPE ='"+request.getTableName()+"' and parent_entityid ="+request.getId()+" and coalesce(workflowstepname,'123') in ('inwork')");
	log.info("checkJustOneTaskInWork taskSize="+taskSize);
	if (taskSize>0)
	    return true;
	else
	    return false;
}

public static List<EntityDTO> getActiveTaskList(EntityDTO request) throws Exception{
	List<EntityDTO> taskList = QueryUtils.getRecordList("TASK","PARENT_ENTITYTYPE ='"+request.getTableName()+"' and parent_entityid ="+request.getId()+" and not coalesce(workflowstepname,'123') in ('cancelled')");
	return taskList;
}


public static void cancelActiveTasks(EntityDTO request) throws Exception{
	List<EntityDTO> taskList = getActiveTaskList(request);
	EntityDTO wfStep = QueryUtils.getRecordByShortName("workflowstep","9b65fa50-e9e7-9331-3975-93371103505e");
	if (CommonUtils.isEmpty(wfStep))
	    return;
	ContextUtils.addMessage("найдено нарядов ="+taskList.size());
	for (EntityDTO task:taskList){
	    task.setWorkflowStepId(wfStep.getId());
	    ContextUtils.addMessage("задан статус наряда ="+task.getId()+", статус ="+task.get("workflowstepname"));
	    task.doUpdate();
	}
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}