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
DisplayName: ЖЦ Инцидента
Steps:
 - preparing (Новый)
 - inplanning (Направлен в группу)
 - inwork (В работе)
 - completed (Выполнен)
 - suspended (Приостановлен)
 - cancelled (Отменить)
 - closed (Закрыт)
*/

public class WF_31_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Отменить
	//after <sn>incident_cancel</sn>
	//steps: inplanning (Направлен в группу) -> cancelled (Отменить)
	public static void incident_cancel_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

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

	//Выполнить
	//before <sn>incident_complete</sn>
	//steps: inwork (В работе) -> completed (Выполнен)
	public static void incident_complete_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Lib.TaskUtils.checkTasksCompleted(record))) return;

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
	//after <sn>incident_complete</sn>
	//steps: inwork (В работе) -> completed (Выполнен)
	public static void incident_complete_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Lib.TaskUtils.checkTasksCompleted(record))) return;

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

	//В работу
	//before <sn>incident_towork_fromsuspend</sn>
	//steps: suspended (Приостановлен) -> inwork (В работе)
	public static void incident_towork_fromsuspend_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(IncidentUtils.CheckNoOneTasksEqualStatus(record, "suspended"))) return;

		//region Clean UnsSuspended Date in Task
		//rule <sn>CleanTaskUnsSuspendedDate</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("UNSUSPEND_DATE",null);
			//</body>
		}
		//endregion

	}

	//На планирование
	//before <sn>incident_toplanning</sn>
	//steps: preparing (Новый) -> inplanning (Направлен в группу)
	public static void incident_toplanning_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check Object Tasks Exist
		//rule <sn>8c60a7d8-a5b7-da00-7e24-2ee46d2bfb36</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!Lib.TaskUtils.checkActiveTasksExist(record))
			    throw new Exception("Не создано ни одного наряда");
			//</body>
		}
		//endregion

	}

	//Отменить
	//after <sn>incident_cancel2</sn>
	//steps: preparing (Новый) -> cancelled (Отменить)
	public static void incident_cancel2_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

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

	//На доработку
	//before <sn>incident_back_towork</sn>
	//steps: completed (Выполнен) -> inwork (В работе)
	public static void incident_back_towork_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check Open Tasks Exist
		//common rule <sn>f287874b-142b-b7cb-8011-42645fe6c557</sn>
		//entities: ЖЦ Проблемы, ЖЦ Инцидента
		if (/*if*/true/*if*/) {
			//<body>
			if (Lib.TaskUtils.checkTasksCompleted(record))
			    throw new Exception("Верните в работу один из нарядов или создайте новый");
			//</body>
		}
		//endregion

	}

	//На доработку (авто)
	//before <sn>inc_auto_reopen</sn>
	//steps: completed (Выполнен) -> inwork (В работе)
	public static void inc_auto_reopen_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(record.getAsBoolean("enableautotransition"),true))) return;

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

	//На доработку (авто)
	//after <sn>inc_auto_reopen</sn>
	//steps: completed (Выполнен) -> inwork (В работе)
	public static void inc_auto_reopen_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(record.getAsBoolean("enableautotransition"),true))) return;

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

}
