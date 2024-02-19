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

//Выполнение планирования окна
public class Work_schedule_status extends EntityDecorator {

	private static final String TABLE_NAME = "work_schedule_status";

	public Work_schedule_status() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Work_schedule_status(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Объект
	public Long get_Entityid() {
		return record.getAsLong("entityid");
	}

	//Тип объекта
	public String get_Entitytype() {
		return record.getAsString("entitytype");
	}

	//Hostname
	public String get_Hostname() {
		return record.getAsString("hostname");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Необходимо остановить модель
	public Boolean get_Model_stop_flag() {
		return record.getAsBoolean("model_stop_flag");
	}

	//Прогресс
	public Long get_Progress() {
		return record.getAsLong("progress");
	}

	//Результат выполнения
	public String get_Result_message() {
		return record.getAsString("result_message");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Статус
	public String get_Status() {
		return record.getAsString("status");
	}

	//Номер потока Java
	public Long get_Threadid() {
		return record.getAsLong("threadid");
	}

	//Thread Status
	public String get_Threadstatus() {
		return record.getAsString("threadstatus");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//ID
	public Long get_Work_schedule_statusid() {
		return record.getAsLong("work_schedule_statusid");
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Объект
	public void set_Entityid(Long fieldValue) {
		record.set("entityid", fieldValue);
	}

	//Тип объекта
	public void set_Entitytype(String fieldValue) {
		record.set("entitytype", fieldValue);
	}

	//Hostname
	public void set_Hostname(String fieldValue) {
		record.set("hostname", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Необходимо остановить модель
	public void set_Model_stop_flag(Boolean fieldValue) {
		record.set("model_stop_flag", fieldValue);
	}

	//Прогресс
	public void set_Progress(Long fieldValue) {
		record.set("progress", fieldValue);
	}

	//Результат выполнения
	public void set_Result_message(String fieldValue) {
		record.set("result_message", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Статус
	public void set_Status(String fieldValue) {
		record.set("status", fieldValue);
	}

	//Номер потока Java
	public void set_Threadid(Long fieldValue) {
		record.set("threadid", fieldValue);
	}

	//Thread Status
	public void set_Threadstatus(String fieldValue) {
		record.set("threadstatus", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//ID
	public void set_Work_schedule_statusid(Long fieldValue) {
		record.set("work_schedule_statusid", fieldValue);
	}

}
