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

//История Обращение
public class Interaction_history extends EntityDecorator {

	private static final String TABLE_NAME = "interaction_history";

	public Interaction_history() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Interaction_history(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Пользователь
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Поле
	public Long get_Entityfieldid() {
		return record.getAsLong("entityfieldid");
	}

	//Номер
	public Long get_Entityid() {
		return record.getAsLong("entityid");
	}

	//Сущность
	public Long get_Entitytypeid() {
		return record.getAsLong("entitytypeid");
	}

	//Event Type
	public String get_Eventtype() {
		return record.getAsString("eventtype");
	}

	//Имя Поля
	public String get_Fieldname() {
		return record.getAsString("fieldname");
	}

	//История
	public Long get_Historyid() {
		return record.getAsLong("historyid");
	}

	//Новое Значение
	public String get_Newvalue() {
		return record.getAsString("newvalue");
	}

	//Новое Значение (Текст)
	public String get_Newvaluetext() {
		return record.getAsString("newvaluetext");
	}

	//Старое Значение
	public String get_Oldvalue() {
		return record.getAsString("oldvalue");
	}

	//Старое Значение (Текст)
	public String get_Oldvaluetext() {
		return record.getAsString("oldvaluetext");
	}

	//Кем изменено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Пространство
	public Long get_Workspaceid() {
		return record.getAsLong("workspaceid");
	}

	//Пользователь
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Поле
	public void set_Entityfieldid(Long fieldValue) {
		record.set("entityfieldid", fieldValue);
	}

	//Номер
	public void set_Entityid(Long fieldValue) {
		record.set("entityid", fieldValue);
	}

	//Сущность
	public void set_Entitytypeid(Long fieldValue) {
		record.set("entitytypeid", fieldValue);
	}

	//Event Type
	public void set_Eventtype(String fieldValue) {
		record.set("eventtype", fieldValue);
	}

	//Имя Поля
	public void set_Fieldname(String fieldValue) {
		record.set("fieldname", fieldValue);
	}

	//История
	public void set_Historyid(Long fieldValue) {
		record.set("historyid", fieldValue);
	}

	//Новое Значение
	public void set_Newvalue(String fieldValue) {
		record.set("newvalue", fieldValue);
	}

	//Новое Значение (Текст)
	public void set_Newvaluetext(String fieldValue) {
		record.set("newvaluetext", fieldValue);
	}

	//Старое Значение
	public void set_Oldvalue(String fieldValue) {
		record.set("oldvalue", fieldValue);
	}

	//Старое Значение (Текст)
	public void set_Oldvaluetext(String fieldValue) {
		record.set("oldvaluetext", fieldValue);
	}

	//Кем изменено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Пространство
	public void set_Workspaceid(Long fieldValue) {
		record.set("workspaceid", fieldValue);
	}

}
