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
DisplayName: ЖЦ наряда
Steps:
 - task_suspended (Ожидание)
 - new (Новый)
 - planned (Запланирован)
 - inwork (В работе)
 - completed (Выполнен)
 - cancelled (Отменен)
*/

public class WF_64_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Запланировать (auto) 
	//before <sn>task_common_plan_autor</sn>
	//steps: new (Новый) -> planned (Запланирован)
	public static void task_common_plan_autor_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(record.getAsBooleanNullSub("enableautotransition", false))) return;

		//region Disable AutoTransiton 
		//common rule <sn>7bf017fd-9c7e-0fa3-8b86-808ca08c560e</sn>
		//entities: ЖЦ Заявки, ЖЦ наряда
		if (/*if*/true/*if*/) {
			//<body>
			record.set("enableautotransition",false);
			//</body>
		}
		//endregion

	}

	//В работу
	//after <sn>itsm_task_towork</sn>
	//steps: planned (Запланирован) -> inwork (В работе)
	public static void itsm_task_towork_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

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

	//Перепланировать
	//before <sn>itsm_task_replan</sn>
	//steps: inwork (В работе) -> planned (Запланирован)
	public static void itsm_task_replan_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Task AlertViewed To False
		//rule <sn>34b11066-66e5-8c6e-1a13-de6fb84b6c55</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("isalertviewed",false);
			//</body>
		}
		//endregion

	}

	//Выполнить
	//after <sn>itsm_task_complete</sn>
	//steps: inwork (В работе) -> completed (Выполнен)
	public static void itsm_task_complete_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Process Parent Object And Enable Autotransition
		//rule <sn>417ddecc-e9d7-9117-282d-e8adfe6f09b7</sn>
		if (/*if*/(record.get("parent_entitytype")!=null) && (record.get("parent_entityid")!=null)/*if*/) {
			//<body>
			EntityDTO parentObj = new EntityDTO(record.getAsString("parent_entitytype"),record.getAsInteger("parent_entityid"));
			if (parentObj!=null){
			    if (parentObj.hasField("solution") && !record.isFieldEmpty("solution")){
			        String solution = null;
			        if (!parentObj.isFieldEmpty("solution"))
			            solution =  parentObj.getAsString("solution")+"\n";
			        record.set("solution",solution+record.getAsString("solution"));
			    }
			    parentObj.set("enableautotransition", true);
			    parentObj.processTransitions();
			}    
			//</body>
		}
		//endregion

		//region Plan next Tasks
		//rule <sn>22f1d8a7-8510-6f29-973a-d84ba33ff775</sn>
		if (/*if*/true/*if*/) {
			//<body>
			List<EntityDTO> childTaskList = QueryUtils.getRecordList("task_common","predecessor ="+record.getKeyValue());
			for (EntityDTO childTask:childTaskList){
			    if (childTask.get("planned_start")!=null){
			        if (childTask.getAsDateTime("planned_start").before(new Date()))
			            childTask.set("planned_start", new Date());
			    }
			    else{
			        childTask.set("planned_start", new Date());
			    }
			    childTask.set("enableautotransition",true);
			    childTask.processTransitions();
			}    
			//</body>
		}
		//endregion

	}

	//Вернуть в работу
	//before <sn>itsm_task_backtowork</sn>
	//steps: completed (Выполнен) -> inwork (В работе)
	public static void itsm_task_backtowork_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Clear solution
		//rule <sn>123b3c83-f5af-56b5-e0f0-50d73974953c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("solution", null);
			//</body>
		}
		//endregion

	}

	//Вернуть в работу авт.
	//before <sn>task_back_to_work_auto</sn>
	//steps: completed (Выполнен) -> inwork (В работе)
	public static void task_back_to_work_auto_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(record.getAsBoolean("enableautotransition"),true))) return;

		//region Clear solution
		//rule <sn>123b3c83-f5af-56b5-e0f0-50d73974953c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("solution", null);
			//</body>
		}
		//endregion

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

	//Запланировать
	//after <sn>itsm_task_replan2</sn>
	//steps: cancelled (Отменен) -> planned (Запланирован)
	public static void itsm_task_replan2_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Task AlertViewed To False
		//rule <sn>34b11066-66e5-8c6e-1a13-de6fb84b6c55</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("isalertviewed",false);
			//</body>
		}
		//endregion

	}

}
