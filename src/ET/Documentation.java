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

//Документы
public class Documentation extends EntityDecorator {

	private static final String TABLE_NAME = "documentation";

	public Documentation() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Documentation(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Актуальная версия
	public Long get_Actual_version() {
		return record.getAsLong("actual_version");
	}

	//Тип ресурса
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Тип КЕ
	public String get_Citype_shortname() {
		return record.getAsString("citype_shortname");
	}

	//Код поиска
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

	//Подразделение
	public Long get_Department() {
		return record.getAsLong("department");
	}

	//Наименование
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

	//Вид документа
	public Long get_Document_type() {
		return record.getAsLong("document_type");
	}

	//ID
	public Long get_Documentationid() {
		return record.getAsLong("documentationid");
	}

	//Действует по
	public Date get_End_date() {
		return record.getAsDateTime("end_date");
	}

	//Ответственный за информацию
	public Long get_Info_resp() {
		return record.getAsLong("info_resp");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Код поиска
	public String get_Logicalname() {
		return record.getAsString("logicalname");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Действует с
	public Date get_Start_date() {
		return record.getAsDateTime("start_date");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//WorkFlow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Статус
	public Long get_Workflowstepid() {
		return record.getAsLong("workflowstepid");
	}

	//Внутренний статус
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Актуальная версия
	public void set_Actual_version(Long fieldValue) {
		record.set("actual_version", fieldValue);
	}

	//Тип ресурса
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Тип КЕ
	public void set_Citype_shortname(String fieldValue) {
		record.set("citype_shortname", fieldValue);
	}

	//Код поиска
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

	//Подразделение
	public void set_Department(Long fieldValue) {
		record.set("department", fieldValue);
	}

	//Наименование
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

	//Вид документа
	public void set_Document_type(Long fieldValue) {
		record.set("document_type", fieldValue);
	}

	//ID
	public void set_Documentationid(Long fieldValue) {
		record.set("documentationid", fieldValue);
	}

	//Действует по
	public void set_End_date(Date fieldValue) {
		record.set("end_date", fieldValue);
	}

	//Ответственный за информацию
	public void set_Info_resp(Long fieldValue) {
		record.set("info_resp", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Код поиска
	public void set_Logicalname(String fieldValue) {
		record.set("logicalname", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Действует с
	public void set_Start_date(Date fieldValue) {
		record.set("start_date", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//WorkFlow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Статус
	public void set_Workflowstepid(Long fieldValue) {
		record.set("workflowstepid", fieldValue);
	}

	//Внутренний статус
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

}
