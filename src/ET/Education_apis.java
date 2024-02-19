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

//API для обучения
public class Education_apis extends EntityDecorator {

	private static final String TABLE_NAME = "education_apis";

	public Education_apis() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Education_apis(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Api
	public String get_Api() {
		return record.getAsString("api");
	}

	//Авторизация
	public String get_Auth() {
		return record.getAsString("auth");
	}

	//Категория
	public String get_Category() {
		return record.getAsString("category");
	}

	//Cors
	public String get_Cors() {
		return record.getAsString("cors");
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

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//ID
	public Long get_Education_apisid() {
		return record.getAsLong("education_apisid");
	}

	//H T T P S
	public Boolean get_Https() {
		return record.getAsBoolean("https");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Link
	public String get_Link() {
		return record.getAsString("link");
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

	//Api
	public void set_Api(String fieldValue) {
		record.set("api", fieldValue);
	}

	//Авторизация
	public void set_Auth(String fieldValue) {
		record.set("auth", fieldValue);
	}

	//Категория
	public void set_Category(String fieldValue) {
		record.set("category", fieldValue);
	}

	//Cors
	public void set_Cors(String fieldValue) {
		record.set("cors", fieldValue);
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

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//ID
	public void set_Education_apisid(Long fieldValue) {
		record.set("education_apisid", fieldValue);
	}

	//H T T P S
	public void set_Https(Boolean fieldValue) {
		record.set("https", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Link
	public void set_Link(String fieldValue) {
		record.set("link", fieldValue);
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
