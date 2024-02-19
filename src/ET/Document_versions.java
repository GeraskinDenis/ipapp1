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

//Версии документов
public class Document_versions extends EntityDecorator {

	private static final String TABLE_NAME = "document_versions";

	public Document_versions() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Document_versions(EntityDTO record) {
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

	//Документ
	public Long get_Document() {
		return record.getAsLong("document");
	}

	//ID
	public Long get_Document_versionsid() {
		return record.getAsLong("document_versionsid");
	}

	//Файл
	public Long get_File() {
		return record.getAsLong("file");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Актуальная
	public Boolean get_Isactual() {
		return record.getAsBoolean("isactual");
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

	//Дата версии
	public Date get_Version_datetime() {
		return record.getAsDateTime("version_datetime");
	}

	//Номер версии
	public String get_Version_number() {
		return record.getAsString("version_number");
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

	//Документ
	public void set_Document(Long fieldValue) {
		record.set("document", fieldValue);
	}

	//ID
	public void set_Document_versionsid(Long fieldValue) {
		record.set("document_versionsid", fieldValue);
	}

	//Файл
	public void set_File(Long fieldValue) {
		record.set("file", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Актуальная
	public void set_Isactual(Boolean fieldValue) {
		record.set("isactual", fieldValue);
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

	//Дата версии
	public void set_Version_datetime(Date fieldValue) {
		record.set("version_datetime", fieldValue);
	}

	//Номер версии
	public void set_Version_number(String fieldValue) {
		record.set("version_number", fieldValue);
	}

}
