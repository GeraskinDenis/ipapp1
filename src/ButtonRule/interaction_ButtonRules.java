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
DisplayName: Заявка
Fields:
 - category (Категория) - STRING
 - category_icon (Тип работ) - STRING, определение соединения: category->icon
 - chroma (Цвет печати) - STRING
 - ci (КЕ) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - citype (Тип КЕ) - INTEGER
 - connection_type (Способ подключения) - STRING
 - contact (Инициатор) - INTEGER
 - contact_email (Email) - STRING
 - contact_phone (Телефон) - STRING
 - contact_title (Должность) - STRING
 - coordinator (Координатор) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - deadline (Дата выполнения) - DATETIME
 - deadline_expired (Крайний срок просрочен) - BOOLEAN
 - decline_cause (Причина отклонения) - TEXT
 - description (Описание) - TEXT
 - displayname (Краткое описание) - STRING
 - duration (Длительность (часов)) - DOUBLE
 - enableautotransition (Разрешить автопереходы) - BOOLEAN
 - externalid (Внешний ID) - STRING
 - fact_finish (Фактическое окончание) - DATETIME
 - finish_date (Дата окончания) - DATETIME
 - floor (Этаж) - INTEGER
 - fullname (ФИО) - STRING
 - incident_date (Дата и время Инцидента) - DATETIME
 - interactionid (ID) - INTEGER
 - internal_contact (Сотрудник/Внешний пользователь) - BOOLEAN
 - is_need_approve (Требует согласования) - BOOLEAN, определение соединения: template->is_need_approve
 - is_two_sided (Двусторонняя печать) - BOOLEAN
 - isactive (Активно) - BOOLEAN
 - location (Местоположение) - INTEGER
 - main_interaction (Ведущее обращения) - INTEGER
 - meeting_format (Формат встречи) - STRING
 - message_id (ID сообщения Telegram) - INTEGER
 - model (Модель) - INTEGER
 - network_connection_type (Способ подключения) - STRING
 - new_workstation (Новое рабочее место) - INTEGER
 - office (Здание) - STRING
 - office_meeting_room (Переговорная комната) - INTEGER
 - oldci (КЕ - источник данных) - STRING
 - operation_systems (Операционная система на устройстве) - INTEGER
 - paper_format (Формат) - STRING
 - participants (Участники) - INTEGER
 - priority (Приоритет) - INTEGER
 - reopen_cause (Причина возврата на доработку) - TEXT
 - reopen_count (Количество переоткрытий) - INTEGER
 - repeation_type (Выполнить резервное копирование) - STRING
 - service (Услуга) - INTEGER
 - service_citype (Типовая услуга) - STRING
 - shortname (Внутреннее имя) - STRING
 - solution (Решение) - TEXT
 - source (Источник) - STRING
 - start_date (Дата начала) - DATETIME
 - storage_volume (Объем хранилища) - INTEGER
 - telegram_chatid (ID чата Telegram) - STRING
 - telegram_username (Логин Telegram) - STRING
 - template (Шаблон заявки) - INTEGER
 - treatment (Способ обращения) - STRING
 - updatealertlog (Требуется обновление ALERTLOG) - BOOLEAN
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - virtual_room (Виртуальная комната) - INTEGER
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Рабочая группа) - INTEGER
 - workplace (Рабочее пространство) - STRING
 - workplace_leve (Тип рабочего места) - STRING
 - workstation (Рабочее место) - INTEGER
 - wrong_contact_info (Неточная информация) - BOOLEAN
*/

public class interaction_ButtonRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Создать Инцидент
	//button <sn>2b566b2f-6de1-a8a6-ac64-5af068ee6354</sn>
	public static void create_incident_from_interaction_1298_onEdit(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!("incident".equals(record.getAsString("CATEGORY")) && Arrays.asList("waiting_inc_reopen").contains(record.getAsString("workflowstepname")))) return;

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

	//Объединить
	//button <sn>71a861d4-6670-f6ce-df8c-b2dd99b4ea74</sn>
	public static void relate_interactions_1154_onSearch_list(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(false)) return;

		//region relate interactions
		//rule <sn>62f92b33-2dd0-cc5a-8b43-67a36b81e68e</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO interRel = QueryUtils.getRecord("interaction_relation","(isactive=1) and (main_interaction=related_interaction)");
			if (interRel==null){
			    EntityDTO newInterRel = new EntityDTO("interaction_relation");
			    newInterRel.set("main_interaction", record.getKeyValue());
			    newInterRel.set("related_interaction", record.getKeyValue());
			    newInterRel.doInsert();
			}
			else{
			    record.set("main_interaction", interRel.getAsInteger("main_interaction"));
			    record.doUpdate();
			}
			//</body>
		}
		//endregion

	}

	//Согласовать
	//button <sn>f5176a61-2d5b-f339-3c7b-0b0cc909dff9</sn>
	public static void itsm_approve_1120_onSearch_list(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(ContextUtils.getCurrentDisplayViewName(),"interaction_waiting_my_approve"))) return;

		//region Mass Approve Action
		//common rule <sn>bb13183e-b14d-b0f2-5af8-376ec7e0b7fa</sn>
		//entities: request, interaction
		if (/*if*/true/*if*/) {
			//<body>
			//record.setWorkflowStepId(307); 
			//record.doUpdate();
			ContextUtils.addMessage("mass approve interactionid="+record.getId());
			record.processTransition("itsm_interaction_approve2");
			//</body>
		}
		//endregion

	}

	//Отклонить
	//button <sn>494556ca-61b8-e9fe-4ce9-9991dd72852c</sn>
	public static void itsm_decline_1121_onSearch_list(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(ContextUtils.getCurrentDisplayViewName(),"interaction_waiting_my_approve"))) return;

		//region Mass Decline Action
		//common rule <sn>bb123f59-cef0-57d0-90b7-9102808daa01</sn>
		//entities: request, interaction
		if (/*if*/true/*if*/) {
			//<body>
			//record.setWorkflowStepId(301); 
			//record.doUpdate();
			String declineCause = CastUtils.getStringValue(updateFields.get("p_decline_cause"));
			record.set("decline_cause",declineCause);
			record.processTransition("itsm_interaction_decline");
			//</body>
		}
		//endregion

	}

}
