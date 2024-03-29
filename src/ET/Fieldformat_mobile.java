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

//Формат Поля в Мобильном приложении
public class Fieldformat_mobile extends EntityDecorator {

	private static final String TABLE_NAME = "fieldformat_mobile";

	public Fieldformat_mobile() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Fieldformat_mobile(EntityDTO record) {
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

	//Отображаемое значение
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//ID
	public Long get_Fieldformat_mobileid() {
		return record.getAsLong("fieldformat_mobileid");
	}

	//Поле
	public String get_Fieldname() {
		return record.getAsString("fieldname");
	}

	//Формат (fieldname->fieldtype)
	public String get_Fieldtype() {
		return record.getAsString("fieldtype");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Порядок
	public Long get_Sortorder() {
		return record.getAsLong("sortorder");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Хранимое значение
	public String get_Value() {
		return record.getAsString("value");
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Отображаемое значение
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//ID
	public void set_Fieldformat_mobileid(Long fieldValue) {
		record.set("fieldformat_mobileid", fieldValue);
	}

	//Поле
	public void set_Fieldname(String fieldValue) {
		record.set("fieldname", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Порядок
	public void set_Sortorder(Long fieldValue) {
		record.set("sortorder", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Хранимое значение
	public void set_Value(String fieldValue) {
		record.set("value", fieldValue);
	}

}
