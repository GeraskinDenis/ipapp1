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
DisplayName: Выполнение планирования окна
Fields:
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - entityid (Объект) - INTEGER
 - entitytype (Тип объекта) - STRING
 - hostname (Hostname) - STRING
 - isactive (Активно) - BOOLEAN
 - model_stop_flag (Необходимо остановить модель) - BOOLEAN
 - progress (Прогресс) - INTEGER
 - result_message (Результат выполнения) - STRING
 - shortname (Внутреннее имя) - STRING
 - status (Статус) - STRING
 - threadid (Номер потока Java) - INTEGER
 - threadstatus (Thread Status) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - work_schedule_statusid (ID) - INTEGER
*/

public class work_schedule_status_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Вывести прогресс и статус в лог
		//rule <sn>57aa1edf-86cf-1bac-8d93-1f2a779e46d3</sn>
		if (/*if*/!record.isFieldEmpty("entitytype") && !record.isFieldEmpty("entityid") && !record.isFieldEmpty("progress")/*if*/) {
			//<body>
			log.info(record.getAsString("entitytype") + "#" + record.getAsInteger("entityid")
			    + ": progress: " + record.getAsInteger("progress") + ", status: " + record.get("status"));
			//</body>
		}
		//endregion

	}

}
