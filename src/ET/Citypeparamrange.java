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

//Диапазон значений параметра КЕ
public class Citypeparamrange extends EntityDecorator {

	private static final String TABLE_NAME = "citypeparamrange";

	public Citypeparamrange() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Citypeparamrange(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Целевой параметр КЕ
	public Long get_Citypeparamid() {
		return record.getAsLong("citypeparamid");
	}

	//ID
	public Long get_Citypeparamrangeid() {
		return record.getAsLong("citypeparamrangeid");
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

	//Целевое значение параметра
	public String get_Paramvalue() {
		return record.getAsString("paramvalue");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Референсный параметр типа КЕ
	public Long get_Sourcecitypeparamid() {
		return record.getAsLong("sourcecitypeparamid");
	}

	//Нижнее значение параметра
	public String get_Sourcelowervalue() {
		return record.getAsString("sourcelowervalue");
	}

	//Верхнее значение параметра
	public String get_Sourceuppervalue() {
		return record.getAsString("sourceuppervalue");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Целевой параметр КЕ
	public void set_Citypeparamid(Long fieldValue) {
		record.set("citypeparamid", fieldValue);
	}

	//ID
	public void set_Citypeparamrangeid(Long fieldValue) {
		record.set("citypeparamrangeid", fieldValue);
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

	//Целевое значение параметра
	public void set_Paramvalue(String fieldValue) {
		record.set("paramvalue", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Референсный параметр типа КЕ
	public void set_Sourcecitypeparamid(Long fieldValue) {
		record.set("sourcecitypeparamid", fieldValue);
	}

	//Нижнее значение параметра
	public void set_Sourcelowervalue(String fieldValue) {
		record.set("sourcelowervalue", fieldValue);
	}

	//Верхнее значение параметра
	public void set_Sourceuppervalue(String fieldValue) {
		record.set("sourceuppervalue", fieldValue);
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
