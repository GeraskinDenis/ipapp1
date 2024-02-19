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

//Типовая неисправность
public class Citypicalfault extends EntityDecorator {

	private static final String TABLE_NAME = "citypicalfault";

	public Citypicalfault() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Citypicalfault(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Параметр Типа КЕ
	public Long get_Citypeparam() {
		return record.getAsLong("citypeparam");
	}

	//ID
	public Long get_Citypicalfaultid() {
		return record.getAsLong("citypicalfaultid");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Длительность до кр. срока
	public Long get_Deadline() {
		return record.getAsLong("deadline");
	}

	//Единица длительн. до кр. срока
	public String get_Deadlineunits() {
		return record.getAsString("deadlineunits");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Нижняя граница
	public Double get_Lowervalue() {
		return record.getAsDouble("lowervalue");
	}

	//Приоритет
	public Long get_Priority() {
		return record.getAsLong("priority");
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

	//Верхняя граница
	public Double get_Uppervalue() {
		return record.getAsDouble("uppervalue");
	}

	//Параметр Типа КЕ
	public void set_Citypeparam(Long fieldValue) {
		record.set("citypeparam", fieldValue);
	}

	//ID
	public void set_Citypicalfaultid(Long fieldValue) {
		record.set("citypicalfaultid", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Длительность до кр. срока
	public void set_Deadline(Long fieldValue) {
		record.set("deadline", fieldValue);
	}

	//Единица длительн. до кр. срока
	public void set_Deadlineunits(String fieldValue) {
		record.set("deadlineunits", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Нижняя граница
	public void set_Lowervalue(Double fieldValue) {
		record.set("lowervalue", fieldValue);
	}

	//Приоритет
	public void set_Priority(Long fieldValue) {
		record.set("priority", fieldValue);
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

	//Верхняя граница
	public void set_Uppervalue(Double fieldValue) {
		record.set("uppervalue", fieldValue);
	}

}
