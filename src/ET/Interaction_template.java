package ET;

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
import ru.ip.server.database.sql.QueryType;
import ru.ip.server.entity.EntityDecorator;
import java.sql.Time;

//Шаблон обращений
public class Interaction_template extends EntityDecorator {

	private static final String TABLE_NAME = "interaction_template";

	public Interaction_template() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Interaction_template(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Комментарий по настройке согласования
	public String get_Approve_configuration_comment() {
		return record.getAsString("approve_configuration_comment");
	}

	//Исполнитель
	public Long get_Assignedto() {
		return record.getAsLong("assignedto");
	}

	//Доступные типы КЕ
	public Long get_Available_citypes() {
		return record.getAsLong("available_citypes");
	}

	//Категория
	public String get_Category() {
		return record.getAsString("category");
	}

	//КЕ
	public Long get_Ci() {
		return record.getAsLong("ci");
	}

	//Формат КЕ
	public Long get_Ci_format() {
		return record.getAsLong("ci_format");
	}

	//Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Телефон
	public String get_Contact_phone() {
		return record.getAsString("contact_phone");
	}

	//Должность
	public String get_Contact_title() {
		return record.getAsString("contact_title");
	}

	//Координатор
	public Long get_Coordinator() {
		return record.getAsLong("coordinator");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Крайний срок просрочен
	public Boolean get_Deadline_expired() {
		return record.getAsBoolean("deadline_expired");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Название
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Длительность (часов)
	public Double get_Duration() {
		return record.getAsDouble("duration");
	}

	//Разрешить автопереходы
	public Boolean get_Enableautotransition() {
		return record.getAsBoolean("enableautotransition");
	}

	//Внешний ID
	public String get_Externalid() {
		return record.getAsString("externalid");
	}

	//Форма заявки
	public String get_Form() {
		return record.getAsString("form");
	}

	//ФИО
	public String get_Fullname() {
		return record.getAsString("fullname");
	}

	//Дата и время Инцидента
	public Date get_Incident_date() {
		return record.getAsDateTime("incident_date");
	}

	//ID
	public Long get_Interaction_templateid() {
		return record.getAsLong("interaction_templateid");
	}

	//Сотрудник/Внешний пользователь
	public Boolean get_Internal_contact() {
		return record.getAsBoolean("internal_contact");
	}

	//Требует согласования
	public Boolean get_Is_need_approve() {
		return record.getAsBoolean("is_need_approve");
	}

	//Требует согласования руководителя
	public Boolean get_Is_need_manager_approve() {
		return record.getAsBoolean("is_need_manager_approve");
	}

	//Показывать только КЕ пользователя
	public Boolean get_Is_userci_only() {
		return record.getAsBoolean("is_userci_only");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Требует согласования координатора
	public Boolean get_Isneed_coordinator_apporve() {
		return record.getAsBoolean("isneed_coordinator_apporve");
	}

	//Требует согласования владельца услуги
	public Boolean get_Isneed_servicemanager_approve() {
		return record.getAsBoolean("isneed_servicemanager_approve");
	}

	//Модель КЕ
	public Long get_Model() {
		return record.getAsLong("model");
	}

	//Приоритет
	public Long get_Priority() {
		return record.getAsLong("priority");
	}

	//Количество переоткрытий
	public Long get_Reopen_count() {
		return record.getAsLong("reopen_count");
	}

	//Обязательные поля
	public String get_Required_fields() {
		return record.getAsString("required_fields");
	}

	//Услуга
	public Long get_Service() {
		return record.getAsLong("service");
	}

	//Типовая услуга
	public String get_Service_citype() {
		return record.getAsString("service_citype");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Требуется обновление ALERTLOG
	public Boolean get_Updatealertlog() {
		return record.getAsBoolean("updatealertlog");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//ЖЦ обработки запроса
	public Long get_Workflow() {
		return record.getAsLong("workflow");
	}

	//WorkFlow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Внутренний статус
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Рабочая группа
	public Long get_Workgroup() {
		return record.getAsLong("workgroup");
	}

	//Неточная информация
	public Boolean get_Wrong_contact_info() {
		return record.getAsBoolean("wrong_contact_info");
	}

	//Комментарий по настройке согласования
	public void set_Approve_configuration_comment(String fieldValue) {
		record.set("approve_configuration_comment", fieldValue);
	}

	//Исполнитель
	public void set_Assignedto(Long fieldValue) {
		record.set("assignedto", fieldValue);
	}

	//Доступные типы КЕ
	public void set_Available_citypes(Long fieldValue) {
		record.set("available_citypes", fieldValue);
	}

	//Категория
	public void set_Category(String fieldValue) {
		record.set("category", fieldValue);
	}

	//КЕ
	public void set_Ci(Long fieldValue) {
		record.set("ci", fieldValue);
	}

	//Формат КЕ
	public void set_Ci_format(Long fieldValue) {
		record.set("ci_format", fieldValue);
	}

	//Тип КЕ
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Телефон
	public void set_Contact_phone(String fieldValue) {
		record.set("contact_phone", fieldValue);
	}

	//Должность
	public void set_Contact_title(String fieldValue) {
		record.set("contact_title", fieldValue);
	}

	//Координатор
	public void set_Coordinator(Long fieldValue) {
		record.set("coordinator", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Крайний срок просрочен
	public void set_Deadline_expired(Boolean fieldValue) {
		record.set("deadline_expired", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Название
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Длительность (часов)
	public void set_Duration(Double fieldValue) {
		record.set("duration", fieldValue);
	}

	//Разрешить автопереходы
	public void set_Enableautotransition(Boolean fieldValue) {
		record.set("enableautotransition", fieldValue);
	}

	//Внешний ID
	public void set_Externalid(String fieldValue) {
		record.set("externalid", fieldValue);
	}

	//Форма заявки
	public void set_Form(String fieldValue) {
		record.set("form", fieldValue);
	}

	//ФИО
	public void set_Fullname(String fieldValue) {
		record.set("fullname", fieldValue);
	}

	//Дата и время Инцидента
	public void set_Incident_date(Date fieldValue) {
		record.set("incident_date", fieldValue);
	}

	//ID
	public void set_Interaction_templateid(Long fieldValue) {
		record.set("interaction_templateid", fieldValue);
	}

	//Сотрудник/Внешний пользователь
	public void set_Internal_contact(Boolean fieldValue) {
		record.set("internal_contact", fieldValue);
	}

	//Требует согласования
	public void set_Is_need_approve(Boolean fieldValue) {
		record.set("is_need_approve", fieldValue);
	}

	//Требует согласования руководителя
	public void set_Is_need_manager_approve(Boolean fieldValue) {
		record.set("is_need_manager_approve", fieldValue);
	}

	//Показывать только КЕ пользователя
	public void set_Is_userci_only(Boolean fieldValue) {
		record.set("is_userci_only", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Требует согласования координатора
	public void set_Isneed_coordinator_apporve(Boolean fieldValue) {
		record.set("isneed_coordinator_apporve", fieldValue);
	}

	//Требует согласования владельца услуги
	public void set_Isneed_servicemanager_approve(Boolean fieldValue) {
		record.set("isneed_servicemanager_approve", fieldValue);
	}

	//Модель КЕ
	public void set_Model(Long fieldValue) {
		record.set("model", fieldValue);
	}

	//Приоритет
	public void set_Priority(Long fieldValue) {
		record.set("priority", fieldValue);
	}

	//Количество переоткрытий
	public void set_Reopen_count(Long fieldValue) {
		record.set("reopen_count", fieldValue);
	}

	//Обязательные поля
	public void set_Required_fields(String fieldValue) {
		record.set("required_fields", fieldValue);
	}

	//Услуга
	public void set_Service(Long fieldValue) {
		record.set("service", fieldValue);
	}

	//Типовая услуга
	public void set_Service_citype(String fieldValue) {
		record.set("service_citype", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Требуется обновление ALERTLOG
	public void set_Updatealertlog(Boolean fieldValue) {
		record.set("updatealertlog", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//ЖЦ обработки запроса
	public void set_Workflow(Long fieldValue) {
		record.set("workflow", fieldValue);
	}

	//WorkFlow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Внутренний статус
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

	//Рабочая группа
	public void set_Workgroup(Long fieldValue) {
		record.set("workgroup", fieldValue);
	}

	//Неточная информация
	public void set_Wrong_contact_info(Boolean fieldValue) {
		record.set("wrong_contact_info", fieldValue);
	}

}
