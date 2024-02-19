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

//Параметры КЕ
public class Ciparam extends EntityDecorator {

	private static final String TABLE_NAME = "ciparam";

	public Ciparam() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Ciparam(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//КЕ
	public Long get_Ci() {
		return record.getAsLong("ci");
	}

	//         Параметр
	public Long get_Ciparam() {
		return record.getAsLong("ciparam");
	}

	//ID
	public Long get_Ciparamid() {
		return record.getAsLong("ciparamid");
	}

	//         Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Тип КЕ - строка (citype->shortname)
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

	//Описание параметра
	public String get_Description() {
		return record.getAsString("description");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Индикатор (case healthindicator when 'ERROR' then 'glyphicon glyphicon-remove-sign#FF0000' when 'WARN' then 'glyphicon glyphicon-adjust#FFA500' when 'OK' then 'glyphicon glyphicon-ok-sign#008000' else 'glyphicon glyphicon-ok-sign#008000' end)
	public String get_Healthicon() {
		return record.getAsString("healthicon");
	}

	//Показатель здоровья параметра
	public String get_Healthindicator() {
		return record.getAsString("healthindicator");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Порядок резерв.
	public Long get_Param_order() {
		return record.getAsLong("param_order");
	}

	//Тип параметра (ciparam->param_type)
	public String get_Param_type() {
		return record.getAsString("param_type");
	}

	//Имя параметра (ciparam->paramname)
	public String get_Paramname() {
		return record.getAsString("paramname");
	}

	//Уровень сервиса
	public Long get_Service_level() {
		return record.getAsLong("service_level");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Не показывать (ciparam->skipvisualization)
	public Boolean get_Skipvisualization() {
		return record.getAsBoolean("skipvisualization");
	}

	//Порядок (ciparam->sortorder)
	public Long get_Sortorder() {
		return record.getAsLong("sortorder");
	}

	//Ед. измерения (ciparam->unitofmeasure)
	public Long get_Unit_of_measure() {
		return record.getAsLong("unit_of_measure");
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

	//Значение
	public String get_Value() {
		return record.getAsString("value");
	}

	//Формат значения (ciparam->value_format)
	public Long get_Value_format() {
		return record.getAsLong("value_format");
	}

	//КЕ
	public void set_Ci(Long fieldValue) {
		record.set("ci", fieldValue);
	}

	//         Параметр
	public void set_Ciparam(Long fieldValue) {
		record.set("ciparam", fieldValue);
	}

	//ID
	public void set_Ciparamid(Long fieldValue) {
		record.set("ciparamid", fieldValue);
	}

	//         Тип КЕ
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

	//Описание параметра
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Показатель здоровья параметра
	public void set_Healthindicator(String fieldValue) {
		record.set("healthindicator", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Порядок резерв.
	public void set_Param_order(Long fieldValue) {
		record.set("param_order", fieldValue);
	}

	//Уровень сервиса
	public void set_Service_level(Long fieldValue) {
		record.set("service_level", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
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

	//Значение
	public void set_Value(String fieldValue) {
		record.set("value", fieldValue);
	}

}
