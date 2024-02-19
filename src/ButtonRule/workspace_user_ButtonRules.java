package ButtonRule;

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

public class workspace_user_ButtonRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Сбросить Пароль
	//button <sn>802b1cc0-53bf-54af-be7d-26c84080222c</sn>
	public static void sendResorePasswordEmail_1273_onEdit(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(Arrays.asList("Administrator","WORKSPACE_OWNER").contains(ContextUtils.getCurrentUser().getRoleShortName())  && Arrays.asList("REGISTERED","PREREGISTERED","INVITED").contains(record.getAsString("status")) )) return;

		//region sendRestorePasswordEmail
		//rule <sn>b7f29a74-1d78-9103-f7dd-6f491a164ac6</sn>
		if (/*if*/true/*if*/) {
			//<body>
			UserUtils.processRestorePasswordRequest(QueryUtils.getRecordById("user", record.getAsInteger("userid")));
			ContextUtils.addMessage("Пользователю отправлено письмо со ссылкой для сброса пароля");
			//</body>
		}
		//endregion

	}

	//Предоставить доступ
	//button <sn>7f35af60-2594-007d-0ca1-b827c82bee86</sn>
	public static void grantUserAccess_1274_onEdit(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(("UNREGISTERED".equalsIgnoreCase(record.getAsString("status")) || "DEACTIVATED".equalsIgnoreCase(record.getAsString("status"))) && record.getAsBoolean("isactive") && Arrays.asList("Administrator","WORKSPACE_OWNER").contains(ContextUtils.getCurrentUser().getRoleShortName()))) return;

		//region Invite User
		//rule <sn>62334a97-c34d-e454-938b-b81a825b8cc9</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.set("status","INVITED");
			record.doUpdate();
			
			ContextUtils.addMessage("Информация о доступе в систему отправлена пользователю на электронную почту");
			//</body>
		}
		//endregion

	}

}
