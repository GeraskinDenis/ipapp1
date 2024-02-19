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

public class DateCalcUtils {

private static final Log log = Log.getLogger(DateCalcUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static Date addDaysToDate(Date startDate, Integer numbeOfDays){
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    cal.add(Calendar.DATE, numbeOfDays); 
    return cal.getTime(); 
}

public static String addDaysToDateAsString(Date startDate, Integer numbeOfDays){
    Date resultDate =  addDaysToDate(startDate, numbeOfDays);
    return dateToString(resultDate,"dd/MM/yyyy"); 
    
}

public static String dateToString(Date d, String format){
    java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(format);
    return dateFormat.format(d);
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}