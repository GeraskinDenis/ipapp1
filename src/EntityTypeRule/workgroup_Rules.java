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
DisplayName: Рабочая группа
Fields:
 - access_group (Группа доступа) - INTEGER
 - calendar (Календарь) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - email (Email) - STRING
 - is_parentorgunit (Является родительской группой) - BOOLEAN
 - isactive (Активно) - BOOLEAN
 - link_to_scheduler (Расписание РГ) - TEXT
 - location (Местонахождение) - INTEGER
 - manager (Руководитель группы) - INTEGER
 - mobilephone (Телефон) - STRING
 - parent_orgunit (Родительская группа) - INTEGER
 - responsible_zone (Зона ответственности) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workgroupid (ID) - INTEGER
 - workgrouptype (Тип РГ) - STRING
 - workspace (Пространство группы) - INTEGER
*/

public class workgroup_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Создание связанной группы доступа
		//rule <sn>234e374d-5eb1-7e67-b1e1-823a4a1928a7</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isSame(record.getAsBoolean("isactive"), true)){
			    Lib.WorkgroupUtils.createAccessGroup(record);
			}
			//</body>
		}
		//endregion

		//region Обновить запись
		//rule <sn>6013c9a0-0cce-b8c8-01bf-ddc446ae8f0a</sn>
		if (/*if*/true/*if*/) {
			//<body>
			record.doUpdate();
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Удаление связанной группы доступа при деактивации
		//rule <sn>82cad32a-03a0-0e9a-9ac7-1b6f041684ff</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isSame(oldrecord.getAsBoolean("isactive"), true) && CommonUtils.isSame(record.getAsBoolean("isactive"), false)) {
			Lib.WorkgroupUtils.removeAccessGroup(record);
			record.set("access_group", null);
			}
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Создание связанной группы доступа при активации
		//rule <sn>0a620dae-3ae0-6f8a-2741-0a0b05abf726</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isSame(oldrecord.getAsBoolean("isactive"), false) && CommonUtils.isSame(record.getAsBoolean("isactive"),true)){
			    Lib.WorkgroupUtils.createAccessGroup(record);
			    record.doUpdate();
			}
			//</body>
		}
		//endregion

	}

	public static void beforeDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Удаление связанной группы доступа
		//rule <sn>d98e2ed5-2aef-d16a-bfa8-df2aeb2aad4a</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Lib.WorkgroupUtils.removeAccessGroup(record);
			//</body>
		}
		//endregion

	}

}
