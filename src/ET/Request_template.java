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

//Шаблон запроса
public class Request_template extends EntityDecorator {

	private static final String TABLE_NAME = "request_template";

	public Request_template() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Request_template(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
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

	//Крайний срок
	public Date get_Deadline() {
		return record.getAsDateTime("deadline");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Длительность (часов)
	public Double get_Duration() {
		return record.getAsDouble("duration");
	}

	//Требует согласования
	public Boolean get_Is_need_approve() {
		return record.getAsBoolean("is_need_approve");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Местонахождение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Категория запроса
	public String get_Request_category() {
		return record.getAsString("request_category");
	}

	//ID
	public Long get_Request_templateid() {
		return record.getAsLong("request_templateid");
	}

	//Услуга
	public Long get_Service() {
		return record.getAsLong("service");
	}

	//Типовая услуга
	public String get_Service_citype() {
		return record.getAsString("service_citype");
	}

	//Уровень сервиса
	public Long get_Service_level() {
		return record.getAsLong("service_level");
	}

	//Short Name
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

	//ЖЦ обработки запроса
	public Long get_Workflow() {
		return record.getAsLong("workflow");
	}

	//WorkFlow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Шаг WorkFlow
	public Long get_Workflowstepid() {
		return record.getAsLong("workflowstepid");
	}

	//Статус
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Тип КЕ
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
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

	//Крайний срок
	public void set_Deadline(Date fieldValue) {
		record.set("deadline", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Длительность (часов)
	public void set_Duration(Double fieldValue) {
		record.set("duration", fieldValue);
	}

	//Требует согласования
	public void set_Is_need_approve(Boolean fieldValue) {
		record.set("is_need_approve", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Местонахождение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Категория запроса
	public void set_Request_category(String fieldValue) {
		record.set("request_category", fieldValue);
	}

	//ID
	public void set_Request_templateid(Long fieldValue) {
		record.set("request_templateid", fieldValue);
	}

	//Услуга
	public void set_Service(Long fieldValue) {
		record.set("service", fieldValue);
	}

	//Типовая услуга
	public void set_Service_citype(String fieldValue) {
		record.set("service_citype", fieldValue);
	}

	//Уровень сервиса
	public void set_Service_level(Long fieldValue) {
		record.set("service_level", fieldValue);
	}

	//Short Name
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

	//ЖЦ обработки запроса
	public void set_Workflow(Long fieldValue) {
		record.set("workflow", fieldValue);
	}

	//WorkFlow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Шаг WorkFlow
	public void set_Workflowstepid(Long fieldValue) {
		record.set("workflowstepid", fieldValue);
	}

	//Статус
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

}
