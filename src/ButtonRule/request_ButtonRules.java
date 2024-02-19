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
DisplayName: Стандартный запрос
Fields:
 - coordinator (Координатор) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - deadline (Дата исполнения) - DATETIME
 - description (Описание) - TEXT
 - displayname (Наименование) - STRING
 - initiator (Инициатор) - INTEGER
 - interaction (Заявка) - INTEGER
 - is_need_approve (Требует согласования) - BOOLEAN, определение соединения: template->is_need_approve
 - isactive (Активно) - BOOLEAN
 - location (Местоположение) - INTEGER
 - requested_date (Желаемая дата) - DATE
 - requestid (ID) - INTEGER
 - service (Услуга) - INTEGER
 - service_citype (Типовая услуга) - STRING
 - shortname (Внутреннее имя) - STRING
 - template (Шаблон) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага  WF) - STRING
*/

public class request_ButtonRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Согласовать
	//button <sn>1f73f566-84bb-31a4-a95c-8064e692dcd0</sn>
	public static void itsm_approve_1120_onSearch_list(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(ContextUtils.getCurrentDisplayViewName(),"request_wating_approve"))) return;

		//region Mass Approve Action
		//common rule <sn>bb13183e-b14d-b0f2-5af8-376ec7e0b7fa</sn>
		//entities: request, interaction
		if (/*if*/true/*if*/) {
			//<body>
			//record.setWorkflowStepId(307); 
			//record.doUpdate();
			ContextUtils.addMessage("mass approve interactionid="+record.getId());
			record.processTransition("itsm_interaction_approve2");
			//</body>
		}
		//endregion

	}

	//Отклонить
	//button <sn>7cf1c63e-b22a-f431-e988-cc1741865748</sn>
	public static void itsm_decline_1121_onSearch_list(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(CommonUtils.isSame(ContextUtils.getCurrentDisplayViewName(),"request_wating_approve"))) return;

		//region Mass Decline Action
		//common rule <sn>bb123f59-cef0-57d0-90b7-9102808daa01</sn>
		//entities: request, interaction
		if (/*if*/true/*if*/) {
			//<body>
			//record.setWorkflowStepId(301); 
			//record.doUpdate();
			String declineCause = CastUtils.getStringValue(updateFields.get("p_decline_cause"));
			record.set("decline_cause",declineCause);
			record.processTransition("itsm_interaction_decline");
			//</body>
		}
		//endregion

	}

}
