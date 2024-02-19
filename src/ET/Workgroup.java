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

//Рабочая группа
public class Workgroup extends EntityDecorator {

	private static final String TABLE_NAME = "workgroup";

	public Workgroup() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Workgroup(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Группа доступа
	public Long get_Access_group() {
		return record.getAsLong("access_group");
	}

	//Календарь
	public Long get_Calendar() {
		return record.getAsLong("calendar");
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

	//Email
	public String get_Email() {
		return record.getAsString("email");
	}

	//Является родительской группой
	public Boolean get_Is_parentorgunit() {
		return record.getAsBoolean("is_parentorgunit");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Расписание РГ
	public String get_Link_to_scheduler() {
		return record.getAsString("link_to_scheduler");
	}

	//Местонахождение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Руководитель группы
	public Long get_Manager() {
		return record.getAsLong("manager");
	}

	//Телефон
	public String get_Mobilephone() {
		return record.getAsString("mobilephone");
	}

	//Родительская группа
	public Long get_Parent_orgunit() {
		return record.getAsLong("parent_orgunit");
	}

	//Зона ответственности
	public Long get_Responsible_zone() {
		return record.getAsLong("responsible_zone");
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

	//ID
	public Long get_Workgroupid() {
		return record.getAsLong("workgroupid");
	}

	//Тип РГ
	public String get_Workgrouptype() {
		return record.getAsString("workgrouptype");
	}

	//Пространство группы
	public Long get_Workspace() {
		return record.getAsLong("workspace");
	}

	//Группа доступа
	public void set_Access_group(Long fieldValue) {
		record.set("access_group", fieldValue);
	}

	//Календарь
	public void set_Calendar(Long fieldValue) {
		record.set("calendar", fieldValue);
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

	//Email
	public void set_Email(String fieldValue) {
		record.set("email", fieldValue);
	}

	//Является родительской группой
	public void set_Is_parentorgunit(Boolean fieldValue) {
		record.set("is_parentorgunit", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Расписание РГ
	public void set_Link_to_scheduler(String fieldValue) {
		record.set("link_to_scheduler", fieldValue);
	}

	//Местонахождение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Руководитель группы
	public void set_Manager(Long fieldValue) {
		record.set("manager", fieldValue);
	}

	//Телефон
	public void set_Mobilephone(String fieldValue) {
		record.set("mobilephone", fieldValue);
	}

	//Родительская группа
	public void set_Parent_orgunit(Long fieldValue) {
		record.set("parent_orgunit", fieldValue);
	}

	//Зона ответственности
	public void set_Responsible_zone(Long fieldValue) {
		record.set("responsible_zone", fieldValue);
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

	//ID
	public void set_Workgroupid(Long fieldValue) {
		record.set("workgroupid", fieldValue);
	}

	//Тип РГ
	public void set_Workgrouptype(String fieldValue) {
		record.set("workgrouptype", fieldValue);
	}

	//Пространство группы
	public void set_Workspace(Long fieldValue) {
		record.set("workspace", fieldValue);
	}

}
