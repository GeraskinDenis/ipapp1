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

public class ITSMServiceUtils {

private static final Log log = Log.getLogger(ITSMServiceUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void calcAvailability(Integer serviceId) throws Exception{
    Integer totalIncidentDuration = 0;
    DateTime now  = new DateTime();
    DateTime firstDayOfMonth = now.withDayOfMonth(1);
    Integer totalTimeInMonth = Seconds.secondsBetween(firstDayOfMonth, now).getSeconds();
    List<EntityDTO> incidentList = QueryUtils.getRecordList("interaction","category='incident' and priority=1 and createdtime>date_trunc('month', current_date) and service = "+serviceId);
    log.info("кол-во инцидентов="+incidentList.size());
    for (EntityDTO incident:incidentList){
    	//Double CalengarUtils.get
    	DateTime startDate =new DateTime(incident.getAsDateTime("createdtime"));
        DateTime finishDate = null;
        if ("completed".equals(incident.getAsString("workflowstepname")))
        	finishDate = new DateTime(incident.getAsDateTime("updatedtime")); 
        else
        	finishDate = now;
    	Seconds seconds = Seconds.secondsBetween(startDate, finishDate);
    	log.info("дата создания = "+startDate+", время завершения="+finishDate);
    	log.info("время инцидента "+incident.getKeyValue()+", ="+seconds.getSeconds());
    	totalIncidentDuration += seconds.getSeconds();
    }
    Double availability = (totalTimeInMonth - totalIncidentDuration)*1.0/(totalTimeInMonth);
    log.info("сек Инцидентов="+totalIncidentDuration);
    log.info("сек в месяце="+totalTimeInMonth);
    log.info("доступность ="+availability);
}
/*
public static Integer getIncidentDuration(EntityDTO incident) throws Exception{
    DateTime startDate =new DateTime(incident.getAsDateTime("createdtime"));
    DateTime finishDate = null;
    Integer incidentDuration = 0;
    if ((incident.get("reopen_count") == null) || (incident.getAsInteger("reopen_count")==0)){
        if ("completed".equals(incident.getAsString("workflowstepname")))
        	finishDate = new DateTime(incident.getAsDateTime("updatedtime")); 
        else
        	finishDate = new DateTime();
    	Seconds seconds = Seconds.secondsBetween(startDate, finishDate);
    	log.info("дата создания = "+startDate+", время завершения="+finishDate);
    	log.info("время инцидента "+incident.getKeyValue()+", ="+seconds.getSeconds());
    	incidentDuration = seconds.getSeconds();
    }
    else{
        incidentDuration = 0;
    }
    
}
 */

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}