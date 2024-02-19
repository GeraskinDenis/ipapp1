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
DisplayName: ЖЦ Стандартного Запроса
Steps:
 - fulfillment (Выполнение)
 - completed (Выполнен)
 - closed (Закрыт)
 - cancelled (Отменен)
*/

public class WF_65_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Выполнить авт.
	//before <sn>itsm_request_complete</sn>
	//steps: fulfillment (Выполнение) -> completed (Выполнен)
	public static void itsm_request_complete_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Lib.ITSMRequestUtils.checkTasksCompleted(record) )) return;

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

	//Выполнить авт.
	//after <sn>itsm_request_complete</sn>
	//steps: fulfillment (Выполнение) -> completed (Выполнен)
	public static void itsm_request_complete_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Lib.ITSMRequestUtils.checkTasksCompleted(record) )) return;

		//region Try to procees Related Interaction
		//common rule <sn>tryProceesRelatedInteraction</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Инцидента
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO entityType = Lib.ITSMIncidentUtils.getWorkspaceTable("interaction");
			if (entityType!=null){
			    List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='"+record.getTableName()+"' and entityid2="+record.getKeyValue());
			    for (EntityDTO rel:relList){
			        EntityDTO interaction = new EntityDTO("INTERACTION",rel.getAsInteger("entityid1"));
				    if (interaction!=null){
					    log.info("Обращение найдено - "+interaction.getKeyValue());
					    interaction.processTransitions();
				    }
			    }
			}
			//</body>
		}
		//endregion

	}

	//Отменить
	//after <sn>itsm_request_cancel</sn>
	//steps: fulfillment (Выполнение) -> cancelled (Отменен)
	public static void itsm_request_cancel_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Cancel Object Active Tasks
		//common rule <sn>9ef3b2ed-751c-7905-f142-fb3276e537ca</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Инцидента
		if (/*if*/true/*if*/) {
			//<body>
			Lib.TaskUtils.cancelActiveTasks(record); 
			//</body>
		}
		//endregion

	}

	//Авт. возврат на доработку
	//before <sn>req_auto_inc_reopen</sn>
	//steps: completed (Выполнен) -> fulfillment (Выполнение)
	public static void req_auto_inc_reopen_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!((Lib.ITSMRequestUtils.getActiveTaskList(record).size()==1) && CommonUtils.isSame(record.getAsBoolean("enableautotransition"),true) )) return;

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

	//Авт. возврат на доработку
	//after <sn>req_auto_inc_reopen</sn>
	//steps: completed (Выполнен) -> fulfillment (Выполнение)
	public static void req_auto_inc_reopen_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!((Lib.ITSMRequestUtils.getActiveTaskList(record).size()==1) && CommonUtils.isSame(record.getAsBoolean("enableautotransition"),true) )) return;

		//region Reopen Parent Object Tasks
		//common rule <sn>53cb447e-5b25-bda1-edd7-6c1f5c03587d</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Инцидента
		if (/*if*/true/*if*/) {
			//<body>
			List<EntityDTO> taskList = QueryUtils.getRecordList("TASK","PARENT_ENTITYTYPE ='"+record.getTableName()+"' and parent_entityid ="+record.getId()+" and not coalesce(workflowstepname,'123') in ('cancelled')");
			for (EntityDTO task:taskList){
				task.set("enableautotransition",true);
				task.processTransitions();
			}
			//</body>
		}
		//endregion

	}

	//Вернуть на доработку
	//before <sn>req_back_to_work</sn>
	//steps: completed (Выполнен) -> fulfillment (Выполнение)
	public static void req_back_to_work_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check Request Active Tasks Exists
		//rule <sn>ff8124fb-a407-6807-158e-b3043f7208a3</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (Lib.ITSMRequestUtils.checkTasksCompleted(record))
			    throw new Exception("Откройте один из нарядов Запроса или создайте новый");
			//</body>
		}
		//endregion

	}

}
