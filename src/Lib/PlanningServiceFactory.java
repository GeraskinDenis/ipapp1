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

public class PlanningServiceFactory {

private static final Log log = Log.getLogger(PlanningServiceFactory.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

/**
 * Библиотека для инициализации сервиса планирования расписаний 
 * Основые методы интерфейса пларинования расписания PlanningService:
 * - DateTime getNextTime(DateTime startDateTime) - вычисляет ближайшую дату по расписанию начиная с startDateTime
 * - List<DateTime> getNextTimeList(DateTime startDateTime, int cnt) - вычисляет cnt ближайших плановых дат начиная с startDateTime
 * - List<DateTime> getNextTimeList(DateTime startDateTime, DateTime endDateTime) - вычисляет плановые даты между датой начала startDateTime и датой окончания endDateTime
 */

// Инициализация планировщика расписания на основании записи в табилце Шаблон ППР () 
    public static PlanningService createByPlannedActivityTemplate(EntityDTO templateDTO){

        String repeatType = templateDTO.getAsString("repeatType");

        Integer timeZoneId = templateDTO.getAsInteger("timeZone");
        ru.ip.server.calendar.TimeZone timeZone = ru.ip.server.cache.CacheManager.getInstance().getTimeZone(timeZoneId);
        DateTimeZone dateTimeZone = DateTimeZone.forID(timeZone.getShortName());

        PlanningService planningService;
        // Расписание на основании интервала
        if ("interval".equalsIgnoreCase(repeatType)){

            planningService = new IntervalPlanningService(dateTimeZone, templateDTO.getAsString("intervaltimeunit"), templateDTO.getAsInteger("intervalunits"));
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            if (templateDTO.getAsBooleanNullSub("cumulativeshift", false) && !templateDTO.isFieldEmpty("deadlinetimeunit") && !templateDTO.isFieldEmpty("deadlineunitcount")){
                planningService.setShift(templateDTO.getAsString("deadlinetimeunit"), templateDTO.getAsInteger("deadlineunitcount"));
            }
            return planningService;
        }

        // Время суток, на которое нужно установить плановое время.
        LocalTime timeOfDay = CastUtils.getLocalTime(templateDTO.get("timeOfDay"));

        // Через интервал по рабочим дням
        if ("working_days".equalsIgnoreCase(repeatType)){
            planningService = new WorkingDailyPlanningService(dateTimeZone, templateDTO.getAsString("calendarName"), timeOfDay, templateDTO.getAsInteger("intervalunits"));
            if (templateDTO.getAsBooleanNullSub("cumulativeshift", false) && !templateDTO.isFieldEmpty("deadlinetimeunit") && !templateDTO.isFieldEmpty("deadlineunitcount")){
                planningService.setShift(templateDTO.getAsString("deadlinetimeunit"), templateDTO.getAsInteger("deadlineunitcount"));
            }
            return planningService;
        }

        // Ежедневне расписание
        if ("daily".equalsIgnoreCase(repeatType)){
            planningService = new DailyPlanningService(dateTimeZone, timeOfDay);
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            return planningService;
        }

        // Еженедельное расписания
        if ("weekly".equalsIgnoreCase(repeatType)){
            planningService = new WeeklyPlanningService(dateTimeZone, timeOfDay, templateDTO.getIntegerArray("daysofweek"));
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            return planningService;
        }

        // Флаг "Конец периода" - означает, что плановую дату нужно отсчитывать от конца периода.
        Boolean onLastDay = templateDTO.getAsBooleanNullSub("onLastDay", false);
        // Дни для отсчета от конца периода (работает в паре с выставленным флагом onLastDay).
        // Если lastDays не заданы и выставлен флаг onLastDay - плановая дата устанавливается на последний день периода.
        List<Integer> lastDays = templateDTO.getIntegerArray("lastDays");

        // Ежемесячное расписание по числам
        if ("monthly".equalsIgnoreCase(repeatType)){
            planningService = new MonthlyPlanningService(dateTimeZone, timeOfDay, templateDTO.getIntegerArray("days"), onLastDay, lastDays);
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            return planningService;
        }

        // Ежегодное расписания по числам
        if ("annually".equalsIgnoreCase(repeatType)){
            planningService = new AnnualPlanningService(dateTimeZone, timeOfDay, templateDTO.getStringArray("daysOfYear"), onLastDay, lastDays);
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            return planningService;
        }

        // Ежемесячное расписание по дням недели
        if ("monthlyDayOfWeek".equalsIgnoreCase(repeatType)){
            planningService = new DayInWeekOfMonthPlanningService(dateTimeZone, timeOfDay, templateDTO.getIntegerArray("daysofweek"), templateDTO.getIntegerArray("weeks"));
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            return planningService;
        }

        // Ежегодное расписание по дням недели
        if ("annuallyDayOfWeek".equalsIgnoreCase(repeatType)){
            planningService = new DayInWeekAndMonthAnnuallyPlanningService(dateTimeZone, timeOfDay, templateDTO.getIntegerArray("daysofweek"), templateDTO.getIntegerArray("weeks"), templateDTO.getIntegerArray("months"));
            planningService.setCalendar(templateDTO.getAsString("calendarName"));
            return planningService;
        }

        throw new ValidationException("Unknown repeatType: " + repeatType);
    }

private static List<Integer> getIntegerArray(EntityDTO record, String fieldName) {
	List<Integer> result = new ArrayList<>();
	String whereQuery = String.format("entitytypeid = %d AND entityid = %d AND fieldname = '%s'",
		record.getEntityTypeId(), record.getKeyValue(), fieldName);
	List<EntityDTO> values = QueryUtils.getRecordList("integerarray", whereQuery);
	for (EntityDTO value : values) {
		result.add(value.getAsInteger("fieldvalue"));
	}
	return result;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}