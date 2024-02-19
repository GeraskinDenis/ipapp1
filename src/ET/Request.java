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

//Стандартный запрос
public class Request extends EntityDecorator {

	private static final String TABLE_NAME = "request";

	public Request() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Request(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
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

	//Дата исполнения
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

	//Инициатор
	public Long get_Initiator() {
		return record.getAsLong("initiator");
	}

	//Заявка
	public Long get_Interaction() {
		return record.getAsLong("interaction");
	}

	//Требует согласования (template->is_need_approve)
	public Boolean get_Is_need_approve() {
		return record.getAsBoolean("is_need_approve");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Местоположение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Желаемая дата
	public Date get_Requested_date() {
		return record.getAsDateTime("requested_date");
	}

	//ID
	public Long get_Requestid() {
		return record.getAsLong("requestid");
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

	//Шаблон
	public Long get_Template() {
		return record.getAsLong("template");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//WorkFlow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Статус
	public Long get_Workflowstepid() {
		return record.getAsLong("workflowstepid");
	}

	//Название шага  WF
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
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

	//Дата исполнения
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

	//Инициатор
	public void set_Initiator(Long fieldValue) {
		record.set("initiator", fieldValue);
	}

	//Заявка
	public void set_Interaction(Long fieldValue) {
		record.set("interaction", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Местоположение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Желаемая дата
	public void set_Requested_date(Date fieldValue) {
		record.set("requested_date", fieldValue);
	}

	//ID
	public void set_Requestid(Long fieldValue) {
		record.set("requestid", fieldValue);
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

	//Шаблон
	public void set_Template(Long fieldValue) {
		record.set("template", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//WorkFlow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Статус
	public void set_Workflowstepid(Long fieldValue) {
		record.set("workflowstepid", fieldValue);
	}

	//Название шага  WF
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

}
