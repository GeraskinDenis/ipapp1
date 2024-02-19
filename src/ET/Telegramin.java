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

//Входящие сообщения Telegram
public class Telegramin extends EntityDecorator {

	private static final String TABLE_NAME = "telegramin";

	public Telegramin() {
		this.record = new EntityDTO(TABLE_NAME);
	}

	public Telegramin(EntityDTO record) {
		if (record == null)
			throw new ValidationException("Origin record is null!");
		if (!record.getTableName().equals(TABLE_NAME)) {
			String message = "Using object for table '%s', but table of origin entity is '%s'.";
			throw new ValidationException(String.format(message, TABLE_NAME, record.getTableName()));
		}
		this.record = record;
	}

	//Chat_id
	public Long get_Chat_id() {
		return record.getAsLong("chat_id");
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

	//First_name
	public String get_First_name() {
		return record.getAsString("first_name");
	}

	//Активно
	public Boolean get_Isactive() {
		return record.getAsBoolean("isactive");
	}

	//Last_name
	public String get_Last_name() {
		return record.getAsString("last_name");
	}

	//Message_id
	public Long get_Message_id() {
		return record.getAsLong("message_id");
	}

	//Reply_message_id
	public Long get_Reply_message_id() {
		return record.getAsLong("reply_message_id");
	}

	//Reply_text
	public String get_Reply_text() {
		return record.getAsString("reply_text");
	}

	//Внутреннее имя
	public String get_Shortname() {
		return record.getAsString("shortname");
	}

	//ID
	public Long get_Telegraminid() {
		return record.getAsLong("telegraminid");
	}

	//Text
	public String get_Text() {
		return record.getAsString("text");
	}

	//Update_id
	public Long get_Update_id() {
		return record.getAsLong("update_id");
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

	//Chat_id
	public void set_Chat_id(Long fieldValue) {
		record.set("chat_id", fieldValue);
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

	//First_name
	public void set_First_name(String fieldValue) {
		record.set("first_name", fieldValue);
	}

	//Активно
	public void set_Isactive(Boolean fieldValue) {
		record.set("isactive", fieldValue);
	}

	//Last_name
	public void set_Last_name(String fieldValue) {
		record.set("last_name", fieldValue);
	}

	//Message_id
	public void set_Message_id(Long fieldValue) {
		record.set("message_id", fieldValue);
	}

	//Reply_message_id
	public void set_Reply_message_id(Long fieldValue) {
		record.set("reply_message_id", fieldValue);
	}

	//Reply_text
	public void set_Reply_text(String fieldValue) {
		record.set("reply_text", fieldValue);
	}

	//Внутреннее имя
	public void set_Shortname(String fieldValue) {
		record.set("shortname", fieldValue);
	}

	//ID
	public void set_Telegraminid(Long fieldValue) {
		record.set("telegraminid", fieldValue);
	}

	//Text
	public void set_Text(String fieldValue) {
		record.set("text", fieldValue);
	}

	//Update_id
	public void set_Update_id(Long fieldValue) {
		record.set("update_id", fieldValue);
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
