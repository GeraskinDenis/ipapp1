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

//Журнал изменения параметров
public class Ciparamhealthlog extends EntityDecorator {

	private static final String TABLE_NAME = "ciparamhealthlog";

	public Ciparamhealthlog() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Ciparamhealthlog(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//ID КЕ
	public Long get_Ciid() {
		return record.getAsLong("ciid");
	}

	//ID
	public Long get_Ciparamhealthlogid() {
		return record.getAsLong("ciparamhealthlogid");
	}

	//Тип объекта
	public Long get_Citypeid() {
		return record.getAsLong("citypeid");
	}

	//Параметр
	public Long get_Citypeparamid() {
		return record.getAsLong("citypeparamid");
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

	//Нижняя граница показателя здоровья
	public Double get_Newhealthlowervalue() {
		return record.getAsDouble("newhealthlowervalue");
	}

	//Верхняя граница показателя здоровья
	public Double get_Newhealthuppervalue() {
		return record.getAsDouble("newhealthuppervalue");
	}

	//Новое знач. индикатора
	public String get_Newhealthvalue() {
		return record.getAsString("newhealthvalue");
	}

	//Новое значение
	public String get_Newvalue() {
		return record.getAsString("newvalue");
	}

	//Старое знач. индикатора
	public String get_Oldhealthvalue() {
		return record.getAsString("oldhealthvalue");
	}

	//Старое значение
	public String get_Oldvalue() {
		return record.getAsString("oldvalue");
	}

	//Session Context Tag
	public String get_Sessioncontexttag() {
		return record.getAsString("sessioncontexttag");
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

	//ID КЕ
	public void set_Ciid(Long fieldValue) {
		record.set("ciid", fieldValue);
	}

	//ID
	public void set_Ciparamhealthlogid(Long fieldValue) {
		record.set("ciparamhealthlogid", fieldValue);
	}

	//Тип объекта
	public void set_Citypeid(Long fieldValue) {
		record.set("citypeid", fieldValue);
	}

	//Параметр
	public void set_Citypeparamid(Long fieldValue) {
		record.set("citypeparamid", fieldValue);
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

	//Нижняя граница показателя здоровья
	public void set_Newhealthlowervalue(Double fieldValue) {
		record.set("newhealthlowervalue", fieldValue);
	}

	//Верхняя граница показателя здоровья
	public void set_Newhealthuppervalue(Double fieldValue) {
		record.set("newhealthuppervalue", fieldValue);
	}

	//Новое знач. индикатора
	public void set_Newhealthvalue(String fieldValue) {
		record.set("newhealthvalue", fieldValue);
	}

	//Новое значение
	public void set_Newvalue(String fieldValue) {
		record.set("newvalue", fieldValue);
	}

	//Старое знач. индикатора
	public void set_Oldhealthvalue(String fieldValue) {
		record.set("oldhealthvalue", fieldValue);
	}

	//Старое значение
	public void set_Oldvalue(String fieldValue) {
		record.set("oldvalue", fieldValue);
	}

	//Session Context Tag
	public void set_Sessioncontexttag(String fieldValue) {
		record.set("sessioncontexttag", fieldValue);
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

}
