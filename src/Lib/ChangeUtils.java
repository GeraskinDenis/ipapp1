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

public class ChangeUtils {

private static final Log log = Log.getLogger(ChangeUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void createChangeTasksFromTemplate(EntityDTO change) throws Exception{
    if (change.isFieldEmpty("change_template")){
        log.error("Template is empty!");
        return;
    }
    DateTime startTime = new DateTime().withSecondOfMinute(0);
    log.info("parent_entityid="+change.getAsInteger("change_template"));
    log.info("parent_entitytype="+change.getTableName());
    List<EntityDTO> taskTemplList = QueryUtils.getRecordList("task_template", 
    "parent_entitytype ='change_template' and parent_entityid = " + change.getAsInteger("change_template"));
    log.info("найдено шаблонов задач = "+taskTemplList.size());
    for (EntityDTO taskTempl:taskTemplList){
        if (taskTempl.get("predecessor")==null)
            Lib.TaskTemplateUtils.addPlannedActivityTask(taskTempl, change, taskTemplList, startTime, null, true, null);
    }
}


public static Boolean checkChangedRelatedToProblemCompleted(EntityDTO problem){
    List<EntityDTO> changeList = QueryUtils.getRecordList("problem_change_relation","problem = "+problem.getId());
    if (changeList.size()==0)
        return true;
    for (EntityDTO changeRel:changeList){
        if (CommonUtils.isEmpty(changeRel.get("change")))
            continue;
        EntityDTO change = QueryUtils.getRecordById("change",changeRel.getAsInteger("change"));
        if (!CommonUtils.isEmpty(change) && !CommonUtils.isSame(change.getAsString("workflowstepname"),"completed"))
            return false;
    }
    return false;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}