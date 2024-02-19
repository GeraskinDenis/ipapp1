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

//Сотрудник
public class Workspace_user extends EntityDecorator {

	private static final String TABLE_NAME = "workspace_user";

	public Workspace_user() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Workspace_user(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Способ Аутентификации
	public String get_Authtype() {
		return record.getAsString("authtype");
	}

	//Аватарка
	public Long get_Avatar_file() {
		return record.getAsLong("avatar_file");
	}

	//Дата рождения
	public Date get_Birthdate() {
		return record.getAsDateTime("birthdate");
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

	//Статус почты
	public String get_Emailstatus() {
		return record.getAsString("emailstatus");
	}

	//Дата увольнения
	public Date get_Fired_date() {
		return record.getAsDateTime("fired_date");
	}

	//Полное имя
	public String get_Fullname() {
		return record.getAsString("fullname");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Логин
	public String get_Loginname() {
		return record.getAsString("loginname");
	}

	//Пароль
	public String get_Loginpass() {
		return record.getAsString("loginpass");
	}

	//Подразделение
	public Long get_Orgunit() {
		return record.getAsLong("orgunit");
	}

	//Телефон
	public String get_Phone() {
		return record.getAsString("phone");
	}

	//Роль
	public Long get_Roleid() {
		return record.getAsLong("roleid");
	}

	//Табельный номер
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Дата приема на работу
	public Date get_Startdate() {
		return record.getAsDateTime("startdate");
	}

	//Статус
	public String get_Status() {
		return record.getAsString("status");
	}

	//Временная зона
	public String get_Timezone() {
		return record.getAsString("timezone");
	}

	//Должность
	public String get_Title() {
		return record.getAsString("title");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Пользователь
	public Long get_Userid() {
		return record.getAsLong("userid");
	}

	//ID
	public Long get_Workspace_userid() {
		return record.getAsLong("workspace_userid");
	}

	//Способ Аутентификации
	public void set_Authtype(String fieldValue) {
		record.set("authtype", fieldValue);
	}

	//Аватарка
	public void set_Avatar_file(Long fieldValue) {
		record.set("avatar_file", fieldValue);
	}

	//Дата рождения
	public void set_Birthdate(Date fieldValue) {
		record.set("birthdate", fieldValue);
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

	//Статус почты
	public void set_Emailstatus(String fieldValue) {
		record.set("emailstatus", fieldValue);
	}

	//Дата увольнения
	public void set_Fired_date(Date fieldValue) {
		record.set("fired_date", fieldValue);
	}

	//Полное имя
	public void set_Fullname(String fieldValue) {
		record.set("fullname", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Логин
	public void set_Loginname(String fieldValue) {
		record.set("loginname", fieldValue);
	}

	//Пароль
	public void set_Loginpass(String fieldValue) {
		record.set("loginpass", fieldValue);
	}

	//Подразделение
	public void set_Orgunit(Long fieldValue) {
		record.set("orgunit", fieldValue);
	}

	//Телефон
	public void set_Phone(String fieldValue) {
		record.set("phone", fieldValue);
	}

	//Роль
	public void set_Roleid(Long fieldValue) {
		record.set("roleid", fieldValue);
	}

	//Табельный номер
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Дата приема на работу
	public void set_Startdate(Date fieldValue) {
		record.set("startdate", fieldValue);
	}

	//Статус
	public void set_Status(String fieldValue) {
		record.set("status", fieldValue);
	}

	//Временная зона
	public void set_Timezone(String fieldValue) {
		record.set("timezone", fieldValue);
	}

	//Должность
	public void set_Title(String fieldValue) {
		record.set("title", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Пользователь
	public void set_Userid(Long fieldValue) {
		record.set("userid", fieldValue);
	}

	//ID
	public void set_Workspace_userid(Long fieldValue) {
		record.set("workspace_userid", fieldValue);
	}

}
