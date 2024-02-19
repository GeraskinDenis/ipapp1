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
DisplayName: Шаблон обращений
Fields:
 - approve_configuration_comment (Комментарий по настройке согласования) - TEXT
 - assignedto (Исполнитель) - INTEGER
 - available_citypes (Доступные типы КЕ) - INTEGER
 - category (Категория) - STRING
 - ci (КЕ) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - citype (Тип КЕ) - INTEGER
 - contact_phone (Телефон) - STRING
 - contact_title (Должность) - STRING
 - coordinator (Координатор) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - deadline_expired (Крайний срок просрочен) - BOOLEAN
 - description (Описание) - TEXT
 - displayname (Название) - STRING
 - duration (Длительность (часов)) - DOUBLE
 - enableautotransition (Разрешить автопереходы) - BOOLEAN
 - externalid (Внешний ID) - STRING
 - form (Форма заявки) - STRING
 - fullname (ФИО) - STRING
 - incident_date (Дата и время Инцидента) - DATETIME
 - interaction_templateid (ID) - INTEGER
 - internal_contact (Сотрудник/Внешний пользователь) - BOOLEAN
 - is_need_approve (Требует согласования) - BOOLEAN
 - is_need_manager_approve (Требует согласования руководителя) - BOOLEAN
 - is_userci_only (Показывать только КЕ пользователя) - BOOLEAN
 - isactive (Активно) - BOOLEAN
 - isneed_coordinator_apporve (Требует согласования координатора) - BOOLEAN
 - isneed_servicemanager_approve (Требует согласования владельца услуги) - BOOLEAN
 - model (Модель КЕ) - INTEGER
 - priority (Приоритет) - INTEGER
 - reopen_count (Количество переоткрытий) - INTEGER
 - required_fields (Обязательные поля) - STRING
 - service (Услуга) - INTEGER
 - service_citype (Типовая услуга) - STRING
 - shortname (Внутреннее имя) - STRING
 - updatealertlog (Требуется обновление ALERTLOG) - BOOLEAN
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflow (ЖЦ обработки запроса) - INTEGER
 - workflowid (WorkFlow) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Рабочая группа) - INTEGER
 - wrong_contact_info (Неточная информация) - BOOLEAN
*/

public class interaction_template_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

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

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

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
