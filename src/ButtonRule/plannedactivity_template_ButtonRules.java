package ButtonRule;

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

public class plannedactivity_template_ButtonRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Отменить все запланированные ППР
	//button <sn>b73e2b8f-40a5-21ca-1048-5e806c74d4f8</sn>
	public static void cancel_plannedactivities_1224_onEdit(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region CancelPlannedActivitiesByTemplate
		//rule <sn>776e8ec0-f839-3f0e-483f-e64ad8723be6</sn>
		if (/*if*/true/*if*/) {
			//<body>
			// ContextUtils.addMessage("Выполняется отмена ППР по заданному расписанию, "
			// +"по окончанию генерации будет выведено сообщение о количестве отмененных ППР");
			//QueryUtils.deleteRecords("cilastplanneddate", "templateId=" + record.getId());
			Lib.PlannedActivityTemplateUtils.cancelPlannedActivities(record);
			//</body>
		}
		//endregion

	}

	//Создать ППР по Расписанию
	//button <sn>07f91d98-ae75-9849-14fa-f45ffda60e99</sn>
	public static void generatePlannedActivitiesByTemplate_1223_onEdit(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame("regularly", record.getAsString("generationtype")))) return;

		//region GeneratePlannedActivitiesByTemplate
		//rule <sn>4a20e39f-8e2b-6082-0a0a-4d962673de25</sn>
		if (/*if*/true/*if*/) {
			//<body>
			// ContextUtils.addMessage("Выполняется генерация ППР по заданному расписанию, "
			// +"по окончанию генерации будет выведено сообщение о количестве сгенерированных ППР");
			Lib.PlannedActivityTemplateUtils.createPlannedActivities(record);
			//</body>
		}
		//endregion

	}

}
