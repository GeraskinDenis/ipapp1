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

public class CalendarCalcUtils {

private static final Log log = Log.getLogger(CalendarCalcUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static DateTime addWorkDays(EntityDTO record, Date startDate, Integer duration) throws Exception{        
    DateTime finishDate = null;
    EntityDTO cal = null;
    if (record.get("ORGUNIT")!=null){
        EntityDTO orgUnit = new EntityDTO("R_ORGUNIT", record.getAsInteger("ORGUNIT"));
        if (orgUnit!=null){
            if (orgUnit.get("CALENDAR")!=null){
                cal = new EntityDTO("CALENDAR",orgUnit.getAsInteger("CALENDAR"));
            }
        }
    }
    if (cal!=null){
        finishDate = CalendarUtils.addWorkDays(cal.getAsString("SHORTNAME"), CastUtils.getDateTime(startDate), duration);
    }
    return finishDate;
}

public static Integer getWorkDays(EntityDTO record, Date startDate, Date finishDate) throws Exception{      
    Integer duration = null;
    EntityDTO cal = null;
    if (record.get("ORGUNIT")!=null){
        EntityDTO orgUnit = new EntityDTO("R_ORGUNIT", record.getAsInteger("ORGUNIT"));
        if (orgUnit!=null){
            if (orgUnit.get("CALENDAR")!=null){
                cal = new EntityDTO("CALENDAR",orgUnit.getAsInteger("CALENDAR"));
            }
        }
    }
    if (cal!=null){
        duration = CalendarUtils.getWorkDays(cal.getAsString("SHORTNAME"), CastUtils.getDateTime(startDate), CastUtils.getDateTime(finishDate));
    }
    return duration;
}

public static Integer getWorkDaysInMonth(Integer calId, Integer year, Integer month) throws Exception{      
    Integer duration = null;
    EntityDTO cal = new EntityDTO("CALENDAR",calId);
    DateTime startDate = new DateTime(year,month, 1,0,0);
    DateTime finishDate = startDate.dayOfMonth().withMaximumValue().withTime(23,59,59,0);
    log.info("finishDate="+finishDate);
    if (cal!=null)
        duration = CalendarUtils.getWorkDays(cal.getAsString("SHORTNAME"), startDate, finishDate);
    return duration;
}

public static Integer getWorkHoursInStandartMonth(Integer calId, Integer year, Integer month) throws Exception{      
   return getWorkDaysInMonth(calId, year, month)*8;
}

public static Integer getWorkHoursBtwnDates(String calendarName, DateTime stDateTime, DateTime enDateTime) throws Exception{ 
    Period p = CalendarUtils.getPeriod(calendarName, stDateTime, enDateTime);
    Integer duration = 0;
    if (p!=null){
        if(p.getDays() > 0) 
            duration = p.getDays()*24;
        if (p.getHours() > 0) 
            duration += p.getHours();
    }
    //log.info("длительность в часах ="+duration);
    return duration;
}

public static Integer getWorkMinutesBtwnDates(String calendarName, DateTime stDateTime, DateTime enDateTime) throws Exception{ 
    Period p = CalendarUtils.getPeriod(calendarName, stDateTime, enDateTime);
    log.info("period in minutes = " + p);
    Integer duration = 0;
    if (p != null){
        if(p.getDays() > 0) 
            duration = p.getDays() * 24 * 60;
        if (p.getHours() > 0) 
            duration += p.getHours() * 60;
        if (p.getMinutes() > 0)
            duration += p.getMinutes();
    }
    log.info("длительность в minutes ="+duration);
    return duration;
}

public static Integer getWorkHoursInMonth(String calendarName, Integer year, Integer month) throws Exception{      
   DateTime startDate = new DateTime(year,month, 1,0,0);
   DateTime finishDate = startDate.dayOfMonth().withMaximumValue().withTime(23,59,59,0);
   return getWorkHoursBtwnDates(calendarName, startDate, finishDate);
}

public static DateTime minusWorkDays(String calendarName, DateTime finishDate, Integer numberOfDays) throws Exception{        
    DateTime startDate = null;
    //Period period = Period.ofDays(numberOfDays);
    Integer hoursInDay = 24;
    if (CommonUtils.isSame("e8b083ae-4f39-bac5-356b-cd8338aaaa40",calendarName))
        hoursInDay = 9;
    else if (CommonUtils.isSame("12_5",calendarName))
        hoursInDay = 12;
    Period period = org.joda.time.Period.hours(numberOfDays*hoursInDay);
    startDate = CalendarUtils.minusPeriod(calendarName, finishDate, period);
    return startDate;
}


public static DateTime getNextWorkHour(DateTime startDate, String calendarName) throws Exception{   
    DateTime nextDate = CalendarUtils.addPeriod(calendarName, startDate, Period.seconds(1));
    nextDate = nextDate.minusSeconds(1);
    return nextDate;
}


public static DateTime getPrevWorkHour(DateTime startDate, String calendarName) throws Exception{   
    DateTime nextDate = CalendarUtils.minusPeriod(calendarName, startDate, Period.seconds(1));
    nextDate = nextDate.plusSeconds(1);
    return nextDate;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}