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

//Монитор
public class Monitor extends EntityDecorator {

	private static final String TABLE_NAME = "monitor";

	public Monitor() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Monitor(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Тип КЕ - число
	public Long get_Citype() {
		return record.getAsLong("citype");
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

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Срок службы, лет (model->lifeduration)
	public Double get_Lifeduration() {
		return record.getAsDouble("lifeduration");
	}

	//Расположение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Код поиска
	public String get_Logicalname() {
		return record.getAsString("logicalname");
	}

	//Производитель
	public String get_Manufacturer() {
		return record.getAsString("manufacturer");
	}

	//Модель
	public String get_Model() {
		return record.getAsString("model");
	}

	//ID
	public Long get_Monitorid() {
		return record.getAsLong("monitorid");
	}

	//Сотрудник
	public Long get_Owner() {
		return record.getAsLong("owner");
	}

	//QR-code
	public String get_Qr_code() {
		return record.getAsString("qr_code");
	}

	//Серийный номер
	public String get_Serialno() {
		return record.getAsString("serialno");
	}

	//Услуга
	public Long get_Service() {
		return record.getAsLong("service");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Дата ввода в эксплуатацию
	public Date get_Startdate() {
		return record.getAsDateTime("startdate");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Work Flow
	public Long get_Workflowid() {
		return record.getAsLong("workflowid");
	}

	//Статус
	public Long get_Workflowstepid() {
		return record.getAsLong("workflowstepid");
	}

	//Название шага
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Рабочее место
	public Long get_Workplace() {
		return record.getAsLong("workplace");
	}

	//Владелец рабочего места (workplace->owner)
	public Long get_Workplace_owner() {
		return record.getAsLong("workplace_owner");
	}

	//Тип КЕ - число
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
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

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Расположение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Код поиска
	public void set_Logicalname(String fieldValue) {
		record.set("logicalname", fieldValue);
	}

	//Производитель
	public void set_Manufacturer(String fieldValue) {
		record.set("manufacturer", fieldValue);
	}

	//Модель
	public void set_Model(String fieldValue) {
		record.set("model", fieldValue);
	}

	//ID
	public void set_Monitorid(Long fieldValue) {
		record.set("monitorid", fieldValue);
	}

	//Сотрудник
	public void set_Owner(Long fieldValue) {
		record.set("owner", fieldValue);
	}

	//QR-code
	public void set_Qr_code(String fieldValue) {
		record.set("qr_code", fieldValue);
	}

	//Серийный номер
	public void set_Serialno(String fieldValue) {
		record.set("serialno", fieldValue);
	}

	//Услуга
	public void set_Service(Long fieldValue) {
		record.set("service", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Дата ввода в эксплуатацию
	public void set_Startdate(Date fieldValue) {
		record.set("startdate", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Work Flow
	public void set_Workflowid(Long fieldValue) {
		record.set("workflowid", fieldValue);
	}

	//Статус
	public void set_Workflowstepid(Long fieldValue) {
		record.set("workflowstepid", fieldValue);
	}

	//Название шага
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

	//Рабочее место
	public void set_Workplace(Long fieldValue) {
		record.set("workplace", fieldValue);
	}

}
