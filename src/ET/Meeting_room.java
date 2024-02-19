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

//Переговорная
public class Meeting_room extends EntityDecorator {

	private static final String TABLE_NAME = "meeting_room";

	public Meeting_room() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Meeting_room(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Аудио-видео устройства
	public Long get_Av_devices() {
		return record.getAsLong("av_devices");
	}

	//Тип КЕ - число
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Тип
	public String get_Citype_shortname() {
		return record.getAsString("citype_shortname");
	}

	//Компьютеры
	public Long get_Computers() {
		return record.getAsLong("computers");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Название
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Максимальное количество сотрудников
	public Long get_Employee_count() {
		return record.getAsLong("employee_count");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Срок службы, лет (model->lifeduration)
	public Double get_Lifeduration() {
		return record.getAsDouble("lifeduration");
	}

	//Расположение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Код поиска
	public String get_Logicalname() {
		return record.getAsString("logicalname");
	}

	//ID
	public Long get_Meeting_roomid() {
		return record.getAsLong("meeting_roomid");
	}

	//Модель
	public String get_Model() {
		return record.getAsString("model");
	}

	//Мониторы
	public Long get_Monitors() {
		return record.getAsLong("monitors");
	}

	//Ответственный
	public Long get_Owner() {
		return record.getAsLong("owner");
	}

	//Комплектующие
	public Long get_Parts() {
		return record.getAsLong("parts");
	}

	//Услуга
	public Long get_Service() {
		return record.getAsLong("service");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//ПО для видео-конференц связи
	public Long get_Software() {
		return record.getAsLong("software");
	}

	//Дата ввода в эксплуатацию
	public Date get_Startdate() {
		return record.getAsDateTime("startdate");
	}

	//Уникальный номер
	public String get_Unnumber() {
		return record.getAsString("unnumber");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Work Flow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Статус
	public Long get_Workflowstepid() {
		return record.getAsLong("workflowstepid");
	}

	//Название шага
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Аудио-видео устройства
	public void set_Av_devices(Long fieldValue) {
		record.set("av_devices", fieldValue);
	}

	//Тип КЕ - число
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Тип
	public void set_Citype_shortname(String fieldValue) {
		record.set("citype_shortname", fieldValue);
	}

	//Компьютеры
	public void set_Computers(Long fieldValue) {
		record.set("computers", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Название
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Максимальное количество сотрудников
	public void set_Employee_count(Long fieldValue) {
		record.set("employee_count", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Расположение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Код поиска
	public void set_Logicalname(String fieldValue) {
		record.set("logicalname", fieldValue);
	}

	//ID
	public void set_Meeting_roomid(Long fieldValue) {
		record.set("meeting_roomid", fieldValue);
	}

	//Модель
	public void set_Model(String fieldValue) {
		record.set("model", fieldValue);
	}

	//Мониторы
	public void set_Monitors(Long fieldValue) {
		record.set("monitors", fieldValue);
	}

	//Ответственный
	public void set_Owner(Long fieldValue) {
		record.set("owner", fieldValue);
	}

	//Комплектующие
	public void set_Parts(Long fieldValue) {
		record.set("parts", fieldValue);
	}

	//Услуга
	public void set_Service(Long fieldValue) {
		record.set("service", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//ПО для видео-конференц связи
	public void set_Software(Long fieldValue) {
		record.set("software", fieldValue);
	}

	//Дата ввода в эксплуатацию
	public void set_Startdate(Date fieldValue) {
		record.set("startdate", fieldValue);
	}

	//Уникальный номер
	public void set_Unnumber(String fieldValue) {
		record.set("unnumber", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Work Flow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Статус
	public void set_Workflowstepid(Long fieldValue) {
		record.set("workflowstepid", fieldValue);
	}

	//Название шага
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

}
