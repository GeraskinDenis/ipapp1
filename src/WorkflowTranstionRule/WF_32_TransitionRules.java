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
DisplayName: ЖЦ Заявки
Steps:
 - new (Новое)
 - registered (Зарегистрировано)
 - approving (Согласование)
 - inwork (В работе)
 - test_step_suspend (Ожидание)
 - completed (Подтверждение выполнения)
 - waiting_inc_reopen (Ожидает возврата на доработку)
 - closed (Закрыто)
 - declined (Не согласована)
*/

public class WF_32_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Создать Инцидент
	//before <sn>interaction_createIncident</sn>
	//steps: registered (Зарегистрировано) -> inwork (В работе)
	public static void interaction_createIncident_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Arrays.asList("incident","complaint").contains(record.getAsString("category")))) return;

		//region Set workgroup for complaint
		//rule <sn>6dc80606-65ef-ce0e-3fd1-f91bdb17836a</sn>
		if (/*if*/true/*if*/) {
			//<body>
			log.info("rule 1151 complaint wg define");
			if (Arrays.asList("complaint").contains(record.getAsString("category"))){
			    EntityDTO wg = QueryUtils.getRecordByShortName("workgroup", "complaint_processing_wg");
			    if (!CommonUtils.isEmpty(wg)){
			        log.info("rule 1151 wg found");
			        record.set("workgroup",wg.getId());
			    }
			}
			//</body>
		}
		//endregion

	}

	//Создать Инцидент
	//after <sn>interaction_createIncident</sn>
	//steps: registered (Зарегистрировано) -> inwork (В работе)
	public static void interaction_createIncident_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Arrays.asList("incident","complaint").contains(record.getAsString("category")))) return;

		//region Create Incident From Interaction
		//rule <sn>createIncidentFromInteraction_itsm</sn>
		if (/*if*/true/*if*/) {
			//<body>
			log.info("rule 483 start");
			EntityDTO incident =  Lib.ITSMIncidentUtils.createIncidentFromInteraction(record);
			if (incident!=null){
			   Lib.ITSMInteractionUtils.createRelationObjectFromInteraction(record, incident,"parent_child");
			}
			//</body>
		}
		//endregion

	}

	//Создать Запрос
	//after <sn>0c936480-0c1c-ad03-98b8-d6c73f123e8f</sn>
	//steps: registered (Зарегистрировано) -> inwork (В работе)
	public static void 0c936480_0c1c_ad03_98b8_d6c73f123e8f_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!("request".equals(record.get("CATEGORY")))) return;

		//region Create Request From Interaction
		//rule <sn>690898bf-7651-246e-39a5-f2b2eb6ac516</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO request =  Lib.InteractionRequestRelation.createRequestFromInteraction(record);
			if (!CommonUtils.isEmpty(request))
			   Lib.ITSMInteractionUtils.createRelationObjectFromInteraction(record, request,"parent_child");
			
			//</body>
		}
		//endregion

	}

	//Авт. эскалация
	//after <sn>inter_create_req_auto</sn>
	//steps: registered (Зарегистрировано) -> inwork (В работе)
	public static void inter_create_req_auto_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame("request",record.getAsString("category")) && !CommonUtils.isEmpty(record.get("service")) && !CommonUtils.isEmpty(record.get("deadline")) && !CommonUtils.isSame(record.getAsBoolean("template->is_need_approve"),true))) return;

		//region Create Request From Interaction auto
		//rule <sn>354a1dbd-e710-6e37-cf3c-a2cd5b63c13e</sn>
		if (/*if*/CommonUtils.isSame("request",record.getAsString("category")) /*if*/) {
			//<body>
			EntityDTO request =  Lib.InteractionRequestRelation.createRequestFromInteraction(record);
			if (!CommonUtils.isEmpty(request))
			   Lib.ITSMInteractionUtils.createRelationObjectFromInteraction(record, request,"parent_child");
			
			//</body>
		}
		//endregion

	}

	//Выполнить
	//before <sn>interaction_completed_by_disp</sn>
	//steps: registered (Зарегистрировано) -> completed (Подтверждение выполнения)
	public static void interaction_completed_by_disp_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Arrays.asList("informationRequest","incident").contains(record.getAsString("category")))) return;

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

	//Закрыть
	//before <sn>interaction_close</sn>
	//steps: registered (Зарегистрировано) -> closed (Закрыто)
	public static void interaction_close_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Arrays.asList("wrongAddress","statusRequest").contains(record.getAsString("category")))) return;

		//region Set Fact Finish to Object
		//rule <sn>509db1c9-24df-291e-8554-075ebd0ee397</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("fact_finish", new DateTime());
			//</body>
		}
		//endregion

		//region Close Related Interactions
		//rule <sn>776e9e7b-6f7c-7aab-3901-a83821865ead</sn>
		if (/*if*/true/*if*/) {
			//<body>
			List<EntityDTO> interList = QueryUtils.getRecordList("interaction","main_interaction="+record.getKeyValue());
			for (EntityDTO inter:interList){
			    inter.set("workflowstepname","closed");
			    inter.set("workflowstepid",180);
			    inter.doUpdate();
			}
			List<EntityDTO> relList = QueryUtils.getRecordList("interaction_relation","main_interaction="+record.getKeyValue());
			for (EntityDTO inter:interList)
			    inter.doDelete();
			//</body>
		}
		//endregion

	}

	//Закрыть
	//after <sn>interaction_close</sn>
	//steps: registered (Зарегистрировано) -> closed (Закрыто)
	public static void interaction_close_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Arrays.asList("wrongAddress","statusRequest").contains(record.getAsString("category")))) return;

		//region Send Interaction Resolution to Telegram
		//rule <sn>58e1696a-81f4-21a6-13c0-d0eb656f55ef</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if ((record.get("source")!=null) && ("telegram".equals(record.getAsString("source"))) && ((record.get("telegram_chatid")!=null)))
			    Lib.TelegramIntegration.sendInteractionResolution(record); 
			//</body>
		}
		//endregion

	}

	//Выполнить
	//before <sn>itsm_interaction_complete</sn>
	//steps: inwork (В работе) -> completed (Подтверждение выполнения)
	public static void itsm_interaction_complete_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!((Lib.ITSMIncidentUtils.checkAllIncidentsCompleted(record) && CommonUtils.isSame(record.getAsString("category"),"incident")) || (Lib.ITSMRequestUtils.checkAllRequestCompleted(record) && CommonUtils.isSame(record.getAsString("category"),"request")))) return;

		//region Set Fact Finish to Object
		//rule <sn>509db1c9-24df-291e-8554-075ebd0ee397</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("fact_finish", new DateTime());
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

	//Выполнить
	//after <sn>itsm_interaction_complete</sn>
	//steps: inwork (В работе) -> completed (Подтверждение выполнения)
	public static void itsm_interaction_complete_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!((Lib.ITSMIncidentUtils.checkAllIncidentsCompleted(record) && CommonUtils.isSame(record.getAsString("category"),"incident")) || (Lib.ITSMRequestUtils.checkAllRequestCompleted(record) && CommonUtils.isSame(record.getAsString("category"),"request")))) return;

		//region External Incident to Comleted
		//rule <sn>445d107a-892d-898c-584f-962540f795fe</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.get("externalid")!=null)
			    Lib.SberIntegration.toCompletedInc(record.getAsString("externalid"));
			//</body>
		}
		//endregion

	}

	//Подтвердить выполнение
	//after <sn>interaction_close_auto</sn>
	//steps: completed (Подтверждение выполнения) -> closed (Закрыто)
	public static void interaction_close_auto_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Close Incident related to Interaction
		//rule <sn>38aab19a-bc6a-6244-97d5-eaa67f384713</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("category"),"incident")/*if*/) {
			//<body>
			List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='incident' and entityid1="+record.getId());
			for (EntityDTO rel:relList){
				EntityDTO incident = QueryUtils.getRecordById("INCIDENT", rel.getAsInteger("ENTITYID2"));
				if (!CommonUtils.isEmpty(incident))
					incident.processTransitions();
			}
			//</body>
		}
		//endregion

		//region Close Request related to Interaction
		//rule <sn>b57b8f19-e109-78fb-53b8-a00e15edb6c4</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("category"),"request")/*if*/) {
			//<body>
			List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='request' and entityid1="+record.getId());
			for (EntityDTO rel:relList){
				EntityDTO request = QueryUtils.getRecordById("request", rel.getAsInteger("ENTITYID2"));
				if (!CommonUtils.isEmpty(request)){
					request.processTransitions();
				}
			}
			//</body>
		}
		//endregion

	}

	//Автомат. закрытие
	//before <sn>interaction_close_auto</sn>
	//steps: completed (Подтверждение выполнения) -> closed (Закрыто)
	public static void interaction_close_auto_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(record.getAsBoolean("enableautotransition"),true))) return;

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

	//Вернуть на доработку
	//before <sn>itsm_interaction_back_towork</sn>
	//steps: completed (Подтверждение выполнения) -> waiting_inc_reopen (Ожидает возврата на доработку)
	public static void itsm_interaction_back_towork_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Disable Auto Transition
		//common rule <sn>5422ca30-5200-0258-fbbe-22cebac93470</sn>
		//entities: ЖЦ Стандартного Запроса, ЖЦ Заявки, Жизненный цикл ППР, ЖЦ Инцидента, ЖЦ наряда
		if (/*if*/true/*if*/) {
			//<body>
			record.set("enableautotransition", false);
			//</body>
		}
		//endregion

		//region Add Interaction Reopen Count
		//rule <sn>ed535199-1ba1-59fa-c053-69d3d94d8e28</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.get("reopen_count") ==null)
			    record.set("reopen_count",1);
			else
			    record.set("reopen_count",record.getAsInteger("reopen_count")+1);
			//</body>
		}
		//endregion

	}

	//Авт. возврат на доработку
	//before <sn>int_auto_inc_reopen</sn>
	//steps: waiting_inc_reopen (Ожидает возврата на доработку) -> inwork (В работе)
	public static void int_auto_inc_reopen_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Lib.InteractionRequestRelation.checkOnlyOneRelatedObjectExist(record))) return;

		//region Reopen Incident related to Interaction
		//rule <sn>a56208ae-1e4e-3cbb-7405-9f95738bbb01</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("category"),"incident")/*if*/) {
			//<body>
			List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='incident' and entityid1="+record.getId());
			for (EntityDTO rel:relList){
				EntityDTO incident = QueryUtils.getRecordById("INCIDENT", rel.getAsInteger("ENTITYID2"));
				if (!CommonUtils.isEmpty(incident)){
					incident.set("enableautotransition",true);
					incident.processTransitions();
				}
			}
			//</body>
		}
		//endregion

		//region Reopen Request related to Interaction
		//rule <sn>94740315-bc78-75b2-ed26-c47a4e88efe0</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("category"),"request")/*if*/) {
			//<body>
			List<EntityDTO> relList = QueryUtils.getRecordList("OBJRELATION","entitytype1='interaction' and entitytype2='request' and entityid1="+record.getId());
			for (EntityDTO rel:relList){
				EntityDTO request = QueryUtils.getRecordById("request", rel.getAsInteger("ENTITYID2"));
				if (!CommonUtils.isEmpty(request)){
					request.set("enableautotransition",true);
					request.processTransitions();
				}
			}
			//</body>
		}
		//endregion

		//region Clear Reopen Cause
		//rule <sn>6ae49385-02f6-5aab-c9c6-cc129d7bcc55</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("reopen_cause",null);
			//</body>
		}
		//endregion

	}

	//На доработку
	//before <sn>int_to_inc_reopen</sn>
	//steps: waiting_inc_reopen (Ожидает возврата на доработку) -> inwork (В работе)
	public static void int_to_inc_reopen_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check Interaction has open Incidents
		//rule <sn>d62522c3-c830-960b-231a-0ec7c08befac</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("category"),"incident")/*if*/) {
			//<body>
			if (Lib.ITSMIncidentUtils.checkAllIncidentsCompleted(record))
			    throw new Exception("Верните в работу существующие Инциденты или создайте новый");
			//</body>
		}
		//endregion

		//region Check Interaction has open Requests
		//rule <sn>9df1341a-09a3-b7a3-9f12-daa738f184fc</sn>
		if (/*if*/CommonUtils.isSame(record.getAsString("category"),"request")/*if*/) {
			//<body>
			if (Lib.ITSMRequestUtils.checkAllRequestCompleted(record))
			    throw new Exception("Верните в работу существующие Запросы или создайте новый");
			//</body>
		}
		//endregion

		//region Clear Reopen Cause
		//rule <sn>6ae49385-02f6-5aab-c9c6-cc129d7bcc55</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("reopen_cause",null);
			//</body>
		}
		//endregion

	}

	//Согласовать
	//after <sn>itsm_interaction_approve2</sn>
	//steps: approving (Согласование) -> inwork (В работе)
	public static void itsm_interaction_approve2_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Create Request From Interaction auto
		//rule <sn>354a1dbd-e710-6e37-cf3c-a2cd5b63c13e</sn>
		if (/*if*/CommonUtils.isSame("request",record.getAsString("category")) /*if*/) {
			//<body>
			EntityDTO request =  Lib.InteractionRequestRelation.createRequestFromInteraction(record);
			if (!CommonUtils.isEmpty(request))
			   Lib.ITSMInteractionUtils.createRelationObjectFromInteraction(record, request,"parent_child");
			
			//</body>
		}
		//endregion

	}

	//Отправить повторно
	//before <sn>itsm_interaction_send_to_approving</sn>
	//steps: declined (Не согласована) -> approving (Согласование)
	public static void itsm_interaction_send_to_approving_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Decline Cause Empty
		//rule <sn>c31c6016-7364-198a-266f-8c2bbb2182f5</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("decline_cause",null)
			//</body>
		}
		//endregion

	}

}
