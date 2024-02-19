package EntityTypeRule;

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
DisplayName: Шаблон работы
Fields:
 - activityname (Название работы) - STRING
 - calendarname (Рабочий график) - STRING
 - category (Категория работ) - STRING
 - ci_shortname (КЕ) - STRING
 - ci_shortname_format (Формат объекта работ) - INTEGER
 - ciids (Объект работ) - INTEGER
 - ciquery (Запрос для выборки объектов работ) - STRING
 - citypeid (Тип КЕ) - INTEGER
 - consists_of_tasks (Состоит из задач) - BOOLEAN
 - coordinator (Координатор) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - cumulativeshift (Накопительное смещение на длит. работ) - BOOLEAN
 - days (Дни с начала месяца ) - INTEGER
 - daysofweek (Дни недели) - INTEGER
 - daysofyear (Дни года (DD.MM)) - STRING
 - deadline_duration (Длительность до крайнего срока) - DOUBLE
 - deadlinetimeunit (Ед. изм. длительности) - STRING
 - deadlineunitcount (Длительность работ (по раб. графику)) - INTEGER
 - description (Описание) - TEXT
 - displayname (Название) - STRING
 - generatetodate (Крайняя дата генерации) - DATETIME
 - generationtype (Тип генерации) - STRING
 - initiator (Инициатор) - INTEGER
 - inter_templ (Шаблон обращения) - INTEGER
 - intervaltimeunit (Ед. измерения интервала) - STRING
 - intervalunits (Кол-во единиц интервала) - INTEGER
 - isactive (Активно) - BOOLEAN
 - lastdays (Количество дней с конца месяца) - INTEGER
 - nextplannedtime (Ближайшее время генерации) - DATETIME
 - onlastday (Добавить дни с конца месяца) - BOOLEAN
 - plannedactivity_templateid (ID) - INTEGER
 - plannedlaborintensity (Плановая трудоемкость) - DOUBLE
 - plannedtimetype (Использовать вычисленную дату как) - STRING
 - planningtimeunit (Горизонт планирования) - STRING
 - repeatenddate (Последняя плановая дата) - DATETIME
 - repeattype (Тип повторения) - STRING
 - shortname (Внутреннее имя) - STRING
 - testplan (Тест) - TEXT
 - timeofday (Время суток) - TIME
 - timezone (Часовой пояс) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
 - workgroup (Рабочая группа) - INTEGER
*/

public class plannedactivity_template_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Заполнить поле Календарь (Шаблон ППР)
		//rule <sn>13d3b50e-a00f-99e0-016f-a4ffc7871d0d</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.setIfEmpty("calendarname", "24x7");
			//</body>
		}
		//endregion

		//region Set PlannedActivityTemplate repeatEndDate
		//rule <sn>5e8343a3-0606-f7d3-5da9-0ef06634052c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			DateTime planningEndTime = Lib.PlannedActivityTemplateUtils.getLastPlannedDate(record);
			if(planningEndTime != null) {
			    DateTime generateToDate = record.getDateTime("generateToDate");
			    if(generateToDate != null && generateToDate.isBefore(planningEndTime)) 
			        planningEndTime = generateToDate;
			    record.set("repeatEndDate", planningEndTime);
			}
			//</body>
		}
		//endregion

		//region CopyWeekdaysToDays
		//rule <sn>868b5d66-3636-9ef3-1dd8-482a10d5ec4f</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("generationtype"), "regularly") && CommonUtils.isSame(record.getAsString("repeattype"), "weekly")/*if*/) {
			//<body>
			if (record.get("daysofweek") != null) {
			    if (!CommonUtils.isSame( oldrecord.getStringArray("daysofweek"), record.getStringArray("daysofweek") )) {
			        record.set("days", record.getStringArray("daysofweek"));
			    }
			}
			else {
			    record.set("days", null);
			}
			//</body>
		}
		//endregion

		//region Установить значение поля ciId из ci_shortname
		//common rule <sn>7c4c7fe8-3640-24a3-98e0-be6afb03834c</sn>
		//entities: plannedactivity_template, plannedactivity
		if (/*if*/record.isFieldEmpty("CiShortnameChanged")/*if*/) {
			//<body>
			if(!CommonUtils.isSame(record.getAsString("ci_shortname"), oldrecord.getAsString("ci_shortname"))) {
			        
			    String tableName = CIType.getCiTypeTableNameById(record.getAsInteger("ciTypeId"));
			    EntityDTO ciEntity = QueryUtils.getRecord(tableName, "shortname = '" + record.getAsString("ci_shortname") + "'");
			    
			    if( ciEntity != null && !ciEntity.isEmpty() && !CommonUtils.isEmpty(ciEntity.getId()) ) {
			        record.set("CiShortnameChanged", true);
			        record.set("ciId", ciEntity.getId());
			    }
			}
			//</body>
		}
		//endregion

		//region Установить значение поля ci_shortname из ciId
		//common rule <sn>619567ec-6e3a-4a82-fe9b-4c21e76d0426</sn>
		//entities: plannedactivity_template, plannedactivity
		if (/*if*/!record.isFieldEmpty("ciTypeId") && !record.isFieldEmpty("ciId") && !CommonUtils.isSame(record.getAsInteger("ciId"), oldrecord.getAsInteger("ciId"))/*if*/) {
			//<body>
			String tableName = CIType.getCiTypeTableNameById(record.getAsInteger("ciTypeId"));
			EntityDTO ciEntity = new EntityDTO(tableName, record.getAsInteger("ciId"));
			
			if(ciEntity != null && !ciEntity.isEmpty() && !ciEntity.isFieldEmpty("shortname")) {
			    record.set("CiShortnameChanged", true);
			    record.set("ci_shortname", ciEntity.getAsString("shortname"));
			    
			    // учтановить филдформат для поля
			    Integer ffId = Lib.FieldFormatUtils.getComplexFieldFormatIdByCiTypeId(record.getAsInteger("ciTypeId"));
			    if (ffId == null) ffId = 5;
			    record.set("ci_shortname_format", ffId);
			}    
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Заполнить поле Календарь (Шаблон ППР)
		//rule <sn>13d3b50e-a00f-99e0-016f-a4ffc7871d0d</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.setIfEmpty("calendarname", "24x7");
			//</body>
		}
		//endregion

		//region Set PlannedActivityTemplate repeatEndDate
		//rule <sn>5e8343a3-0606-f7d3-5da9-0ef06634052c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			DateTime planningEndTime = Lib.PlannedActivityTemplateUtils.getLastPlannedDate(record);
			if(planningEndTime != null) {
			    DateTime generateToDate = record.getDateTime("generateToDate");
			    if(generateToDate != null && generateToDate.isBefore(planningEndTime)) 
			        planningEndTime = generateToDate;
			    record.set("repeatEndDate", planningEndTime);
			}
			//</body>
		}
		//endregion

		//region CopyWeekdaysToDays
		//rule <sn>868b5d66-3636-9ef3-1dd8-482a10d5ec4f</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("generationtype"), "regularly") && CommonUtils.isSame(record.getAsString("repeattype"), "weekly")/*if*/) {
			//<body>
			if (record.get("daysofweek") != null) {
			    if (!CommonUtils.isSame( oldrecord.getStringArray("daysofweek"), record.getStringArray("daysofweek") )) {
			        record.set("days", record.getStringArray("daysofweek"));
			    }
			}
			else {
			    record.set("days", null);
			}
			//</body>
		}
		//endregion

		//region Установить значение поля ciId из ci_shortname
		//common rule <sn>7c4c7fe8-3640-24a3-98e0-be6afb03834c</sn>
		//entities: plannedactivity_template, plannedactivity
		if (/*if*/record.isFieldEmpty("CiShortnameChanged")/*if*/) {
			//<body>
			if(!CommonUtils.isSame(record.getAsString("ci_shortname"), oldrecord.getAsString("ci_shortname"))) {
			        
			    String tableName = CIType.getCiTypeTableNameById(record.getAsInteger("ciTypeId"));
			    EntityDTO ciEntity = QueryUtils.getRecord(tableName, "shortname = '" + record.getAsString("ci_shortname") + "'");
			    
			    if( ciEntity != null && !ciEntity.isEmpty() && !CommonUtils.isEmpty(ciEntity.getId()) ) {
			        record.set("CiShortnameChanged", true);
			        record.set("ciId", ciEntity.getId());
			    }
			}
			//</body>
		}
		//endregion

		//region Установить значение поля ci_shortname из ciId
		//common rule <sn>619567ec-6e3a-4a82-fe9b-4c21e76d0426</sn>
		//entities: plannedactivity_template, plannedactivity
		if (/*if*/!record.isFieldEmpty("ciTypeId") && !record.isFieldEmpty("ciId") && !CommonUtils.isSame(record.getAsInteger("ciId"), oldrecord.getAsInteger("ciId"))/*if*/) {
			//<body>
			String tableName = CIType.getCiTypeTableNameById(record.getAsInteger("ciTypeId"));
			EntityDTO ciEntity = new EntityDTO(tableName, record.getAsInteger("ciId"));
			
			if(ciEntity != null && !ciEntity.isEmpty() && !ciEntity.isFieldEmpty("shortname")) {
			    record.set("CiShortnameChanged", true);
			    record.set("ci_shortname", ciEntity.getAsString("shortname"));
			    
			    // учтановить филдформат для поля
			    Integer ffId = Lib.FieldFormatUtils.getComplexFieldFormatIdByCiTypeId(record.getAsInteger("ciTypeId"));
			    if (ffId == null) ffId = 5;
			    record.set("ci_shortname_format", ffId);
			}    
			//</body>
		}
		//endregion

		//region RefreshPALengthOnTaskLengthes
		//rule <sn>74b877df-d48d-3295-a13a-bab62d9a91b4</sn>
		if (/*if*/record.getAsBooleanNullSub("consists_of_tasks", false)/*if*/) {
			//<body>
			Lib.PlannedActivityTemplateUtils.calcTasksSumDuration(record);
			//</body>
		}
		//endregion

	}

	public static void beforeDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Удалить связанные вложения
		//common rule <sn>ea7d2071-87d9-4f96-0ee3-99eacc1cf68a</sn>
		//entities: plannedactivity_template, plannedactivity
		if (/*if*/!record.isFieldEmpty("shortname")/*if*/) {
			//<body>
			List<EntityDTO> attList = QueryUtils.getRecordList("documentations_po", 
			    "citype_shortname='" + record.getTableName() + "'"
			    + " and shortname = '" + record.getAsString("shortname") + "'");
			if (attList != null) {
			    for(EntityDTO att : attList) {
			        att.doDelete();
			    } 
			}
			//</body>
		}
		//endregion

	}

	public static void afterDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RefreshPAonTemplateChange
		//rule <sn>013fb058-ac0e-4811-111f-f5337fba942f</sn>
		if (/*if*/record.get("autoUpdated") == null/*if*/) {
			//<body>
			// Проверка и пересоздание ППР
			log.info("start 1501");
			// log.info("reord = " + record);
			// log.info("oldreord = " + oldrecord);
			
			String[] fieldTitles = { 
			    "activityname", 
			    "generationtype", 
			    "repeattype",
			    "timeofday", 
			    "days", 
			    "lastdays", 
			    "nextplannedtime", 
			    "intervaltimeunit", 
			    "intervalunits", 
			    "daysofyear", 
			    "repeatenddate", 
			    "citypeid", 
			    //"ciids", 
			    "ciquery", 
			    // "daysofweek", 
			    "calendarname", 
			    "plannedtimetype", 
			    "deadlinetimeunit", 
			    "deadlineunitcount", 
			    "planningtimeunit", 
			    "cumulativeshift",
			    "generatetodate",
			    "consists_of_tasks"
			};
			// record inactive
			if(!record.getAsBooleanNullSub("isactive", false)) {
			    // if it is just deactivated, then cancel plannedactivities
			    if(oldrecord.getAsBooleanNullSub("isactive", false)) {
			        log.info("record deactivation, skip fields check");
			        Lib.PlannedActivityTemplateUtils.cancelPlannedActivities(oldrecord);
			    }
			} 
			// record just activated
			else if(!oldrecord.getAsBooleanNullSub("isactive", true)) {
			    log.info("record activation, skip fields check");
			    Lib.PlannedActivityTemplateUtils.createPlannedActivities(record);
			}
			else {
			    if(record.get("generationType") != null 
			        && CommonUtils.isSame(record.getAsString("generationType"), "regularly")) {
			        for(String s : fieldTitles){
			            log.info("check field: " + s);
			            if(!CommonUtils.isSame(record.getAsString(s), "[]") && !CommonUtils.isSame(record.get(s), oldrecord.get(s))) {
			                log.info("values of " + s + " are not equal, now: '" + record.get(s) + 
			                    "', old: '" + oldrecord.get(s) + "', regenerate plannedactivities!");
			                Lib.PlannedActivityTemplateUtils.cancelPlannedActivities(oldrecord);
			                Lib.PlannedActivityTemplateUtils.createPlannedActivities(record);
			                break;
			            }
			        }
			    }
			}
			
			log.info("end 1501");
			//</body>
		}
		//endregion

	}

}
