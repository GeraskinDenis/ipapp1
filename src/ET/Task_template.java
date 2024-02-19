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

//Шаблон задачи
public class Task_template extends EntityDecorator {

	private static final String TABLE_NAME = "task_template";

	public Task_template() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Task_template(EntityDTO record) {
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

	//Рабочий график
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

	//Длительность до крайнего срока
	public Double get_Deadline_duration() {
		return record.getAsDouble("deadline_duration");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Наименование
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Длительность
	public Double get_Duration() {
		return record.getAsDouble("duration");
	}

	//Единицы длительности
	public String get_Duruation_unitofmeasure() {
		return record.getAsString("duruation_unitofmeasure");
	}

	//Разрыв, мин.
	public Long get_Gap() {
		return record.getAsLong("gap");
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
	public Long get_Parent_entityid() {
		return record.getAsLong("parent_entityid");
	}

	//Тип родительского объекта
	public String get_Parent_entitytype() {
		return record.getAsString("parent_entitytype");
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

	//Short Name
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//ID
	public Long get_Task_templateid() {
		return record.getAsLong("task_templateid");
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

	//Исполнитель
	public void set_Assignedto(Long fieldValue) {
		record.set("assignedto", fieldValue);
	}

	//Рабочий график
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

	//Длительность до крайнего срока
	public void set_Deadline_duration(Double fieldValue) {
		record.set("deadline_duration", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Наименование
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Длительность
	public void set_Duration(Double fieldValue) {
		record.set("duration", fieldValue);
	}

	//Единицы длительности
	public void set_Duruation_unitofmeasure(String fieldValue) {
		record.set("duruation_unitofmeasure", fieldValue);
	}

	//Разрыв, мин.
	public void set_Gap(Long fieldValue) {
		record.set("gap", fieldValue);
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
	public void set_Parent_entityid(Long fieldValue) {
		record.set("parent_entityid", fieldValue);
	}

	//Тип родительского объекта
	public void set_Parent_entitytype(String fieldValue) {
		record.set("parent_entitytype", fieldValue);
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

	//Short Name
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//ID
	public void set_Task_templateid(Long fieldValue) {
		record.set("task_templateid", fieldValue);
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

}
