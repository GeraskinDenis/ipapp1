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

//test2GDGD
public class Test2gdgd extends EntityDecorator {

	private static final String TABLE_NAME = "test2gdgd";

	public Test2gdgd() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Test2gdgd(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Тип КЕ - строка
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

	//Местонахождение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Сотрудник
	public Long get_Owner() {
		return record.getAsLong("owner");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Дата ввода в эксплуатацию
	public Date get_Startdate() {
		return record.getAsDateTime("startdate");
	}

	//ID
	public Long get_Test2gdgdid() {
		return record.getAsLong("test2gdgdid");
	}

	//Серийный номер
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

	//Ответственная рабочая группа
	public Long get_Workgroup() {
		return record.getAsLong("workgroup");
	}

	//Тип КЕ
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Тип КЕ - строка
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

	//Местонахождение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Сотрудник
	public void set_Owner(Long fieldValue) {
		record.set("owner", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Дата ввода в эксплуатацию
	public void set_Startdate(Date fieldValue) {
		record.set("startdate", fieldValue);
	}

	//ID
	public void set_Test2gdgdid(Long fieldValue) {
		record.set("test2gdgdid", fieldValue);
	}

	//Серийный номер
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

	//Ответственная рабочая группа
	public void set_Workgroup(Long fieldValue) {
		record.set("workgroup", fieldValue);
	}

}
