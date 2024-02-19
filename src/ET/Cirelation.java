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

//Связи КЕ
public class Cirelation extends EntityDecorator {

	private static final String TABLE_NAME = "cirelation";

	public Cirelation() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Cirelation(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//КЕ1
	public Long get_Ci1() {
		return record.getAsLong("ci1");
	}

	//Формат КЕ1
	public Long get_Ci1_format() {
		return record.getAsLong("ci1_format");
	}

	//КЕ2
	public Long get_Ci2() {
		return record.getAsLong("ci2");
	}

	//Формат КЕ1
	public Long get_Ci2_format() {
		return record.getAsLong("ci2_format");
	}

	//Формат КЕ
	public Long get_Ci_format() {
		return record.getAsLong("ci_format");
	}

	//ID
	public Long get_Cirelationid() {
		return record.getAsLong("cirelationid");
	}

	//Тип КЕ1
	public Long get_Citype1() {
		return record.getAsLong("citype1");
	}

	//Тип КЕ2
	public Long get_Citype2() {
		return record.getAsLong("citype2");
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

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Тип связи
	public String get_Relation_type() {
		return record.getAsString("relation_type");
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

	//КЕ1
	public void set_Ci1(Long fieldValue) {
		record.set("ci1", fieldValue);
	}

	//Формат КЕ1
	public void set_Ci1_format(Long fieldValue) {
		record.set("ci1_format", fieldValue);
	}

	//КЕ2
	public void set_Ci2(Long fieldValue) {
		record.set("ci2", fieldValue);
	}

	//Формат КЕ1
	public void set_Ci2_format(Long fieldValue) {
		record.set("ci2_format", fieldValue);
	}

	//Формат КЕ
	public void set_Ci_format(Long fieldValue) {
		record.set("ci_format", fieldValue);
	}

	//ID
	public void set_Cirelationid(Long fieldValue) {
		record.set("cirelationid", fieldValue);
	}

	//Тип КЕ1
	public void set_Citype1(Long fieldValue) {
		record.set("citype1", fieldValue);
	}

	//Тип КЕ2
	public void set_Citype2(Long fieldValue) {
		record.set("citype2", fieldValue);
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

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Тип связи
	public void set_Relation_type(String fieldValue) {
		record.set("relation_type", fieldValue);
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
