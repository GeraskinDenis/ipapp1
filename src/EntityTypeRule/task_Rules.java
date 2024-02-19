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
DisplayName: Наряд
Fields:
 - assignedto (Исполнитель) - INTEGER
 - calendarname (Календарь) - STRING
 - category (Категория) - STRING
 - ci (КЕ) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - ci_shortname (КЕ - строка) - STRING
 - ci_shortname_format (Формат  для КЕ - строка) - INTEGER
 - citype (Тип КЕ) - INTEGER
 - color (Цвет) - STRING, вычисление: CASE WHEN (workflowstepname in ('planned','inwork') and deadline<LOCALTIMESTAMP) THEN '#EB8A8A' WHEN ((workflowstepname='planned' and (planned_start<LOCALTIMESTAMP)) OR (workflowstepname in ('planned','inwork') and planned_finish<LOCALTIMESTAMP)) THEN '#E8E67D' END
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - deadline (Крайний срок) - DATETIME
 - description (Описание) - TEXT
 - displayname (Наименование) - STRING
 - duration (Длительность (часов)) - DOUBLE
 - isactive (Активно) - BOOLEAN
 - isalertviewed (Окно показывалось) - BOOLEAN
 - isnt_nec_for_closing (Необязат. для закрытия) - BOOLEAN
 - location (Местонахождение) - INTEGER
 - parent_entityid (Родительский объект) - INTEGER
 - parent_entitytype (Тип родительского объекта) - STRING
 - planned_finish (Плановое окончание) - DATETIME
 - planned_start (Плановое начало) - DATETIME
 - predecessor (Предшественник) - INTEGER
 - priority (Приоритет) - INTEGER
 - service (Услуга) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - solution (Решение) - TEXT
 - taskid (ID) - INTEGER
 - template (Шаблон) - INTEGER
 - unsuspend_date (Дата возобновления работ) - DATE
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
 - workgroup (Рабочая группа) - INTEGER
 - workspace (Пространство) - INTEGER
*/

public class task_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Calc Task Calendar on Workgroup Change
		//rule <sn>2c24d671-5791-416d-a1a7-1ac663314e32</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("calendarname")) || !CommonUtils.isSame(record.getAsInteger("workgroup"), oldrecord.getAsInteger("workgroup"))/*if*/) {
			//<body>
			EntityDTO cal = Lib.ITSMIncidentUtils.getTaskCalendar(record);
			if (cal == null)
			    cal = QueryUtils.getRecordByShortName("calendar", "24x7");
			if (cal!=null)
			    record.set("calendarname", cal.getAsString("shortname"));
			
			//</body>
		}
		//endregion

		//region Calc Task Planned Finish
		//rule <sn>6ee1f9e6-2eda-890a-c48f-7bb89386f31c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if ((record.get("planned_finish")==null) && (record.get("planned_start")!=null) && (record.get("duration")!=null)){
			    //record.set("planned_finish", Lib.ITSMRequestUtils.addWorkHoursToDate(record, record.getAsDateTime("planned_start"), record.getAsInteger("duration")));
			    String calendarName = record.getAsString("calendarname");
			    if (!CommonUtils.isEmpty(calendarName))
			        record.set("planned_finish",Lib.TaskTemplateUtils.addWorkHoursToDate(record.getDateTime("planned_start"), record.getAsInteger("duration"), calendarName));
			}
			//</body>
		}
		//endregion

		//region Add To Task Description Request Params
		//rule <sn>5f5e0eb0-8842-9187-e914-94a04d3bca34</sn>
		if (/*if*/CommonUtils.isSame("request",record.getAsString("parent_entitytype")) && !CommonUtils.isEmpty(record.get("parent_entityid")) /*if*/) {
			//<body>
			String desc = "<h2>"+"Описание работ"+"</h2>"+CommonUtils.nullSub(record.getAsString("description"),"-");
			EntityDTO request = QueryUtils.getRecordById("request",record.getAsInteger("parent_entityid"));
			if (!CommonUtils.isEmpty(request) && !CommonUtils.isEmpty(request.get("description")))
			    desc += request.getAsString("description");
			record.set("description", desc);
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Process Parent Object
		//rule <sn>bed0a1f4-d188-c5f0-0a2c-f25b99806cd9</sn>
		if (/*if*/(record.get("parent_entitytype")!=null) && (record.get("parent_entityid")!=null)/*if*/) {
			//<body>
			EntityDTO parentObj = new EntityDTO(record.getAsString("parent_entitytype"),record.getAsInteger("parent_entityid"));
			if (parentObj!=null)
			    parentObj.processTransitions();
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Calc Task Calendar on Workgroup Change
		//rule <sn>2c24d671-5791-416d-a1a7-1ac663314e32</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("calendarname")) || !CommonUtils.isSame(record.getAsInteger("workgroup"), oldrecord.getAsInteger("workgroup"))/*if*/) {
			//<body>
			EntityDTO cal = Lib.ITSMIncidentUtils.getTaskCalendar(record);
			if (cal == null)
			    cal = QueryUtils.getRecordByShortName("calendar", "24x7");
			if (cal!=null)
			    record.set("calendarname", cal.getAsString("shortname"));
			
			//</body>
		}
		//endregion

		//region Calc Task Planned Finish
		//rule <sn>6ee1f9e6-2eda-890a-c48f-7bb89386f31c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if ((record.get("planned_finish")==null) && (record.get("planned_start")!=null) && (record.get("duration")!=null)){
			    //record.set("planned_finish", Lib.ITSMRequestUtils.addWorkHoursToDate(record, record.getAsDateTime("planned_start"), record.getAsInteger("duration")));
			    String calendarName = record.getAsString("calendarname");
			    if (!CommonUtils.isEmpty(calendarName))
			        record.set("planned_finish",Lib.TaskTemplateUtils.addWorkHoursToDate(record.getDateTime("planned_start"), record.getAsInteger("duration"), calendarName));
			}
			//</body>
		}
		//endregion

	}

}
