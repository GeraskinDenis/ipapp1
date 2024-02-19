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

//Сервер
public class Server extends EntityDecorator {

	private static final String TABLE_NAME = "server";

	public Server() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Server(EntityDTO record) {
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

	// Имя хоста
	public String get_Hostname() {
		return record.getAsString("hostname");
	}

	//Информационная система
	public Long get_Informationsystem() {
		return record.getAsLong("informationsystem");
	}

	//IP
	public String get_Ipaddress() {
		return record.getAsString("ipaddress");
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

	//Модель
	public String get_Model() {
		return record.getAsString("model");
	}

	//ОС
	public Long get_Operatingsystemid() {
		return record.getAsLong("operatingsystemid");
	}

	//Ответственный
	public Long get_Owner() {
		return record.getAsLong("owner");
	}

	//Установленное ПО
	public Long get_Rrelated_soft_list() {
		return record.getAsLong("rrelated_soft_list");
	}

	//Серийный номер
	public String get_Serialno() {
		return record.getAsString("serialno");
	}

	//ID
	public Long get_Serverid() {
		return record.getAsLong("serverid");
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

	//Рабочая группа
	public Long get_Workgroup() {
		return record.getAsLong("workgroup");
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

	// Имя хоста
	public void set_Hostname(String fieldValue) {
		record.set("hostname", fieldValue);
	}

	//Информационная система
	public void set_Informationsystem(Long fieldValue) {
		record.set("informationsystem", fieldValue);
	}

	//IP
	public void set_Ipaddress(String fieldValue) {
		record.set("ipaddress", fieldValue);
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

	//Модель
	public void set_Model(String fieldValue) {
		record.set("model", fieldValue);
	}

	//ОС
	public void set_Operatingsystemid(Long fieldValue) {
		record.set("operatingsystemid", fieldValue);
	}

	//Ответственный
	public void set_Owner(Long fieldValue) {
		record.set("owner", fieldValue);
	}

	//Установленное ПО
	public void set_Rrelated_soft_list(Long fieldValue) {
		record.set("rrelated_soft_list", fieldValue);
	}

	//Серийный номер
	public void set_Serialno(String fieldValue) {
		record.set("serialno", fieldValue);
	}

	//ID
	public void set_Serverid(Long fieldValue) {
		record.set("serverid", fieldValue);
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

	//Рабочая группа
	public void set_Workgroup(Long fieldValue) {
		record.set("workgroup", fieldValue);
	}

}
