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

//Шаблон работы
public class Plannedactivity_template extends EntityDecorator {

	private static final String TABLE_NAME = "plannedactivity_template";

	public Plannedactivity_template() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Plannedactivity_template(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Название работы
	public String get_Activityname() {
		return record.getAsString("activityname");
	}

	//Рабочий график
	public String get_Calendarname() {
		return record.getAsString("calendarname");
	}

	//Категория работ
	public String get_Category() {
		return record.getAsString("category");
	}

	//КЕ
	public String get_Ci_shortname() {
		return record.getAsString("ci_shortname");
	}

	//Формат объекта работ
	public Long get_Ci_shortname_format() {
		return record.getAsLong("ci_shortname_format");
	}

	//Объект работ
	public Long get_Ciids() {
		return record.getAsLong("ciids");
	}

	//Запрос для выборки объектов работ
	public String get_Ciquery() {
		return record.getAsString("ciquery");
	}

	//Тип КЕ
	public Long get_Citypeid() {
		return record.getAsLong("citypeid");
	}

	//Состоит из задач
	public Boolean get_Consists_of_tasks() {
		return record.getAsBoolean("consists_of_tasks");
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

	//Накопительное смещение на длит. работ
	public Boolean get_Cumulativeshift() {
		return record.getAsBoolean("cumulativeshift");
	}

	//Дни с начала месяца 
	public Long get_Days() {
		return record.getAsLong("days");
	}

	//Дни недели
	public Long get_Daysofweek() {
		return record.getAsLong("daysofweek");
	}

	//Дни года (DD.MM)
	public String get_Daysofyear() {
		return record.getAsString("daysofyear");
	}

	//Длительность до крайнего срока
	public Double get_Deadline_duration() {
		return record.getAsDouble("deadline_duration");
	}

	//Ед. изм. длительности
	public String get_Deadlinetimeunit() {
		return record.getAsString("deadlinetimeunit");
	}

	//Длительность работ (по раб. графику)
	public Long get_Deadlineunitcount() {
		return record.getAsLong("deadlineunitcount");
	}

	//Описание
	public String get_Description() {
		return record.getAsString("description");
	}

	//Название
	public String get_Displayname() {
		return record.getAsString("displayname");
	}

	//Крайняя дата генерации
	public Date get_Generatetodate() {
		return record.getAsDateTime("generatetodate");
	}

	//Тип генерации
	public String get_Generationtype() {
		return record.getAsString("generationtype");
	}

	//Инициатор
	public Long get_Initiator() {
		return record.getAsLong("initiator");
	}

	//Шаблон обращения
	public Long get_Inter_templ() {
		return record.getAsLong("inter_templ");
	}

	//Ед. измерения интервала
	public String get_Intervaltimeunit() {
		return record.getAsString("intervaltimeunit");
	}

	//Кол-во единиц интервала
	public Long get_Intervalunits() {
		return record.getAsLong("intervalunits");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Количество дней с конца месяца
	public Long get_Lastdays() {
		return record.getAsLong("lastdays");
	}

	//Ближайшее время генерации
	public Date get_Nextplannedtime() {
		return record.getAsDateTime("nextplannedtime");
	}

	//Добавить дни с конца месяца
	public Boolean get_Onlastday() {
		return record.getAsBoolean("onlastday");
	}

	//ID
	public Long get_Plannedactivity_templateid() {
		return record.getAsLong("plannedactivity_templateid");
	}

	//Плановая трудоемкость
	public Double get_Plannedlaborintensity() {
		return record.getAsDouble("plannedlaborintensity");
	}

	//Использовать вычисленную дату как
	public String get_Plannedtimetype() {
		return record.getAsString("plannedtimetype");
	}

	//Горизонт планирования
	public String get_Planningtimeunit() {
		return record.getAsString("planningtimeunit");
	}

	//Последняя плановая дата
	public Date get_Repeatenddate() {
		return record.getAsDateTime("repeatenddate");
	}

	//Тип повторения
	public String get_Repeattype() {
		return record.getAsString("repeattype");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Тест
	public String get_Testplan() {
		return record.getAsString("testplan");
	}

	//Время суток
	public Time get_Timeofday() {
		return record.getAsTime("timeofday");
	}

	//Часовой пояс
	public Long get_Timezone() {
		return record.getAsLong("timezone");
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

	//Название работы
	public void set_Activityname(String fieldValue) {
		record.set("activityname", fieldValue);
	}

	//Рабочий график
	public void set_Calendarname(String fieldValue) {
		record.set("calendarname", fieldValue);
	}

	//Категория работ
	public void set_Category(String fieldValue) {
		record.set("category", fieldValue);
	}

	//КЕ
	public void set_Ci_shortname(String fieldValue) {
		record.set("ci_shortname", fieldValue);
	}

	//Формат объекта работ
	public void set_Ci_shortname_format(Long fieldValue) {
		record.set("ci_shortname_format", fieldValue);
	}

	//Объект работ
	public void set_Ciids(Long fieldValue) {
		record.set("ciids", fieldValue);
	}

	//Запрос для выборки объектов работ
	public void set_Ciquery(String fieldValue) {
		record.set("ciquery", fieldValue);
	}

	//Тип КЕ
	public void set_Citypeid(Long fieldValue) {
		record.set("citypeid", fieldValue);
	}

	//Состоит из задач
	public void set_Consists_of_tasks(Boolean fieldValue) {
		record.set("consists_of_tasks", fieldValue);
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

	//Накопительное смещение на длит. работ
	public void set_Cumulativeshift(Boolean fieldValue) {
		record.set("cumulativeshift", fieldValue);
	}

	//Дни с начала месяца 
	public void set_Days(Long fieldValue) {
		record.set("days", fieldValue);
	}

	//Дни недели
	public void set_Daysofweek(Long fieldValue) {
		record.set("daysofweek", fieldValue);
	}

	//Дни года (DD.MM)
	public void set_Daysofyear(String fieldValue) {
		record.set("daysofyear", fieldValue);
	}

	//Длительность до крайнего срока
	public void set_Deadline_duration(Double fieldValue) {
		record.set("deadline_duration", fieldValue);
	}

	//Ед. изм. длительности
	public void set_Deadlinetimeunit(String fieldValue) {
		record.set("deadlinetimeunit", fieldValue);
	}

	//Длительность работ (по раб. графику)
	public void set_Deadlineunitcount(Long fieldValue) {
		record.set("deadlineunitcount", fieldValue);
	}

	//Описание
	public void set_Description(String fieldValue) {
		record.set("description", fieldValue);
	}

	//Название
	public void set_Displayname(String fieldValue) {
		record.set("displayname", fieldValue);
	}

	//Крайняя дата генерации
	public void set_Generatetodate(Date fieldValue) {
		record.set("generatetodate", fieldValue);
	}

	//Тип генерации
	public void set_Generationtype(String fieldValue) {
		record.set("generationtype", fieldValue);
	}

	//Инициатор
	public void set_Initiator(Long fieldValue) {
		record.set("initiator", fieldValue);
	}

	//Шаблон обращения
	public void set_Inter_templ(Long fieldValue) {
		record.set("inter_templ", fieldValue);
	}

	//Ед. измерения интервала
	public void set_Intervaltimeunit(String fieldValue) {
		record.set("intervaltimeunit", fieldValue);
	}

	//Кол-во единиц интервала
	public void set_Intervalunits(Long fieldValue) {
		record.set("intervalunits", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Количество дней с конца месяца
	public void set_Lastdays(Long fieldValue) {
		record.set("lastdays", fieldValue);
	}

	//Ближайшее время генерации
	public void set_Nextplannedtime(Date fieldValue) {
		record.set("nextplannedtime", fieldValue);
	}

	//Добавить дни с конца месяца
	public void set_Onlastday(Boolean fieldValue) {
		record.set("onlastday", fieldValue);
	}

	//ID
	public void set_Plannedactivity_templateid(Long fieldValue) {
		record.set("plannedactivity_templateid", fieldValue);
	}

	//Плановая трудоемкость
	public void set_Plannedlaborintensity(Double fieldValue) {
		record.set("plannedlaborintensity", fieldValue);
	}

	//Использовать вычисленную дату как
	public void set_Plannedtimetype(String fieldValue) {
		record.set("plannedtimetype", fieldValue);
	}

	//Горизонт планирования
	public void set_Planningtimeunit(String fieldValue) {
		record.set("planningtimeunit", fieldValue);
	}

	//Последняя плановая дата
	public void set_Repeatenddate(Date fieldValue) {
		record.set("repeatenddate", fieldValue);
	}

	//Тип повторения
	public void set_Repeattype(String fieldValue) {
		record.set("repeattype", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Тест
	public void set_Testplan(String fieldValue) {
		record.set("testplan", fieldValue);
	}

	//Время суток
	public void set_Timeofday(Time fieldValue) {
		record.set("timeofday", fieldValue);
	}

	//Часовой пояс
	public void set_Timezone(Long fieldValue) {
		record.set("timezone", fieldValue);
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
