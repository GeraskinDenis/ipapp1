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

//Организационная единица
public class R_orgunit extends EntityDecorator {

	private static final String TABLE_NAME = "r_orgunit";

	public R_orgunit() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public R_orgunit(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Код
	public String get_Code() {
		return record.getAsString("code");
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

	//Полное имя
	public String get_Fullname() {
		return record.getAsString("fullname");
	}

	//Полная структура подразделения
	public String get_Fullstructure() {
		return record.getAsString("fullstructure");
	}

	//Разница в часовых поясах
	public Long get_Hour_diff() {
		return record.getAsLong("hour_diff");
	}

	//Является родительским подразделением
	public Boolean get_Is_parentorgunit() {
		return record.getAsBoolean("is_parentorgunit");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Местонахождение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Руководитель
	public Long get_Manager() {
		return record.getAsLong("manager");
	}

	//Официальное наименование
	public String get_Officialname() {
		return record.getAsString("officialname");
	}

	//Тип
	public String get_Orgunittype() {
		return record.getAsString("orgunittype");
	}

	//Родительское подразделение
	public Long get_Parent_orgunit() {
		return record.getAsLong("parent_orgunit");
	}

	//ID
	public Long get_R_orgunitid() {
		return record.getAsLong("r_orgunitid");
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

	//Код
	public void set_Code(String fieldValue) {
		record.set("code", fieldValue);
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

	//Полное имя
	public void set_Fullname(String fieldValue) {
		record.set("fullname", fieldValue);
	}

	//Полная структура подразделения
	public void set_Fullstructure(String fieldValue) {
		record.set("fullstructure", fieldValue);
	}

	//Разница в часовых поясах
	public void set_Hour_diff(Long fieldValue) {
		record.set("hour_diff", fieldValue);
	}

	//Является родительским подразделением
	public void set_Is_parentorgunit(Boolean fieldValue) {
		record.set("is_parentorgunit", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Местонахождение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Руководитель
	public void set_Manager(Long fieldValue) {
		record.set("manager", fieldValue);
	}

	//Официальное наименование
	public void set_Officialname(String fieldValue) {
		record.set("officialname", fieldValue);
	}

	//Тип
	public void set_Orgunittype(String fieldValue) {
		record.set("orgunittype", fieldValue);
	}

	//Родительское подразделение
	public void set_Parent_orgunit(Long fieldValue) {
		record.set("parent_orgunit", fieldValue);
	}

	//ID
	public void set_R_orgunitid(Long fieldValue) {
		record.set("r_orgunitid", fieldValue);
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

}
