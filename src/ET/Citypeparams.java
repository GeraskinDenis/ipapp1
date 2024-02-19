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

//Параметры типа КЕ (itsm)
public class Citypeparams extends EntityDecorator {

	private static final String TABLE_NAME = "citypeparams";

	public Citypeparams() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Citypeparams(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Вычисление (paramValue)
	public String get_Calculation() {
		return record.getAsString("calculation");
	}

	//Способ вычисления
	public String get_Calculationtype() {
		return record.getAsString("calculationtype");
	}

	//ID
	public Long get_Ciparamsid() {
		return record.getAsLong("ciparamsid");
	}

	//Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Значение по умолчанию
	public String get_Defaultvalue() {
		return record.getAsString("defaultvalue");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Тип данных
	public String get_Fieldtype() {
		return record.getAsString("fieldtype");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Тип параметра
	public String get_Param_type() {
		return record.getAsString("param_type");
	}

	//Имя параметра
	public String get_Paramname() {
		return record.getAsString("paramname");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Не показывать при визуализации
	public Boolean get_Skipvisualization() {
		return record.getAsBoolean("skipvisualization");
	}

	//Порядок вычисления
	public Long get_Sortorder() {
		return record.getAsLong("sortorder");
	}

	//Единицы измерения
	public Long get_Unitofmeasure() {
		return record.getAsLong("unitofmeasure");
	}

	//Кем обновлено
	public Long get_Updatedbyid() {
		return record.getAsLong("updatedbyid");
	}

	//Дата обновления
	public Date get_Updatedtime() {
		return record.getAsDateTime("updatedtime");
	}

	//Нормативное значение (Удалить?)
	public String get_Value() {
		return record.getAsString("value");
	}

	//Формат значения
	public Long get_Value_format() {
		return record.getAsLong("value_format");
	}

	//Вычисление (paramValue)
	public void set_Calculation(String fieldValue) {
		record.set("calculation", fieldValue);
	}

	//Способ вычисления
	public void set_Calculationtype(String fieldValue) {
		record.set("calculationtype", fieldValue);
	}

	//ID
	public void set_Ciparamsid(Long fieldValue) {
		record.set("ciparamsid", fieldValue);
	}

	//Тип КЕ
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Значение по умолчанию
	public void set_Defaultvalue(String fieldValue) {
		record.set("defaultvalue", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Тип данных
	public void set_Fieldtype(String fieldValue) {
		record.set("fieldtype", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Тип параметра
	public void set_Param_type(String fieldValue) {
		record.set("param_type", fieldValue);
	}

	//Имя параметра
	public void set_Paramname(String fieldValue) {
		record.set("paramname", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Не показывать при визуализации
	public void set_Skipvisualization(Boolean fieldValue) {
		record.set("skipvisualization", fieldValue);
	}

	//Порядок вычисления
	public void set_Sortorder(Long fieldValue) {
		record.set("sortorder", fieldValue);
	}

	//Единицы измерения
	public void set_Unitofmeasure(Long fieldValue) {
		record.set("unitofmeasure", fieldValue);
	}

	//Кем обновлено
	public void set_Updatedbyid(Long fieldValue) {
		record.set("updatedbyid", fieldValue);
	}

	//Дата обновления
	public void set_Updatedtime(Date fieldValue) {
		record.set("updatedtime", fieldValue);
	}

	//Нормативное значение (Удалить?)
	public void set_Value(String fieldValue) {
		record.set("value", fieldValue);
	}

	//Формат значения
	public void set_Value_format(Long fieldValue) {
		record.set("value_format", fieldValue);
	}

}
