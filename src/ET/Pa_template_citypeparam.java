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

//Влияние ППР на параметры КЕ
public class Pa_template_citypeparam extends EntityDecorator {

	private static final String TABLE_NAME = "pa_template_citypeparam";

	public Pa_template_citypeparam() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Pa_template_citypeparam(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Тип КЕ
	public Long get_Citypeid() {
		return record.getAsLong("citypeid");
	}

	//Параметр КЕ
	public Long get_Citypeparamid() {
		return record.getAsLong("citypeparamid");
	}

	//Значение параметра при выполненной ППР
	public String get_Completedvalue() {
		return record.getAsString("completedvalue");
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

	//Значение параметра при НЕ выполненной ППР
	public String get_Notcompletedvalue() {
		return record.getAsString("notcompletedvalue");
	}

	//ID
	public Long get_Pa_template_citypeparamid() {
		return record.getAsLong("pa_template_citypeparamid");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//Настройки ППР
	public Long get_Templateid() {
		return record.getAsLong("templateid");
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

	//Тип КЕ
	public void set_Citypeid(Long fieldValue) {
		record.set("citypeid", fieldValue);
	}

	//Параметр КЕ
	public void set_Citypeparamid(Long fieldValue) {
		record.set("citypeparamid", fieldValue);
	}

	//Значение параметра при выполненной ППР
	public void set_Completedvalue(String fieldValue) {
		record.set("completedvalue", fieldValue);
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

	//Значение параметра при НЕ выполненной ППР
	public void set_Notcompletedvalue(String fieldValue) {
		record.set("notcompletedvalue", fieldValue);
	}

	//ID
	public void set_Pa_template_citypeparamid(Long fieldValue) {
		record.set("pa_template_citypeparamid", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//Настройки ППР
	public void set_Templateid(Long fieldValue) {
		record.set("templateid", fieldValue);
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
