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

//Наряд
public class Task extends EntityDecorator {

	private static final String TABLE_NAME = "task";

	public Task() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Task(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Исполнитель
	public Long get_Assignedto() {
		return record.getAsLong("assignedto");
	}

	//Календарь
	public String get_Calendarname() {
		return record.getAsString("calendarname");
	}

	//Категория
	public String get_Category() {
		return record.getAsString("category");
	}

	//КЕ
	public Long get_Ci() {
		return record.getAsLong("ci");
	}

	//Формат КЕ
	public Long get_Ci_format() {
		return record.getAsLong("ci_format");
	}

	//КЕ - строка
	public String get_Ci_shortname() {
		return record.getAsString("ci_shortname");
	}

	//Формат  для КЕ - строка
	public Long get_Ci_shortname_format() {
		return record.getAsLong("ci_shortname_format");
	}

	//Тип КЕ
	public Long get_Citype() {
		return record.getAsLong("citype");
	}

	//Цвет (CASE WHEN (workflowstepname in ('planned','inwork') and deadline<LOCALTIMESTAMP) THEN '#EB8A8A' WHEN ((workflowstepname='planned' and (planned_start<LOCALTIMESTAMP)) OR (workflowstepname in ('planned','inwork') and planned_finish<LOCALTIMESTAMP)) THEN '#E8E67D' END)
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

	//Крайний срок
	public Date get_Deadline() {
		return record.getAsDateTime("deadline");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Длительность (часов)
	public Double get_Duration() {
		return record.getAsDouble("duration");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Окно показывалось
	public Boolean get_Isalertviewed() {
		return record.getAsBoolean("isalertviewed");
	}

	//Необязат. для закрытия
	public Boolean get_Isnt_nec_for_closing() {
		return record.getAsBoolean("isnt_nec_for_closing");
	}

	//Местонахождение
	public Long get_Location() {
		return record.getAsLong("location");
	}

	//Родительский объект
	public Long get_Parent_entityid() {
		return record.getAsLong("parent_entityid");
	}

	//Тип родительского объекта
	public String get_Parent_entitytype() {
		return record.getAsString("parent_entitytype");
	}

	//Плановое окончание
	public Date get_Planned_finish() {
		return record.getAsDateTime("planned_finish");
	}

	//Плановое начало
	public Date get_Planned_start() {
		return record.getAsDateTime("planned_start");
	}

	//Предшественник
	public Long get_Predecessor() {
		return record.getAsLong("predecessor");
	}

	//Приоритет
	public Long get_Priority() {
		return record.getAsLong("priority");
	}

	//Услуга
	public Long get_Service() {
		return record.getAsLong("service");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Решение
	public String get_Solution() {
		return record.getAsString("solution");
	}

	//ID
	public Long get_Taskid() {
		return record.getAsLong("taskid");
	}

	//Шаблон
	public Long get_Template() {
		return record.getAsLong("template");
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

	//Название шага
	public String get_Workflowstepname() {
		return record.getAsString("workflowstepname");
	}

	//Рабочая группа
	public Long get_Workgroup() {
		return record.getAsLong("workgroup");
	}

	//Пространство
	public Long get_Workspace() {
		return record.getAsLong("workspace");
	}

	//Исполнитель
	public void set_Assignedto(Long fieldValue) {
		record.set("assignedto", fieldValue);
	}

	//Календарь
	public void set_Calendarname(String fieldValue) {
		record.set("calendarname", fieldValue);
	}

	//Категория
	public void set_Category(String fieldValue) {
		record.set("category", fieldValue);
	}

	//КЕ
	public void set_Ci(Long fieldValue) {
		record.set("ci", fieldValue);
	}

	//Формат КЕ
	public void set_Ci_format(Long fieldValue) {
		record.set("ci_format", fieldValue);
	}

	//КЕ - строка
	public void set_Ci_shortname(String fieldValue) {
		record.set("ci_shortname", fieldValue);
	}

	//Формат  для КЕ - строка
	public void set_Ci_shortname_format(Long fieldValue) {
		record.set("ci_shortname_format", fieldValue);
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

	//Крайний срок
	public void set_Deadline(Date fieldValue) {
		record.set("deadline", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Длительность (часов)
	public void set_Duration(Double fieldValue) {
		record.set("duration", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Окно показывалось
	public void set_Isalertviewed(Boolean fieldValue) {
		record.set("isalertviewed", fieldValue);
	}

	//Необязат. для закрытия
	public void set_Isnt_nec_for_closing(Boolean fieldValue) {
		record.set("isnt_nec_for_closing", fieldValue);
	}

	//Местонахождение
	public void set_Location(Long fieldValue) {
		record.set("location", fieldValue);
	}

	//Родительский объект
	public void set_Parent_entityid(Long fieldValue) {
		record.set("parent_entityid", fieldValue);
	}

	//Тип родительского объекта
	public void set_Parent_entitytype(String fieldValue) {
		record.set("parent_entitytype", fieldValue);
	}

	//Плановое окончание
	public void set_Planned_finish(Date fieldValue) {
		record.set("planned_finish", fieldValue);
	}

	//Плановое начало
	public void set_Planned_start(Date fieldValue) {
		record.set("planned_start", fieldValue);
	}

	//Предшественник
	public void set_Predecessor(Long fieldValue) {
		record.set("predecessor", fieldValue);
	}

	//Приоритет
	public void set_Priority(Long fieldValue) {
		record.set("priority", fieldValue);
	}

	//Услуга
	public void set_Service(Long fieldValue) {
		record.set("service", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Решение
	public void set_Solution(String fieldValue) {
		record.set("solution", fieldValue);
	}

	//ID
	public void set_Taskid(Long fieldValue) {
		record.set("taskid", fieldValue);
	}

	//Шаблон
	public void set_Template(Long fieldValue) {
		record.set("template", fieldValue);
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

	//Название шага
	public void set_Workflowstepname(String fieldValue) {
		record.set("workflowstepname", fieldValue);
	}

	//Рабочая группа
	public void set_Workgroup(Long fieldValue) {
		record.set("workgroup", fieldValue);
	}

	//Пространство
	public void set_Workspace(Long fieldValue) {
		record.set("workspace", fieldValue);
	}

}
