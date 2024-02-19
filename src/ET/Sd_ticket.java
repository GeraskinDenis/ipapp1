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

//Запрос на доработку
public class Sd_ticket extends EntityDecorator {

	private static final String TABLE_NAME = "sd_ticket";

	public Sd_ticket() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Sd_ticket(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Ответственный
	public Long get_Assignedto() {
		return record.getAsLong("assignedto");
	}

	//Вовзращен на доработку
	public Boolean get_Backtoworkflag() {
		return record.getAsBoolean("backtoworkflag");
	}

	//Код закрытия
	public String get_Closure_code() {
		return record.getAsString("closure_code");
	}

	//Время закрытия
	public Date get_Closure_time() {
		return record.getAsDateTime("closure_time");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Краткое описание
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Дата исполнения
	public Date get_Execution_date() {
		return record.getAsDateTime("execution_date");
	}

	//Влияние
	public Long get_Impact() {
		return record.getAsLong("impact");
	}

	//Инициатор
	public Long get_Initiator() {
		return record.getAsLong("initiator");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Скрывать специальные поля
	public Boolean get_Isinvisibleforsimpleuser() {
		return record.getAsBoolean("isinvisibleforsimpleuser");
	}

	//Приоритет
	public Long get_Priority() {
		return record.getAsLong("priority");
	}

	//Время регистрации
	public Date get_Registration_time() {
		return record.getAsDateTime("registration_time");
	}

	//Внутренний код
	public Long get_Sd_ticketid() {
		return record.getAsLong("sd_ticketid");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Срочность
	public Long get_Urgency() {
		return record.getAsLong("urgency");
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

	//Ответственный
	public void set_Assignedto(Long fieldValue) {
		record.set("assignedto", fieldValue);
	}

	//Вовзращен на доработку
	public void set_Backtoworkflag(Boolean fieldValue) {
		record.set("backtoworkflag", fieldValue);
	}

	//Код закрытия
	public void set_Closure_code(String fieldValue) {
		record.set("closure_code", fieldValue);
	}

	//Время закрытия
	public void set_Closure_time(Date fieldValue) {
		record.set("closure_time", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Краткое описание
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Дата исполнения
	public void set_Execution_date(Date fieldValue) {
		record.set("execution_date", fieldValue);
	}

	//Влияние
	public void set_Impact(Long fieldValue) {
		record.set("impact", fieldValue);
	}

	//Инициатор
	public void set_Initiator(Long fieldValue) {
		record.set("initiator", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Скрывать специальные поля
	public void set_Isinvisibleforsimpleuser(Boolean fieldValue) {
		record.set("isinvisibleforsimpleuser", fieldValue);
	}

	//Приоритет
	public void set_Priority(Long fieldValue) {
		record.set("priority", fieldValue);
	}

	//Время регистрации
	public void set_Registration_time(Date fieldValue) {
		record.set("registration_time", fieldValue);
	}

	//Внутренний код
	public void set_Sd_ticketid(Long fieldValue) {
		record.set("sd_ticketid", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Срочность
	public void set_Urgency(Long fieldValue) {
		record.set("urgency", fieldValue);
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
