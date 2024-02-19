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

public class ITSMInteractionUtils {

private static final Log log = Log.getLogger(ITSMInteractionUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void createRelationObjectFromInteraction(EntityDTO record1, EntityDTO record2,String type) throws Exception{
	EntityDTO relation = new EntityDTO("OBJRELATION");
	relation.set("ENTITYTYPE1", record1.getTableName());
	relation.set("ENTITYTYPE2", record2.getTableName());
	relation.set("ENTITYID1", record1.getKeyValue());
	relation.set("ENTITYID2", record2.getKeyValue());
	relation.set("RELATION_TYPE",type);
	if (!QueryUtils.isExist("OBJRELATION","ENTITYTYPE1='"+record1.getTableName()+"' and ENTITYTYPE2='"+record2.getTableName()+
		"' and ENTITYID1="+record1.getKeyValue()+" and ENTITYID2 = "+record2.getKeyValue()))
		relation.doInsert();
	return;
}

public static Boolean checkRelatedInteractionClosed(EntityDTO object) throws Exception{
	List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='"+object.getTableName()+"' and entityid2="+object.getId());
	for (EntityDTO rel:relList){
		EntityDTO inter = new EntityDTO("interaction", rel.getAsInteger("ENTITYID1"));
		if (!CommonUtils.isEmpty(inter)){
			if (!Arrays.asList("closed").contains(inter.getAsString("WORKFLOWSTEPNAME")))
				return false;
		}
	}
	return true;
}

public static EntityDTO calcDeadlineFromTemplate(EntityDTO pTemplate, EntityDTO plannedActivity,DateTime plannedDateTime){
  // start - установить дату как начало работ, end - установить дату, как крайний срок
    String units = "deadlinetimeunit", unitCount = "deadlineunitcount";
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
    log.info("временные параметры ППР рассчитаны");
    return plannedActivity;
} 

public static void DeleteInteraction(EntityDTO interaction) throws Exception{
	List<EntityDTO> histList =  QueryUtils.getRecordList("HISTORY", "ENTITYTYPEID = 314 AND ENTITYID = "+interaction.getKeyValue());
	for (EntityDTO hist:histList)
		hist.doDelete();
	List<EntityDTO> alertList = QueryUtils.getRecordList("ALERTLOG","ENTITYTYPEID = 314 AND ENTITYID = "+interaction.getKeyValue());
	for (EntityDTO alert:alertList)
		alert.doDelete();
	List<EntityDTO> commentList = QueryUtils.getRecordList("COMMENT","ENTITYTYPEID = 314 AND ENTITYID = "+interaction.getKeyValue());
	for (EntityDTO comment:commentList)
		comment.doDelete();
	List<EntityDTO> jobList = QueryUtils.getRecordList("DEAMONJOB","ENTITYTAG = '"+interaction.getTag()+"'");
	for (EntityDTO job:jobList)
		job.doDelete();
	List<EntityDTO> relList = QueryUtils.getRecordList("SM_OBJRELATION","ENTITYTYPE1 =314  AND ENTITYID1="+interaction.getKeyValue());
	for (EntityDTO rel:relList)
		rel.doDelete();
	interaction.doDelete();
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}