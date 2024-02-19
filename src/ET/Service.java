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

//Услуга
public class Service extends EntityDecorator {

	private static final String TABLE_NAME = "service";

	public Service() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Service(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Календарь доступности услуги
	public Long get_Calendar() {
		return record.getAsLong("calendar");
	}

	//Тип КЕ - число
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Типовая услуга
	public String get_Citype_shortname() {
		return record.getAsString("citype_shortname");
	}

	//Количество потребителей
	public Long get_Consumer_count() {
		return record.getAsLong("consumer_count");
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

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Ссылка на РСМ
	public String get_Link_to_rsm() {
		return record.getAsString("link_to_rsm");
	}

	//Рабочее пространство
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Код поиска
	public String get_Logicalname() {
		return record.getAsString("logicalname");
	}

	//Модель
	public Long get_Model() {
		return record.getAsLong("model");
	}

	//Менеджер услуги
	public Long get_Owner() {
		return record.getAsLong("owner");
	}

	//ID
	public Long get_Serviceid() {
		return record.getAsLong("serviceid");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Дата ввода в эксплуатацию
	public Date get_Startdate() {
		return record.getAsDateTime("startdate");
	}

	//Подсеть
	public Long get_Subnetwork() {
		return record.getAsLong("subnetwork");
	}

	//Информационные системы
	public Long get_Systems() {
		return record.getAsLong("systems");
	}

	//Код услуги
	public String get_Unnumber() {
		return record.getAsString("unnumber");
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

	//Календарь доступности услуги
	public void set_Calendar(Long fieldValue) {
		record.set("calendar", fieldValue);
	}

	//Тип КЕ - число
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Типовая услуга
	public void set_Citype_shortname(String fieldValue) {
		record.set("citype_shortname", fieldValue);
	}

	//Количество потребителей
	public void set_Consumer_count(Long fieldValue) {
		record.set("consumer_count", fieldValue);
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

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Ссылка на РСМ
	public void set_Link_to_rsm(String fieldValue) {
		record.set("link_to_rsm", fieldValue);
	}

	//Рабочее пространство
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Код поиска
	public void set_Logicalname(String fieldValue) {
		record.set("logicalname", fieldValue);
	}

	//Модель
	public void set_Model(Long fieldValue) {
		record.set("model", fieldValue);
	}

	//Менеджер услуги
	public void set_Owner(Long fieldValue) {
		record.set("owner", fieldValue);
	}

	//ID
	public void set_Serviceid(Long fieldValue) {
		record.set("serviceid", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Дата ввода в эксплуатацию
	public void set_Startdate(Date fieldValue) {
		record.set("startdate", fieldValue);
	}

	//Подсеть
	public void set_Subnetwork(Long fieldValue) {
		record.set("subnetwork", fieldValue);
	}

	//Информационные системы
	public void set_Systems(Long fieldValue) {
		record.set("systems", fieldValue);
	}

	//Код услуги
	public void set_Unnumber(String fieldValue) {
		record.set("unnumber", fieldValue);
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
