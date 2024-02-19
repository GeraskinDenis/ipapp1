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

//Заявка
public class Interaction extends EntityDecorator {

	private static final String TABLE_NAME = "interaction";

	public Interaction() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Interaction(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Категория
	public String get_Category() {
		return record.getAsString("category");
	}

	//Тип работ (category->icon)
	public String get_Category_icon() {
		return record.getAsString("category_icon");
	}

	//Цвет печати
	public String get_Chroma() {
		return record.getAsString("chroma");
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

	//Способ подключения
	public String get_Connection_type() {
		return record.getAsString("connection_type");
	}

	//Инициатор
	public Long get_Contact() {
		return record.getAsLong("contact");
	}

	//Email
	public String get_Contact_email() {
		return record.getAsString("contact_email");
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

	//Дата выполнения
	public Date get_Deadline() {
		return record.getAsDateTime("deadline");
	}

	//Крайний срок просрочен
	public Boolean get_Deadline_expired() {
		return record.getAsBoolean("deadline_expired");
	}

	//Причина отклонения
	public String get_Decline_cause() {
		return record.getAsString("decline_cause");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Краткое описание
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

	//Фактическое окончание
	public Date get_Fact_finish() {
		return record.getAsDateTime("fact_finish");
	}

	//Дата окончания
	public Date get_Finish_date() {
		return record.getAsDateTime("finish_date");
	}

	//Этаж
	public Long get_Floor() {
		return record.getAsLong("floor");
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
	public Long get_Interactionid() {
		return record.getAsLong("interactionid");
	}

	//Сотрудник/Внешний пользователь
	public Boolean get_Internal_contact() {
		return record.getAsBoolean("internal_contact");
	}

	//Требует согласования (template->is_need_approve)
	public Boolean get_Is_need_approve() {
		return record.getAsBoolean("is_need_approve");
	}

	//Двусторонняя печать
	public Boolean get_Is_two_sided() {
		return record.getAsBoolean("is_two_sided");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Местоположение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Ведущее обращения
	public Long get_Main_interaction() {
		return record.getAsLong("main_interaction");
	}

	//Формат встречи
	public String get_Meeting_format() {
		return record.getAsString("meeting_format");
	}

	//ID сообщения Telegram
	public Long get_Message_id() {
		return record.getAsLong("message_id");
	}

	//Модель
	public Long get_Model() {
		return record.getAsLong("model");
	}

	//Способ подключения
	public String get_Network_connection_type() {
		return record.getAsString("network_connection_type");
	}

	//Новое рабочее место
	public Long get_New_workstation() {
		return record.getAsLong("new_workstation");
	}

	//Здание
	public String get_Office() {
		return record.getAsString("office");
	}

	//Переговорная комната
	public Long get_Office_meeting_room() {
		return record.getAsLong("office_meeting_room");
	}

	//КЕ - источник данных
	public String get_Oldci() {
		return record.getAsString("oldci");
	}

	//Операционная система на устройстве
	public Long get_Operation_systems() {
		return record.getAsLong("operation_systems");
	}

	//Формат
	public String get_Paper_format() {
		return record.getAsString("paper_format");
	}

	//Участники
	public Long get_Participants() {
		return record.getAsLong("participants");
	}

	//Приоритет
	public Long get_Priority() {
		return record.getAsLong("priority");
	}

	//Причина возврата на доработку
	public String get_Reopen_cause() {
		return record.getAsString("reopen_cause");
	}

	//Количество переоткрытий
	public Long get_Reopen_count() {
		return record.getAsLong("reopen_count");
	}

	//Выполнить резервное копирование
	public String get_Repeation_type() {
		return record.getAsString("repeation_type");
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

	//Решение
	public String get_Solution() {
		return record.getAsString("solution");
	}

	//Источник
	public String get_Source() {
		return record.getAsString("source");
	}

	//Дата начала
	public Date get_Start_date() {
		return record.getAsDateTime("start_date");
	}

	//Объем хранилища
	public Long get_Storage_volume() {
		return record.getAsLong("storage_volume");
	}

	//ID чата Telegram
	public String get_Telegram_chatid() {
		return record.getAsString("telegram_chatid");
	}

	//Логин Telegram
	public String get_Telegram_username() {
		return record.getAsString("telegram_username");
	}

	//Шаблон заявки
	public Long get_Template() {
		return record.getAsLong("template");
	}

	//Способ обращения
	public String get_Treatment() {
		return record.getAsString("treatment");
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

	//Виртуальная комната
	public Long get_Virtual_room() {
		return record.getAsLong("virtual_room");
	}

	//WorkFlow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Статус
	public Long get_Workflowstepid() {
		return record.getAsLong("workflowstepid");
	}

	//Внутренний статус
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Рабочая группа
	public Long get_Workgroup() {
		return record.getAsLong("workgroup");
	}

	//Рабочее пространство
	public String get_Workplace() {
		return record.getAsString("workplace");
	}

	//Тип рабочего места
	public String get_Workplace_leve() {
		return record.getAsString("workplace_leve");
	}

	//Рабочее место
	public Long get_Workstation() {
		return record.getAsLong("workstation");
	}

	//Неточная информация
	public Boolean get_Wrong_contact_info() {
		return record.getAsBoolean("wrong_contact_info");
	}

	//Категория
	public void set_Category(String fieldValue) {
		record.set("category", fieldValue);
	}

	//Цвет печати
	public void set_Chroma(String fieldValue) {
		record.set("chroma", fieldValue);
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

	//Способ подключения
	public void set_Connection_type(String fieldValue) {
		record.set("connection_type", fieldValue);
	}

	//Инициатор
	public void set_Contact(Long fieldValue) {
		record.set("contact", fieldValue);
	}

	//Email
	public void set_Contact_email(String fieldValue) {
		record.set("contact_email", fieldValue);
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

	//Дата выполнения
	public void set_Deadline(Date fieldValue) {
		record.set("deadline", fieldValue);
	}

	//Крайний срок просрочен
	public void set_Deadline_expired(Boolean fieldValue) {
		record.set("deadline_expired", fieldValue);
	}

	//Причина отклонения
	public void set_Decline_cause(String fieldValue) {
		record.set("decline_cause", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Краткое описание
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

	//Фактическое окончание
	public void set_Fact_finish(Date fieldValue) {
		record.set("fact_finish", fieldValue);
	}

	//Дата окончания
	public void set_Finish_date(Date fieldValue) {
		record.set("finish_date", fieldValue);
	}

	//Этаж
	public void set_Floor(Long fieldValue) {
		record.set("floor", fieldValue);
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
	public void set_Interactionid(Long fieldValue) {
		record.set("interactionid", fieldValue);
	}

	//Сотрудник/Внешний пользователь
	public void set_Internal_contact(Boolean fieldValue) {
		record.set("internal_contact", fieldValue);
	}

	//Двусторонняя печать
	public void set_Is_two_sided(Boolean fieldValue) {
		record.set("is_two_sided", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Местоположение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Ведущее обращения
	public void set_Main_interaction(Long fieldValue) {
		record.set("main_interaction", fieldValue);
	}

	//Формат встречи
	public void set_Meeting_format(String fieldValue) {
		record.set("meeting_format", fieldValue);
	}

	//ID сообщения Telegram
	public void set_Message_id(Long fieldValue) {
		record.set("message_id", fieldValue);
	}

	//Модель
	public void set_Model(Long fieldValue) {
		record.set("model", fieldValue);
	}

	//Способ подключения
	public void set_Network_connection_type(String fieldValue) {
		record.set("network_connection_type", fieldValue);
	}

	//Новое рабочее место
	public void set_New_workstation(Long fieldValue) {
		record.set("new_workstation", fieldValue);
	}

	//Здание
	public void set_Office(String fieldValue) {
		record.set("office", fieldValue);
	}

	//Переговорная комната
	public void set_Office_meeting_room(Long fieldValue) {
		record.set("office_meeting_room", fieldValue);
	}

	//КЕ - источник данных
	public void set_Oldci(String fieldValue) {
		record.set("oldci", fieldValue);
	}

	//Операционная система на устройстве
	public void set_Operation_systems(Long fieldValue) {
		record.set("operation_systems", fieldValue);
	}

	//Формат
	public void set_Paper_format(String fieldValue) {
		record.set("paper_format", fieldValue);
	}

	//Участники
	public void set_Participants(Long fieldValue) {
		record.set("participants", fieldValue);
	}

	//Приоритет
	public void set_Priority(Long fieldValue) {
		record.set("priority", fieldValue);
	}

	//Причина возврата на доработку
	public void set_Reopen_cause(String fieldValue) {
		record.set("reopen_cause", fieldValue);
	}

	//Количество переоткрытий
	public void set_Reopen_count(Long fieldValue) {
		record.set("reopen_count", fieldValue);
	}

	//Выполнить резервное копирование
	public void set_Repeation_type(String fieldValue) {
		record.set("repeation_type", fieldValue);
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

	//Решение
	public void set_Solution(String fieldValue) {
		record.set("solution", fieldValue);
	}

	//Источник
	public void set_Source(String fieldValue) {
		record.set("source", fieldValue);
	}

	//Дата начала
	public void set_Start_date(Date fieldValue) {
		record.set("start_date", fieldValue);
	}

	//Объем хранилища
	public void set_Storage_volume(Long fieldValue) {
		record.set("storage_volume", fieldValue);
	}

	//ID чата Telegram
	public void set_Telegram_chatid(String fieldValue) {
		record.set("telegram_chatid", fieldValue);
	}

	//Логин Telegram
	public void set_Telegram_username(String fieldValue) {
		record.set("telegram_username", fieldValue);
	}

	//Шаблон заявки
	public void set_Template(Long fieldValue) {
		record.set("template", fieldValue);
	}

	//Способ обращения
	public void set_Treatment(String fieldValue) {
		record.set("treatment", fieldValue);
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

	//Виртуальная комната
	public void set_Virtual_room(Long fieldValue) {
		record.set("virtual_room", fieldValue);
	}

	//WorkFlow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Статус
	public void set_Workflowstepid(Long fieldValue) {
		record.set("workflowstepid", fieldValue);
	}

	//Внутренний статус
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

	//Рабочая группа
	public void set_Workgroup(Long fieldValue) {
		record.set("workgroup", fieldValue);
	}

	//Рабочее пространство
	public void set_Workplace(String fieldValue) {
		record.set("workplace", fieldValue);
	}

	//Тип рабочего места
	public void set_Workplace_leve(String fieldValue) {
		record.set("workplace_leve", fieldValue);
	}

	//Рабочее место
	public void set_Workstation(Long fieldValue) {
		record.set("workstation", fieldValue);
	}

	//Неточная информация
	public void set_Wrong_contact_info(Boolean fieldValue) {
		record.set("wrong_contact_info", fieldValue);
	}

}
