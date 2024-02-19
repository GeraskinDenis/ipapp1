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

//Документы по КЕ
public class Documentations_po extends EntityDecorator {

	private static final String TABLE_NAME = "documentations_po";

	public Documentations_po() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Documentations_po(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Тип КЕ
	public String get_Citype_shortname() {
		return record.getAsString("citype_shortname");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Название
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Автор
	public String get_Doc_author() {
		return record.getAsString("doc_author");
	}

	//Дата документа
	public Date get_Doc_date() {
		return record.getAsDateTime("doc_date");
	}

	//Номер документа
	public String get_Doc_num() {
		return record.getAsString("doc_num");
	}

	//Актуальная версия
	public Long get_Doc_ver() {
		return record.getAsLong("doc_ver");
	}

	//Вид документа
	public Long get_Document_type() {
		return record.getAsLong("document_type");
	}

	//Документ
	public Long get_Documentation() {
		return record.getAsLong("documentation");
	}

	//ID
	public Long get_Documentations_poid() {
		return record.getAsLong("documentations_poid");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//КЕ
	public String get_Po_shortname() {
		return record.getAsString("po_shortname");
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

	//Тип КЕ
	public void set_Citype_shortname(String fieldValue) {
		record.set("citype_shortname", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Название
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Автор
	public void set_Doc_author(String fieldValue) {
		record.set("doc_author", fieldValue);
	}

	//Дата документа
	public void set_Doc_date(Date fieldValue) {
		record.set("doc_date", fieldValue);
	}

	//Номер документа
	public void set_Doc_num(String fieldValue) {
		record.set("doc_num", fieldValue);
	}

	//Актуальная версия
	public void set_Doc_ver(Long fieldValue) {
		record.set("doc_ver", fieldValue);
	}

	//Вид документа
	public void set_Document_type(Long fieldValue) {
		record.set("document_type", fieldValue);
	}

	//Документ
	public void set_Documentation(Long fieldValue) {
		record.set("documentation", fieldValue);
	}

	//ID
	public void set_Documentations_poid(Long fieldValue) {
		record.set("documentations_poid", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//КЕ
	public void set_Po_shortname(String fieldValue) {
		record.set("po_shortname", fieldValue);
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
