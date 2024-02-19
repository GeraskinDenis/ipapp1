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
DisplayName: Запрос на доработку
Fields:
 - assignedto (Ответственный) - INTEGER
 - backtoworkflag (Вовзращен на доработку) - BOOLEAN
 - closure_code (Код закрытия) - STRING
 - closure_time (Время закрытия) - DATETIME
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - description (Описание) - TEXT
 - displayname (Краткое описание) - STRING
 - execution_date (Дата исполнения) - DATETIME
 - impact (Влияние) - INTEGER
 - initiator (Инициатор) - INTEGER
 - isactive (Активно) - BOOLEAN
 - isinvisibleforsimpleuser (Скрывать специальные поля) - BOOLEAN
 - priority (Приоритет) - INTEGER
 - registration_time (Время регистрации) - DATETIME
 - sd_ticketid (Внутренний код) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - urgency (Срочность) - INTEGER
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class sd_ticket_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void onOpen(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Initiator to SD_TICKET
		//rule <sn>setInitiatorToSD_TICKET</sn>
		if (/*if*/record.get("INITIATOR") == null/*if*/) {
			//<body>
			record.set("INITIATOR", ContextUtils.getCurrentUserId());
			//</body>
		}
		//endregion

		//region Check SD Ticket Special Fields Visibility
		//rule <sn>CheckSDTicketSpecialFieldsVisibility</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if  (ContextUtils.getCurrentUser().isInAccessGroup(288) || ContextUtils.getCurrentUser().isInAccessGroup(289)) 
			     record.set("ISINVISIBLEFORSIMPLEUSER", false);
			else
			    record.set("ISINVISIBLEFORSIMPLEUSER", true);
			//</body>
		}
		//endregion

		//region Create SD_FOLLOWER Initiator Relation
		//rule <sn>createSDFollowerInitiatorRelation</sn>
		if (/*if*/(record.getAsInteger("INITIATOR")!=null) && (record.getKeyValue()!=null)/*if*/) {
			//<body>
			Lib.SDUtils.CreateSDFollowerInitiatorRelation(record);
			//</body>
		}
		//endregion

		//region Set REGISTRATION_TIME to SD_TICKET
		//rule <sn>SetRegistrationTimeToSD_TICKET</sn>
		if (/*if*/record.get("REGISTRATION_TIME") == null/*if*/) {
			//<body>
			Timestamp currentTime = new Timestamp(new Date().getTime());
			record.set("REGISTRATION_TIME", currentTime);
			//</body>
		}
		//endregion

		//region Create SD_FOLLOWER Assigned To Relation
		//rule <sn>createSDFollowerAssignedToRelation</sn>
		if (/*if*/(record.getAsInteger("ASSIGNEDTO")!=null) && (record.getKeyValue()!=null)/*if*/) {
			//<body>
			Lib.SDUtils.CreateSDFollowerAssignedToRelation(record);
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check ability to go next status
		//common rule <sn>checkGoToNextStatus</sn>
		//entities: sd_ticket, interaction
		if (/*if*/true/*if*/) {
			//<body>
			//log.info("попытка перевода в следующий статус");
			record.processTransitions();
			//</body>
		}
		//endregion

	}

}
