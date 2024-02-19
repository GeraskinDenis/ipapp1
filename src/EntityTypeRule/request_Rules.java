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

public class request_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set ITSM Request Default Initiator
		//rule <sn>db676eec-b0f8-9f64-0616-cd6d5f55ccbc</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isEmpty(record.get("initiator")))
			    record.set("initiator", ContextUtils.getCurrentWorkspaceUserId());
			
			//</body>
		}
		//endregion

		//region Set Workflow From Template
		//rule <sn>28d231c1-1545-f363-3b62-61f0a08c15af</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.get("template")!=null){
			    EntityDTO reqTempl = new EntityDTO("request_template", record.getAsInteger("template"));
			    if (reqTempl!=null){ 
			        if (reqTempl.get("workflow")!=null){
			            record.set("workflowid", reqTempl.getAsInteger("workflow"));
			            EntityDTO workflow = new EntityDTO("workflow", reqTempl.getAsInteger("workflow"));
			            if ((workflow!=null) && (workflow.get("firststepid")!=null)){
			                EntityDTO step = new EntityDTO("workflowstep", workflow.getAsInteger("firststepid"));
			                if (step!=null){
			                    record.set("workflowstepid",step.getKeyValue());
			                    record.set("workflowstepname",step.getAsString("internalname"));
			                }
			            }
			        }
			        if (CommonUtils.isEmpty(record.get("deadline"))){
			            Integer calendarId = null;
			            if (record.get("service")!=null){
			                EntityDTO service = new EntityDTO("service", record.getAsInteger("service"));
			                if ((service!=null) && (service.get("calendar")!=null))
			                    calendarId = service.getAsInteger("calendar");
			            } 
			            if (calendarId==null)
			                calendarId = Lib.TaskTemplateUtils.getWorkspaceCalendar(ContextUtils.getWorkspaceId()).getId();
			                
			            if (reqTempl.get("duration")!=null) 
			                record.set("deadline", Lib.TaskTemplateUtils.addWorkHoursToDate(new Date(), reqTempl.getAsInteger("duration"), calendarId));
			            else
			                record.set("deadline", Lib.TaskTemplateUtils.addWorkHoursToDate(new Date(),16,calendarId));
			        }        
			    }
			}
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Create Request Tasks From Template
		//rule <sn>0525cc0f-4ebf-3ebd-4444-3956655db522</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Lib.ITSMRequestUtils.createRequestTasksFromTemplate(record);
			//record.doUpdate();
			//</body>
		}
		//endregion

	}

}
