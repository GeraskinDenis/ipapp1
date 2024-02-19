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

//Связь объектов SD
public class Sd_objrelation extends EntityDecorator {

	private static final String TABLE_NAME = "sd_objrelation";

	public Sd_objrelation() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Sd_objrelation(EntityDTO record) {
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

	//Код объекта 1
	public Long get_Entityid1() {
		return record.getAsLong("entityid1");
	}

	//Код объекта 2
	public Long get_Entityid2() {
		return record.getAsLong("entityid2");
	}

	//Таблица 1
	public Long get_Entitytype1() {
		return record.getAsLong("entitytype1");
	}

	//Таблица 2
	public Long get_Entitytype2() {
		return record.getAsLong("entitytype2");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Тип связи
	public String get_Relation_type() {
		return record.getAsString("relation_type");
	}

	//Внутренний код
	public Long get_Sd_objrelationid() {
		return record.getAsLong("sd_objrelationid");
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

	//Код объекта 1
	public void set_Entityid1(Long fieldValue) {
		record.set("entityid1", fieldValue);
	}

	//Код объекта 2
	public void set_Entityid2(Long fieldValue) {
		record.set("entityid2", fieldValue);
	}

	//Таблица 1
	public void set_Entitytype1(Long fieldValue) {
		record.set("entitytype1", fieldValue);
	}

	//Таблица 2
	public void set_Entitytype2(Long fieldValue) {
		record.set("entitytype2", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Тип связи
	public void set_Relation_type(String fieldValue) {
		record.set("relation_type", fieldValue);
	}

	//Внутренний код
	public void set_Sd_objrelationid(Long fieldValue) {
		record.set("sd_objrelationid", fieldValue);
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
