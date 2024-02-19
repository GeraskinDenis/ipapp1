package WorkflowTranstionRule;

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
DisplayName: Жизненный цикл ППР
Steps:
 - new (Новая)
 - planned (Запланирована)
 - appointed (Назначена)
 - in_work (В работе)
 - completed (Выполнена)
 - closed (Закрыта)
 - cancelled (Отменена)
*/

public class WF_52_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Запланировать (auto)
	//before <sn>new_to_planned_auto</sn>
	//steps: new (Новая) -> planned (Запланирована)
	public static void new_to_planned_auto_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(record.getAsBooleanNullSub("enableAutoTransition", false))) return;

		//region Disable Auto Transition
		//common rule <sn>5422ca30-5200-0258-fbbe-22cebac93470</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Заявки, Жизненный цикл ППР, ЖЦ Инцидента, ЖЦ наряда
		if (/*if*/true/*if*/) {
			//<body>
			record.set("enableautotransition", false);
			//</body>
		}
		//endregion

		//region Зафиксировать крайний срок
		//rule <sn>11ae4295-fb89-d45c-0dc8-0efcadf53a1f</sn>
		if (/*if*/!record.isFieldEmpty("deadline")/*if*/) {
			//<body>
			record.setIfEmpty("deadline_fixed", record.getDateTime("deadline"));
			//</body>
		}
		//endregion

	}

	//Назначить (auto)
	//before <sn>pa_planned_to_appointed_auto</sn>
	//steps: planned (Запланирована) -> appointed (Назначена)
	public static void pa_planned_to_appointed_auto_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(record.getAsBooleanNullSub("enableAutoTransition", false))) return;

		//region Disable Auto Transition
		//common rule <sn>5422ca30-5200-0258-fbbe-22cebac93470</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Заявки, Жизненный цикл ППР, ЖЦ Инцидента, ЖЦ наряда
		if (/*if*/true/*if*/) {
			//<body>
			record.set("enableautotransition", false);
			//</body>
		}
		//endregion

	}

	//Выполнить
	//before <sn>in_work_to_completed</sn>
	//steps: in_work (В работе) -> completed (Выполнена)
	public static void in_work_to_completed_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Проверить, закрыты ли задачи ППР
		//rule <sn>be392c49-b758-c004-8c7f-d2c16c4085c7</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if ( !CommonUtils.isEmpty(record.getId()) ) {
			    
			    Integer notComplCount = QueryUtils.getCount("task", "parent_entitytype = 'plannedactivity'"
			        + " and parent_entityid = " + record.getId()
			        + " and workflowstepname not in ( 'completed', 'closed' )");
			    
			    if(notComplCount != null && notComplCount > 0) {
			        throw new Exception("Нельзя выполнить ППР, пока не выполнены дочерние задачи");
			    }
			}
			//</body>
		}
		//endregion

		//region Заполнить поле Факт. окончание и Факт. длительность
		//rule <sn>d28b5b62-8c0e-35a9-0aae-8e29a4698744</sn>
		if (/*if*/true/*if*/) {
			//<body>
			DateTime dt = new DateTime(CalendarUtils.getCurrentUserTimeZone());
			record.set("factEndTime", dt);
			
			if (!record.isFieldEmpty("factStartTime")) {
			    if(!record.isFieldEmpty("factEndTime") && !record.isFieldEmpty("calendarName")){
			        long durUnitCount = 0;
			        String durTimeUnits = "HOUR";
			        
			        Period p = CalendarUtils.getPeriod(record.getAsString("calendarName"), 
			            record.getDateTime("factStartTime"), 
			            record.getDateTime("factEndTime"));
			            
			        durUnitCount = Lib.CalendarCalcUtils.getWorkHoursBtwnDates(record.getAsString("calendarName"), 
			            record.getDateTime("factStartTime"), 
			            record.getDateTime("factEndTime"));
			            
			        if (p.getMinutes() > 0) {
			            durUnitCount *= 60;
			            durUnitCount += p.getMinutes();
			            durTimeUnits = "MINUTE";
			        }
			                
			        record.set("factDuration", durUnitCount);
			        record.set("factDurTimeUnits", durTimeUnits);
			    } 
			    // else
			    //     ContextUtils.addMessage("plannedEndTime is null", "ERROR");
			}
			// else
			//      ContextUtils.addMessage("plannedStartTime is null", "ERROR");
			//</body>
		}
		//endregion

	}

	//Прекратить
	//before <sn>pa_in_work_to_closed</sn>
	//steps: in_work (В работе) -> cancelled (Отменена)
	public static void pa_in_work_to_closed_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Установить Код закрытия в Прекращено
		//rule <sn>e7e72a56-df74-8250-af01-7926133e9500</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("closing_code", "Прекращено");
			//</body>
		}
		//endregion

		//region Заполнить фактическое окончание Работы при Прекращении
		//rule <sn>5bd79e2f-7b48-a481-8ad3-228857f3de21</sn>
		if (/*if*/true/*if*/) {
			//<body>
			DateTime dt = new DateTime();
			record.set("factendtime", dt);
			
			/*
			if (!record.isFieldEmpty("factstarttime") && !record.isFieldEmpty("calendarname")) {
			    long durUnitCount = 0;
			    String durTimeUnits = "HOUR";
			    
			    DateTime factStart = Lib.TaskTemplateUtils.getNextWorkHour(
			        record.getDateTime("factstarttime"),
			        record.getAsString("calendarname"));
			        
			    DateTime factEnd = Lib.TaskTemplateUtils.getNextWorkHour(
			        dt,
			        record.getAsString("calendarname"));
			    
			    Period p = CalendarUtils.getPeriod(record.getAsString("calendarName"), 
			        record.getDateTime("factstarttime"), 
			        record.getDateTime("factendtime"));
			    
			    durUnitCount = Lib.CalendarCalcUtils.getWorkHoursBtwnDates(record.getAsString("calendarName"), 
			        record.getDateTime("factstarttime"), 
			        record.getDateTime("factendtime"));
			        
			    if (p.getMinutes() > 0) {
			        
			        int minutes = p.getMinutes();
			        if (minutes == 59) {
			            durUnitCount ++;
			        }
			        else {
			            durUnitCount *= 60;
			            durUnitCount += minutes;
			            durTimeUnits = "MINUTE";
			        }
			    }
			            
			    record.set("factDuration", durUnitCount);
			    record.set("factDurTimeUnits", durTimeUnits);
			}*/
			//</body>
		}
		//endregion

	}

	//Прекратить
	//after <sn>pa_in_work_to_closed</sn>
	//steps: in_work (В работе) -> cancelled (Отменена)
	public static void pa_in_work_to_closed_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Отменить задачи отмененной работы
		//rule <sn>aef8a005-02f9-e007-cdfa-bdd2c19ab782</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!CommonUtils.isEmpty(record.getId())) {
			    // найти все дочерние задачи, которые можно отменить
			    List<EntityDTO> tasks = QueryUtils.getRecordList("task", 
			        "parent_entitytype='plannedactivity'"
			        + " and parent_entityid=" + record.getId()
			        + " and workflowstepname not in ('returned_back', 'completed', 'closed')");
			    
			    // если задачи найдены, послать команду на отмену
			    if (tasks != null) {
			        for(EntityDTO task : tasks) {
			            /*task.set("autoCancelEnabled", true);
			            task.processTransitions();*/
			            task.set("workflowstepname", "closed");
			            task.set("workflowstepid", 512);
			            task.doUpdate();
			        }
			    }
			}
			//</body>
		}
		//endregion

	}

	//Отклонить
	//before <sn>pa_appointed_to_planned</sn>
	//steps: appointed (Назначена) -> planned (Запланирована)
	public static void pa_appointed_to_planned_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Очистить поле Координатор
		//rule <sn>e1730dd6-6764-5ab0-0be8-ccd2a43fffaf</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("coordinator", null);
			//</body>
		}
		//endregion

	}

	//В работу (auto)
	//before <sn>planned_to_in_work_auto</sn>
	//steps: appointed (Назначена) -> in_work (В работе)
	public static void planned_to_in_work_auto_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(record.getAsBooleanNullSub("enableautotransition", false))) return;

		//region Disable Auto Transition
		//common rule <sn>5422ca30-5200-0258-fbbe-22cebac93470</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Заявки, Жизненный цикл ППР, ЖЦ Инцидента, ЖЦ наряда
		if (/*if*/true/*if*/) {
			//<body>
			record.set("enableautotransition", false);
			//</body>
		}
		//endregion

		//region Заполнить поле Фактическое начало
		//rule <sn>254caea4-6a1e-e15d-71e1-da5821ea879d</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.setIfEmpty("factstarttime", new DateTime());
			//</body>
		}
		//endregion

	}

	//В работу (auto)
	//after <sn>planned_to_in_work_auto</sn>
	//steps: appointed (Назначена) -> in_work (В работе)
	public static void planned_to_in_work_auto_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(record.getAsBooleanNullSub("enableautotransition", false))) return;

		//region Перевести в работу родительское Обращение
		//rule <sn>b9aef0b5-f446-1e3a-399d-b5747c424592</sn>
		if (/*if*/!record.isFieldEmpty("interaction")/*if*/) {
			//<body>
			EntityDTO inter = new EntityDTO("interaction", record.getAsInteger("interaction"));
			if (inter != null && !inter.isFieldEmpty("workflowstepname")) {
			    if (CommonUtils.isSame("appointed", record.getAsString("interaction->workflowstepname"))) {
			        inter.set("enableAutoTransition", true);
			        inter.processTransitions();
			    }
			}
			//</body>
		}
		//endregion

	}

}
