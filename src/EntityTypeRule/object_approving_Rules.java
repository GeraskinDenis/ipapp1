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
DisplayName: Согласование
Fields:
 - approve_type (Тип согласования) - STRING
 - approver (Согласующий) - INTEGER
 - approver_format (Формат согласующего) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Название) - STRING
 - isactive (Активно) - BOOLEAN
 - object (Объект) - INTEGER
 - object_approvingid (ID) - INTEGER
 - object_type (Тип объекта) - STRING
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class object_approving_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void onOpen(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Object Approving Format
		//rule <sn>4d38ce85-c446-7f77-32ea-07c93bbd59e0</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isSame(record.getAsString("approve_type"),"AccessGroup")){
			    EntityDTO format = QueryUtils.getRecordByShortName("fieldformat","AccessGroup");
			    if (!CommonUtils.isEmpty(format))
			        record.set("approver_format",format.getId());
			}
			else{
			    EntityDTO format = QueryUtils.getRecordByShortName("fieldformat","bf88622e-a1dc-6157-a9ad-30f9041159ca");
			    if (!CommonUtils.isEmpty(format))
			        record.set("approver_format",format.getId());
			}
			
			//</body>
		}
		//endregion

	}

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Object Approving Format
		//rule <sn>4d38ce85-c446-7f77-32ea-07c93bbd59e0</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isSame(record.getAsString("approve_type"),"AccessGroup")){
			    EntityDTO format = QueryUtils.getRecordByShortName("fieldformat","AccessGroup");
			    if (!CommonUtils.isEmpty(format))
			        record.set("approver_format",format.getId());
			}
			else{
			    EntityDTO format = QueryUtils.getRecordByShortName("fieldformat","bf88622e-a1dc-6157-a9ad-30f9041159ca");
			    if (!CommonUtils.isEmpty(format))
			        record.set("approver_format",format.getId());
			}
			
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set Object Approving Format
		//rule <sn>4d38ce85-c446-7f77-32ea-07c93bbd59e0</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isSame(record.getAsString("approve_type"),"AccessGroup")){
			    EntityDTO format = QueryUtils.getRecordByShortName("fieldformat","AccessGroup");
			    if (!CommonUtils.isEmpty(format))
			        record.set("approver_format",format.getId());
			}
			else{
			    EntityDTO format = QueryUtils.getRecordByShortName("fieldformat","bf88622e-a1dc-6157-a9ad-30f9041159ca");
			    if (!CommonUtils.isEmpty(format))
			        record.set("approver_format",format.getId());
			}
			
			//</body>
		}
		//endregion

	}

}
