package EntityTypeRule;

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

/**
DisplayName: Сотрудник
Fields:
 - authtype (Способ Аутентификации) - STRING
 - avatar_file (Аватарка) - INTEGER
 - birthdate (Дата рождения) - DATE
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - email (Email) - STRING
 - emailstatus (Статус почты) - STRING
 - fired_date (Дата увольнения) - DATE
 - fullname (Полное имя) - STRING
 - isactive (Активно) - BOOLEAN
 - loginname (Логин) - STRING
 - loginpass (Пароль) - STRING
 - orgunit (Подразделение) - INTEGER
 - phone (Телефон) - STRING
 - roleid (Роль) - INTEGER
 - shortname (Табельный номер) - STRING
 - startdate (Дата приема на работу) - DATE
 - status (Статус) - STRING
 - timezone (Временная зона) - STRING
 - title (Должность) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - userid (Пользователь) - INTEGER
 - workspace_userid (ID) - INTEGER
*/

public class workspace_user_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Default Status To User
		//rule <sn>setDefaultStatusToUser</sn>
		if (/*if*/record.get("WORKSPACEID")!=null/*if*/) {
			//<body>
			if ((record.getAsInteger("WORKSPACEID")==51) || (record.getAsInteger("WORKSPACEID")==209)){
			    record.set("status", "REGISTERED");
			    record.set("emailstatus", "CONFIRMED");
			    record.set("loginname", record.getAsString("email"));
			}
			if (record.getAsInteger("WORKSPACEID")==49){
			    record.set("status", "REGISTERED");
			    record.set("emailstatus", "CONFIRMED");
			    //record.set("loginname", record.getAsString("email"));
			}
			//</body>
		}
		//endregion

		//region Deactivate User
		//rule <sn>021af785-2408-3d4c-4d70-0a96c64fbfcb</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Date now = new Date();
			
			if (!CommonUtils.isEmpty(record.get("fired_date")) 
			    && record.getAsDateTime("fired_date").before(now) 
			    && !record.getAsString("status").equalsIgnoreCase("DEACTIVATED")) {
			    record.set("isActive", false);
			    record.set("status", "DEACTIVATED");
			}
			//</body>
		}
		//endregion

		//region Check Login and Email is Unique
		//rule <sn>df3557b3-9407-e9ba-230b-0d9dcb4cd8e7</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Integer skipId = record.getId();
			
			if (record.get("loginname")!=null) {
			    String query = "status in ('INVITED','REGISTERED') and lower(loginname)=lower('" + record.get("loginname") + "')";
			    if (skipId != null) {
			        query += " and not userId=" + skipId;
			    }
			    Integer usCount = QueryUtils.getCount("workspace_user", query);
			    if (usCount > 0) {
			        throw new ValidationException("В Системе уже есть пользователь с указанным логином");
			    }
			}
			
			if (record.get("email")!=null) {
			    String query = "status in ('INVITED','REGISTERED') and lower(email)=lower('" + record.get("email") + "')";
			    if (skipId != null) {
			        query += " and not userId=" + skipId;
			    }
			    Integer usCount = QueryUtils.getCount("workspace_user", query);
			    if (usCount > 0) {
			        throw new ValidationException("В Системе уже есть пользователь с указанным Email");
			    }
			}
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Активировать пользователя, если у него стёрли дату увольнения
		//rule <sn>c5afb6f0-8ca7-ccc6-2014-8c62df3babe3</sn>
		if (/*if*/!record.getAsBooleanNullSub("isActive", false) && !CommonUtils.isEmpty(oldrecord.get("fired_date")) && CommonUtils.isEmpty(record.get("fired_date"))/*if*/) {
			//<body>
			record.set("isActive", true);
			record.set("status","UNREGISTERED");
			//</body>
		}
		//endregion

		//region Deactivate User
		//rule <sn>021af785-2408-3d4c-4d70-0a96c64fbfcb</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Date now = new Date();
			
			if (!CommonUtils.isEmpty(record.get("fired_date")) 
			    && record.getAsDateTime("fired_date").before(now) 
			    && !record.getAsString("status").equalsIgnoreCase("DEACTIVATED")) {
			    record.set("isActive", false);
			    record.set("status", "DEACTIVATED");
			}
			//</body>
		}
		//endregion

	}

}
