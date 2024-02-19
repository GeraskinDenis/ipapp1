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

//Типы ресурсов
public class Citype extends EntityDecorator {

	private static final String TABLE_NAME = "citype";

	public Citype() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Citype(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Запрос для выборки активных КЕ
	public String get_Activeitemsquery() {
		return record.getAsString("activeitemsquery");
	}

	//Поля кода поиска КЕ
	public String get_Citype_uniquefieldsline() {
		return record.getAsString("citype_uniquefieldsline");
	}

	//ID
	public Long get_Citypeid() {
		return record.getAsLong("citypeid");
	}

	//Цвет
	public String get_Color() {
		return record.getAsString("color");
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

	//Таблица
	public Long get_Entitytype() {
		return record.getAsLong("entitytype");
	}

	//Формат для выбора данных
	public Long get_Fieldformat_integer() {
		return record.getAsLong("fieldformat_integer");
	}

	//Изображение
	public String get_Icon() {
		return record.getAsString("icon");
	}

	//Код поиска сформирован
	public Boolean get_Is_uniquefields() {
		return record.getAsBoolean("is_uniquefields");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Уровень
	public String get_Level() {
		return record.getAsString("level");
	}

	//Родительский тип
	public Long get_Parent_citype() {
		return record.getAsLong("parent_citype");
	}

	//Префикс
	public String get_Prefix() {
		return record.getAsString("prefix");
	}

	//Порядковый номер
	public Long get_Row_order() {
		return record.getAsLong("row_order");
	}

	//Внутреннее наименование
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Системное наименование
	public String get_Tablename() {
		return record.getAsString("tablename");
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

	//Запрос для выборки активных КЕ
	public void set_Activeitemsquery(String fieldValue) {
		record.set("activeitemsquery", fieldValue);
	}

	//Поля кода поиска КЕ
	public void set_Citype_uniquefieldsline(String fieldValue) {
		record.set("citype_uniquefieldsline", fieldValue);
	}

	//ID
	public void set_Citypeid(Long fieldValue) {
		record.set("citypeid", fieldValue);
	}

	//Цвет
	public void set_Color(String fieldValue) {
		record.set("color", fieldValue);
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

	//Таблица
	public void set_Entitytype(Long fieldValue) {
		record.set("entitytype", fieldValue);
	}

	//Формат для выбора данных
	public void set_Fieldformat_integer(Long fieldValue) {
		record.set("fieldformat_integer", fieldValue);
	}

	//Изображение
	public void set_Icon(String fieldValue) {
		record.set("icon", fieldValue);
	}

	//Код поиска сформирован
	public void set_Is_uniquefields(Boolean fieldValue) {
		record.set("is_uniquefields", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Уровень
	public void set_Level(String fieldValue) {
		record.set("level", fieldValue);
	}

	//Родительский тип
	public void set_Parent_citype(Long fieldValue) {
		record.set("parent_citype", fieldValue);
	}

	//Префикс
	public void set_Prefix(String fieldValue) {
		record.set("prefix", fieldValue);
	}

	//Порядковый номер
	public void set_Row_order(Long fieldValue) {
		record.set("row_order", fieldValue);
	}

	//Внутреннее наименование
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Системное наименование
	public void set_Tablename(String fieldValue) {
		record.set("tablename", fieldValue);
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

}
