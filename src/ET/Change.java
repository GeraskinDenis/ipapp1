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

//Изменение
public class Change extends EntityDecorator {

	private static final String TABLE_NAME = "change";

	public Change() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Change(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Тип изменения
	public Long get_Change_template() {
		return record.getAsLong("change_template");
	}

	//ID
	public Long get_Changeid() {
		return record.getAsLong("changeid");
	}

	//ID КЕ 
	public Long get_Ci() {
		return record.getAsLong("ci");
	}

	//Формат КЕ
	public Long get_Ci_format() {
		return record.getAsLong("ci_format");
	}

	//КЕ
	public String get_Ci_shortname() {
		return record.getAsString("ci_shortname");
	}

	//Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Параметр КЕ
	public Long get_Citypeparam() {
		return record.getAsLong("citypeparam");
	}

	//Значение Параметра КЕ
	public String get_Citypeparamvalue() {
		return record.getAsString("citypeparamvalue");
	}

	//Код закрытия
	public String get_Closure_code() {
		return record.getAsString("closure_code");
	}

	//Координатор
	public Long get_Coordinator() {
		return record.getAsLong("coordinator");
	}

	//Кем создано
	public Long get_Createdbyid() {
		return record.getAsLong("createdbyid");
	}

	//Дата создания
	public Date get_Createdtime() {
		return record.getAsDateTime("createdtime");
	}

	//Крайний срок
	public Date get_Deadline() {
		return record.getAsDateTime("deadline");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Краткое описание
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Родительский объект
	public Long get_Entityid() {
		return record.getAsLong("entityid");
	}

	//Фактическое окончание
	public Date get_Fact_finish() {
		return record.getAsDateTime("fact_finish");
	}

	//Фактическое начало
	public Date get_Fact_start() {
		return record.getAsDateTime("fact_start");
	}

	//Инициатор
	public Long get_Initiator() {
		return record.getAsLong("initiator");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Местонахождение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Родительский объект
	public Long get_Parent_entity() {
		return record.getAsLong("parent_entity");
	}

	//Тип родительского объекта
	public String get_Parent_entitytype() {
		return record.getAsString("parent_entitytype");
	}

	//Родительский объект
	public Long get_Parentid() {
		return record.getAsLong("parentid");
	}

	//Плановое окончание
	public Date get_Planned_finish() {
		return record.getAsDateTime("planned_finish");
	}

	//Плановое начало
	public Date get_Planned_start() {
		return record.getAsDateTime("planned_start");
	}

	//Приоритет
	public Long get_Priority() {
		return record.getAsLong("priority");
	}

	//План отката
	public String get_Rollback_actions() {
		return record.getAsString("rollback_actions");
	}

	//Услуга
	public Long get_Service() {
		return record.getAsLong("service");
	}

	//Серьезность
	public String get_Severity() {
		return record.getAsString("severity");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Источник поступления
	public String get_Source() {
		return record.getAsString("source");
	}

	//Дата возобновления работ
	public Date get_Unsuspend_date() {
		return record.getAsDateTime("unsuspend_date");
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

	//Рабочая группа
	public Long get_Workgroup() {
		return record.getAsLong("workgroup");
	}

	//Тип изменения
	public void set_Change_template(Long fieldValue) {
		record.set("change_template", fieldValue);
	}

	//ID
	public void set_Changeid(Long fieldValue) {
		record.set("changeid", fieldValue);
	}

	//ID КЕ 
	public void set_Ci(Long fieldValue) {
		record.set("ci", fieldValue);
	}

	//Формат КЕ
	public void set_Ci_format(Long fieldValue) {
		record.set("ci_format", fieldValue);
	}

	//КЕ
	public void set_Ci_shortname(String fieldValue) {
		record.set("ci_shortname", fieldValue);
	}

	//Тип КЕ
	public void set_Citype(Long fieldValue) {
		record.set("citype", fieldValue);
	}

	//Параметр КЕ
	public void set_Citypeparam(Long fieldValue) {
		record.set("citypeparam", fieldValue);
	}

	//Значение Параметра КЕ
	public void set_Citypeparamvalue(String fieldValue) {
		record.set("citypeparamvalue", fieldValue);
	}

	//Код закрытия
	public void set_Closure_code(String fieldValue) {
		record.set("closure_code", fieldValue);
	}

	//Координатор
	public void set_Coordinator(Long fieldValue) {
		record.set("coordinator", fieldValue);
	}

	//Кем создано
	public void set_Createdbyid(Long fieldValue) {
		record.set("createdbyid", fieldValue);
	}

	//Дата создания
	public void set_Createdtime(Date fieldValue) {
		record.set("createdtime", fieldValue);
	}

	//Крайний срок
	public void set_Deadline(Date fieldValue) {
		record.set("deadline", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Краткое описание
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Родительский объект
	public void set_Entityid(Long fieldValue) {
		record.set("entityid", fieldValue);
	}

	//Фактическое окончание
	public void set_Fact_finish(Date fieldValue) {
		record.set("fact_finish", fieldValue);
	}

	//Фактическое начало
	public void set_Fact_start(Date fieldValue) {
		record.set("fact_start", fieldValue);
	}

	//Инициатор
	public void set_Initiator(Long fieldValue) {
		record.set("initiator", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Местонахождение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Родительский объект
	public void set_Parent_entity(Long fieldValue) {
		record.set("parent_entity", fieldValue);
	}

	//Тип родительского объекта
	public void set_Parent_entitytype(String fieldValue) {
		record.set("parent_entitytype", fieldValue);
	}

	//Родительский объект
	public void set_Parentid(Long fieldValue) {
		record.set("parentid", fieldValue);
	}

	//Плановое окончание
	public void set_Planned_finish(Date fieldValue) {
		record.set("planned_finish", fieldValue);
	}

	//Плановое начало
	public void set_Planned_start(Date fieldValue) {
		record.set("planned_start", fieldValue);
	}

	//Приоритет
	public void set_Priority(Long fieldValue) {
		record.set("priority", fieldValue);
	}

	//План отката
	public void set_Rollback_actions(String fieldValue) {
		record.set("rollback_actions", fieldValue);
	}

	//Услуга
	public void set_Service(Long fieldValue) {
		record.set("service", fieldValue);
	}

	//Серьезность
	public void set_Severity(String fieldValue) {
		record.set("severity", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Источник поступления
	public void set_Source(String fieldValue) {
		record.set("source", fieldValue);
	}

	//Дата возобновления работ
	public void set_Unsuspend_date(Date fieldValue) {
		record.set("unsuspend_date", fieldValue);
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

	//Рабочая группа
	public void set_Workgroup(Long fieldValue) {
		record.set("workgroup", fieldValue);
	}

}
