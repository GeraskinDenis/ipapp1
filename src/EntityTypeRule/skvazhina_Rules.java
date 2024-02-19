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
DisplayName: Скважина
Fields:
 - citype (Тип КЕ) - INTEGER
 - citype_shortname (Тип КЕ) - STRING
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Название) - STRING
 - isactive (Активно) - BOOLEAN
 - server (Сервер) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - skvazhinaid (ID) - INTEGER
 - software (Установленное ПО) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class skvazhina_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void onOpen(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Fill citype from citype_shortname
		//common rule <sn>8f7c4fe1-df04-c827-d4e3-96320ae0e552</sn>
		//entities: server, informationsystem, skvazhina, software, printer, documentation, meeting_room, monitor, network_device, cartridge, computer, test2gdgd, citemplate, av_device, service, subnetwork, workspace_parts, workplace, mobile_device, studycitypetable
		if (/*if*/CommonUtils.isEmpty(record.get("citype"))/*if*/) {
			//<body>
			if (record.get("citype_shortname")!=null){ 
			    EntityDTO ciType = QueryUtils.getRecordByShortName("citype", record.getAsString("citype_shortname"));
			    if (!CommonUtils.isEmpty(ciType))
			        record.set("CITYPE",ciType.getId());
			}
			//</body>
		}
		//endregion

	}

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Fill citype from citype_shortname
		//common rule <sn>8f7c4fe1-df04-c827-d4e3-96320ae0e552</sn>
		//entities: server, informationsystem, skvazhina, software, printer, documentation, meeting_room, monitor, network_device, cartridge, computer, test2gdgd, citemplate, av_device, service, subnetwork, workspace_parts, workplace, mobile_device, studycitypetable
		if (/*if*/CommonUtils.isEmpty(record.get("citype"))/*if*/) {
			//<body>
			if (record.get("citype_shortname")!=null){ 
			    EntityDTO ciType = QueryUtils.getRecordByShortName("citype", record.getAsString("citype_shortname"));
			    if (!CommonUtils.isEmpty(ciType))
			        record.set("CITYPE",ciType.getId());
			}
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM Create CIParams
		//common rule <sn>ba885ffb-61a4-8af4-a68e-c5817eac303a</sn>
		//entities: server, informationsystem, skvazhina, software, printer, documentation, meeting_room, monitor, commonci, network_device, cartridge, computer, test2gdgd, citemplate, av_device, service, subnetwork, workspace_parts, workplace, mobile_device, studycitypetable
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.createParameters(record);
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM Create CIParams
		//common rule <sn>ba885ffb-61a4-8af4-a68e-c5817eac303a</sn>
		//entities: server, informationsystem, skvazhina, software, printer, documentation, meeting_room, monitor, commonci, network_device, cartridge, computer, test2gdgd, citemplate, av_device, service, subnetwork, workspace_parts, workplace, mobile_device, studycitypetable
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.createParameters(record);
			//</body>
		}
		//endregion

	}

}
