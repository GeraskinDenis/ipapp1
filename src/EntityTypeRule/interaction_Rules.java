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

public class interaction_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void onOpen(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Calc default workplace for special services
		//rule <sn>ca495765-9415-bf81-0c1a-22580a92f1d8</sn>
		if (/*if*/!CommonUtils.isEmpty(record.get("service_citype")) && Arrays.asList("service_personal_device","service_access","service_complex").contains(record.getAsString("service_citype"))/*if*/) {
			//<body>
			EntityDTO workPlace = QueryUtils.getRecord("workplace","owner="+ContextUtils.getCurrentWorkspaceUserId()+" and workflowstepname in ('inproduction','inrepair')");
			if (!CommonUtils.isEmpty(workPlace))
			    record.set("workstation",workPlace.getId());
			//</body>
		}
		//endregion

	}

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Calc Interaction Location
		//rule <sn>34178937-d183-5ef6-b854-584d933a9024</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("location"))/*if*/) {
			//<body>
			String locationName = null;
			if (!CommonUtils.isEmpty(record.getAsString("workplace")))
			    locationName = record.getAsString("workplace");
			else if (!CommonUtils.isEmpty(record.getAsString("office")))
			    locationName = record.getAsString("office");
			//ContextUtils.addMessage("rule 1049 locationName");
			if (locationName!=null){
			    EntityDTO location = QueryUtils.getRecordByShortName("r_location", locationName);
			    if (!CommonUtils.isEmpty(location))
			        record.set("location",location.getId());
			}
			else{
			    if (!CommonUtils.isEmpty(record.get("workstation")))
			        record.set("location",record.get("workstation->workspace->r_locationid"));
			}
			
			    
			    
			
			
			//</body>
		}
		//endregion

		//region Calc Interaction Service and ServiceType
		//rule <sn>c40ce191-4f2b-3f3c-365b-ad1c863c4f79</sn>
		if (/*if*/true/*if*/) {
			//<body>
			String serviceType = null;
			if (!CommonUtils.isEmpty(record.getAsString("service_citype")))
			    serviceType = record.getAsString("service_citype");
			else{
			    if (!CommonUtils.isEmpty(record.getAsInteger("template"))){
			        EntityDTO intTempl = QueryUtils.getRecordById("interaction_template",record.getAsInteger("template"));
			        if (!CommonUtils.isEmpty(intTempl) && !CommonUtils.isEmpty(intTempl.getAsString("service_citype"))){
			            serviceType = intTempl.getAsString("service_citype");
			            record.set("service_citype",intTempl.getAsString("service_citype"));
			        }
			    }
			}
			log.info("rule 1045 serviceType ="+serviceType+", location ="+record.get("location"));
			if (CommonUtils.isEmpty(record.get("service")) && (serviceType!=null) && !CommonUtils.isEmpty(record.get("location"))){
			    EntityDTO service = QueryUtils.getRecord("service","citype_shortname='"+serviceType+"' and location="+record.get("location"));
			    if (!CommonUtils.isEmpty(service)){
			        record.set("service", service.getId());
			    }
			    else{
			        EntityDTO location = QueryUtils.getRecordById("r_location",record.getAsInteger("location"));
			        if (!CommonUtils.isEmpty(location) && !CommonUtils.isEmpty(location.get("parent_location"))){
			            service = QueryUtils.getRecord("service","citype_shortname='"+serviceType+"' and location="+location.get("parent_location"));
			            if (!CommonUtils.isEmpty(service)){
			                record.set("service", service.getId());
			            }
			        }
			    }
			}
			if (CommonUtils.isEmpty(record.get("service_citype")) && !CommonUtils.isEmpty(record.get("service"))){
			    EntityDTO service = QueryUtils.getRecordById("service",record.getAsInteger("service"));
			    if (!CommonUtils.isEmpty(service) && !CommonUtils.isEmpty(service.get("citype_shortname"))){
			        record.set("service_citype", service.getAsString("citype_shortname"));
			    }
			}
			    
			    
			
			
			//</body>
		}
		//endregion

		//region Calculate InteractionDeadline
		//rule <sn>d992f252-053a-37b2-57ce-4c341c808047</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!CommonUtils.isSame(record.get("PRIORITY"),oldrecord.get("PRIORITY")) || CommonUtils.isEmpty(record.get("deadline")) || !CommonUtils.isSame(record.get("template"), oldrecord.get("template")) || !CommonUtils.isSame(record.get("service"), oldrecord.get("service"))){
			    Integer priority = record.getAsInteger("priority");
			    Integer duration = 8;
			    Integer calendarId = null;
			    if (CommonUtils.isEmpty(priority))
			        priority = 2;
			    if (record.get("template")!=null){
			        EntityDTO templ = new EntityDTO("interaction_template",record.getAsInteger("template"));
			        if ((templ!=null) && (templ.get("duration")!=null))
			            duration = templ.getAsInteger("duration");
			        if (!CommonUtils.isEmpty(templ) && !CommonUtils.isEmpty(templ.get("coordinator")))
			            record.set("coordinator", templ.getAsInteger("coordinator"));
			    }
			    else{
			        EntityDTO deadlineByCategory = QueryUtils.getRecord("interaction_category_deadline","priority = "+priority+" and category='"+record.get("category")+"'");
			        if (!CommonUtils.isEmpty(deadlineByCategory) && !CommonUtils.isEmpty(deadlineByCategory.get("duration")))
			            duration = deadlineByCategory.getAsInteger("duration");
			    
			    }
			    if ((record.get("workgroup")!=null) && (record.get("workgroup->calendar")!=null))
			        calendarId = record.getAsInteger("workgroup->calendar");
			    if ((calendarId==null) && (record.get("service")!=null)){
			        EntityDTO service = new EntityDTO("service", record.getAsInteger("service"));
			        if ((service!=null) && (service.get("calendar")!=null))
			            calendarId = service.getAsInteger("calendar");
			    }
			    if (calendarId==null)
			        calendarId = Lib.TaskTemplateUtils.getWorkspaceCalendar(ContextUtils.getWorkspaceId()).getId();
			    log.info("rule 737, calendarid ="+calendarId+", duration ="+duration);
			    record.set("deadline", Lib.TaskTemplateUtils.addWorkHoursToDate(new Date(),duration, calendarId));
			}
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check ability to go next status
		//common rule <sn>checkGoToNextStatus</sn>
		//entities: sd_ticket, interaction
		if (/*if*/true/*if*/) {
			//<body>
			//log.info("попытка перевода в следующий статус");
			record.processTransitions();
			//</body>
		}
		//endregion

		//region Kafka Data Sync - After Create
		//common rule <sn>77c7c793-b3b0-4310-8191-77e08aeb3c83</sn>
		//entities: interaction_template, server, software, printer, monitor, citype, network_device, computer, av_device, service, subnetwork, interaction, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			KafkaUtils.recordCreateDataForKafka(record);
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Calc Interaction Location
		//rule <sn>34178937-d183-5ef6-b854-584d933a9024</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("location"))/*if*/) {
			//<body>
			String locationName = null;
			if (!CommonUtils.isEmpty(record.getAsString("workplace")))
			    locationName = record.getAsString("workplace");
			else if (!CommonUtils.isEmpty(record.getAsString("office")))
			    locationName = record.getAsString("office");
			//ContextUtils.addMessage("rule 1049 locationName");
			if (locationName!=null){
			    EntityDTO location = QueryUtils.getRecordByShortName("r_location", locationName);
			    if (!CommonUtils.isEmpty(location))
			        record.set("location",location.getId());
			}
			else{
			    if (!CommonUtils.isEmpty(record.get("workstation")))
			        record.set("location",record.get("workstation->workspace->r_locationid"));
			}
			
			    
			    
			
			
			//</body>
		}
		//endregion

		//region Calc Interaction Service and ServiceType
		//rule <sn>c40ce191-4f2b-3f3c-365b-ad1c863c4f79</sn>
		if (/*if*/true/*if*/) {
			//<body>
			String serviceType = null;
			if (!CommonUtils.isEmpty(record.getAsString("service_citype")))
			    serviceType = record.getAsString("service_citype");
			else{
			    if (!CommonUtils.isEmpty(record.getAsInteger("template"))){
			        EntityDTO intTempl = QueryUtils.getRecordById("interaction_template",record.getAsInteger("template"));
			        if (!CommonUtils.isEmpty(intTempl) && !CommonUtils.isEmpty(intTempl.getAsString("service_citype"))){
			            serviceType = intTempl.getAsString("service_citype");
			            record.set("service_citype",intTempl.getAsString("service_citype"));
			        }
			    }
			}
			log.info("rule 1045 serviceType ="+serviceType+", location ="+record.get("location"));
			if (CommonUtils.isEmpty(record.get("service")) && (serviceType!=null) && !CommonUtils.isEmpty(record.get("location"))){
			    EntityDTO service = QueryUtils.getRecord("service","citype_shortname='"+serviceType+"' and location="+record.get("location"));
			    if (!CommonUtils.isEmpty(service)){
			        record.set("service", service.getId());
			    }
			    else{
			        EntityDTO location = QueryUtils.getRecordById("r_location",record.getAsInteger("location"));
			        if (!CommonUtils.isEmpty(location) && !CommonUtils.isEmpty(location.get("parent_location"))){
			            service = QueryUtils.getRecord("service","citype_shortname='"+serviceType+"' and location="+location.get("parent_location"));
			            if (!CommonUtils.isEmpty(service)){
			                record.set("service", service.getId());
			            }
			        }
			    }
			}
			if (CommonUtils.isEmpty(record.get("service_citype")) && !CommonUtils.isEmpty(record.get("service"))){
			    EntityDTO service = QueryUtils.getRecordById("service",record.getAsInteger("service"));
			    if (!CommonUtils.isEmpty(service) && !CommonUtils.isEmpty(service.get("citype_shortname"))){
			        record.set("service_citype", service.getAsString("citype_shortname"));
			    }
			}
			    
			    
			
			
			//</body>
		}
		//endregion

		//region Calculate InteractionDeadline
		//rule <sn>d992f252-053a-37b2-57ce-4c341c808047</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!CommonUtils.isSame(record.get("PRIORITY"),oldrecord.get("PRIORITY")) || CommonUtils.isEmpty(record.get("deadline")) || !CommonUtils.isSame(record.get("template"), oldrecord.get("template")) || !CommonUtils.isSame(record.get("service"), oldrecord.get("service"))){
			    Integer priority = record.getAsInteger("priority");
			    Integer duration = 8;
			    Integer calendarId = null;
			    if (CommonUtils.isEmpty(priority))
			        priority = 2;
			    if (record.get("template")!=null){
			        EntityDTO templ = new EntityDTO("interaction_template",record.getAsInteger("template"));
			        if ((templ!=null) && (templ.get("duration")!=null))
			            duration = templ.getAsInteger("duration");
			        if (!CommonUtils.isEmpty(templ) && !CommonUtils.isEmpty(templ.get("coordinator")))
			            record.set("coordinator", templ.getAsInteger("coordinator"));
			    }
			    else{
			        EntityDTO deadlineByCategory = QueryUtils.getRecord("interaction_category_deadline","priority = "+priority+" and category='"+record.get("category")+"'");
			        if (!CommonUtils.isEmpty(deadlineByCategory) && !CommonUtils.isEmpty(deadlineByCategory.get("duration")))
			            duration = deadlineByCategory.getAsInteger("duration");
			    
			    }
			    if ((record.get("workgroup")!=null) && (record.get("workgroup->calendar")!=null))
			        calendarId = record.getAsInteger("workgroup->calendar");
			    if ((calendarId==null) && (record.get("service")!=null)){
			        EntityDTO service = new EntityDTO("service", record.getAsInteger("service"));
			        if ((service!=null) && (service.get("calendar")!=null))
			            calendarId = service.getAsInteger("calendar");
			    }
			    if (calendarId==null)
			        calendarId = Lib.TaskTemplateUtils.getWorkspaceCalendar(ContextUtils.getWorkspaceId()).getId();
			    log.info("rule 737, calendarid ="+calendarId+", duration ="+duration);
			    record.set("deadline", Lib.TaskTemplateUtils.addWorkHoursToDate(new Date(),duration, calendarId));
			}
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Add Interaction decline cause to comments
		//rule <sn>85b6cb1b-1e9d-8932-cb24-c0f96b53b6c4</sn>
		if (/*if*/!CommonUtils.isSame(record.getAsString("decline_cause"),oldrecord.getAsString("decline_cause")) && !CommonUtils.isEmpty(record.getAsString("decline_cause"))/*if*/) {
			//<body>
			Integer author = ContextUtils.getCurrentUserId();
			if (!CommonUtils.isEmpty(record.get("updatedbyid")))
			    author = record.getAsInteger("updatedbyid");
			DateTime createdTime = new DateTime();
			if (!CommonUtils.isEmpty(record.get("updatedtime")))
			    createdTime = record.getDateTime("updatedtime");
			Lib.CommentUtils.addComment(record, "Причина отклонения:\n"+record.getAsString("decline_cause"), author, createdTime);
			
			//</body>
		}
		//endregion

		//region SendTelegramMessageChangeWorkflow
		//rule <sn>537d9fa1-4d8a-d3f9-1ca5-cd694bd22068</sn>
		if (/*if*/true/*if*/) {
			//<body>
			String source = record.getAsString("source");
			Integer entityTypeId = record.getAsInteger("entityTypeId");
			
			if (!CommonUtils.isEmpty(source) && !CommonUtils.isEmpty(entityTypeId)) {
			
			    if (!"telegram".equalsIgnoreCase(source) && "interaction".equalsIgnoreCase(SystemUtils.getTableNameByEntityTypeId(entityTypeId))) {
			        ru.ip.server.integration.workspace.WorkspaceIntegrationService integrationService = ru.ip.server.integration.workspace.WorkspaceIntegrationServiceFactory.getIntegrationService("telegram");
			
			        if (integrationService != null) {
			            ru.ip.server.integration.workspace.TelegramWorkspaceIntegrationService telegramService = (ru.ip.server.integration.workspace.TelegramWorkspaceIntegrationService) integrationService;
			
			            String userName = "";
			
			            if (!record.isFieldEmpty("createdbyid")) {
			                userName = record.getAsString("createdbyid->fullname");
			            }
			
			            String startDelimiter = ru.ip.server.integration.workspace.TelegramWorkspaceIntegrationService.START_DELIMITER;
			            String endDelimiter = ru.ip.server.integration.workspace.TelegramWorkspaceIntegrationService.END_DELIMITER;
			            Integer entityId = record.getAsInteger("entityid");
			
			            if (!CommonUtils.isEmpty(entityId)) {
			
			                String interactionId = startDelimiter + entityId + endDelimiter;
			                String message = "Пользователь " + userName + " оставил комментарий под вашей заявкой " + interactionId
			                        + "\nСодержание:\n" + HtmlUtils.html2text(record.getAsString("comment"));
			
			
			                EntityDTO messageId = QueryUtils.select("message_id").from("interaction").where("interactionid =:_interaction_id").bind("_interaction_id", entityId).getRecord();
			
			                if (!CommonUtils.isEmpty(messageId) && !CommonUtils.isEmpty(messageId.getAsInteger("message_id"))) {
			                    EntityDTO chatId = QueryUtils.select("chat_id").from("telegramin").where("message_id =:_message_id")
			                            .bind("_message_id", messageId)
			                            .getRecord();
			
			                    if (!CommonUtils.isEmpty(chatId) && !CommonUtils.isEmpty(chatId.getAsInteger("chat_id"))) {
			                        telegramService.getTelegramBot().sendMessage(chatId.getAsInteger("message_id"), message, messageId.getAsInteger("message_id"));
			                    }
			                }
			            }
			        }
			    }
			}
			//</body>
		}
		//endregion

		//region Kafka Data Sync - After Update
		//common rule <sn>5877a964-65c9-538c-116a-983026feb9d0</sn>
		//entities: interaction_template, server, software, printer, monitor, citype, network_device, computer, av_device, service, subnetwork, interaction, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			KafkaUtils.recordUpdateDataForKafka(record, oldrecord);
			//</body>
		}
		//endregion

	}

	public static void afterDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Kafka Data Sync - After Delete
		//common rule <sn>70eba929-b998-ce6d-6a29-79fcbb5e6ed1</sn>
		//entities: interaction_template, server, software, printer, monitor, citype, network_device, computer, av_device, service, subnetwork, interaction, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			KafkaUtils.recordDeleteDataForKafka(oldrecord);
			//</body>
		}
		//endregion

	}

}
