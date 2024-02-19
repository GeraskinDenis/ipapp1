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
DisplayName: КЕ (общая)
Fields:
 - commonciid (ID) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Название) - STRING
 - isactive (Активно) - BOOLEAN
 - logicalname (Код поиска) - STRING
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class commonci_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Генерация кода поиска + Проверка на уникальность
		//common rule <sn>b810a162-14e4-2277-428b-6dfeb62a984b</sn>
		//entities: server, informationsystem, software, printer, documentation, meeting_room, monitor, commonci, network_device, cartridge, computer, av_device, service, subnetwork, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO ciType = QueryUtils.getRecord("citype", "citypeid = "+record.getAsInteger("citype"));
			
			if (!CommonUtils.isEmpty(ciType) && CommonUtils.isSame(ciType.get("is_uniquefields"),true)){
			
			String logicalname = "";
			String dash = "-";
			
			String prefix = ciType.getAsString("prefix");
			
			logicalname = logicalname + prefix;
			
			List < EntityDTO > uniqueFields = QueryUtils.getRecordList("citype_uniquefields", "citype = " + record.get("citype"), "sort_order");
			if (uniqueFields.size() > 0) {
			  for (EntityDTO field: uniqueFields) {
			    logicalname = logicalname + dash + record.getDisplayValue(field.getAsString("entityfield->fieldname"));
			  }
			}
			
			String tableId = ciType.getAsString("entitytype->tablename") + "id";
			
			EntityDTO commonLogicalName = QueryUtils.select().from(record.getTableName())
			                .where("logicalname =:_logicalname" + (record.getId() == null ? " " : " and " + tableId + " <> " + record.getId()))
			                .bind("_logicalname", logicalname)
			                .getRecord();
			
			if (!CommonUtils.isEmpty(commonLogicalName)) {
			  throw new Exception("Ошибка уникальности кода поиска. Измените значение уникальных атрибутов.");
			} else {
			  record.set("logicalname", logicalname);
			}
			} else {
			    throw new Exception("Ошибка генерации кода поиска. Убедитесь, что код поиска для данного типа КЕ сформирован.");
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

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Генерация кода поиска + Проверка на уникальность
		//common rule <sn>b810a162-14e4-2277-428b-6dfeb62a984b</sn>
		//entities: server, informationsystem, software, printer, documentation, meeting_room, monitor, commonci, network_device, cartridge, computer, av_device, service, subnetwork, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO ciType = QueryUtils.getRecord("citype", "citypeid = "+record.getAsInteger("citype"));
			
			if (!CommonUtils.isEmpty(ciType) && CommonUtils.isSame(ciType.get("is_uniquefields"),true)){
			
			String logicalname = "";
			String dash = "-";
			
			String prefix = ciType.getAsString("prefix");
			
			logicalname = logicalname + prefix;
			
			List < EntityDTO > uniqueFields = QueryUtils.getRecordList("citype_uniquefields", "citype = " + record.get("citype"), "sort_order");
			if (uniqueFields.size() > 0) {
			  for (EntityDTO field: uniqueFields) {
			    logicalname = logicalname + dash + record.getDisplayValue(field.getAsString("entityfield->fieldname"));
			  }
			}
			
			String tableId = ciType.getAsString("entitytype->tablename") + "id";
			
			EntityDTO commonLogicalName = QueryUtils.select().from(record.getTableName())
			                .where("logicalname =:_logicalname" + (record.getId() == null ? " " : " and " + tableId + " <> " + record.getId()))
			                .bind("_logicalname", logicalname)
			                .getRecord();
			
			if (!CommonUtils.isEmpty(commonLogicalName)) {
			  throw new Exception("Ошибка уникальности кода поиска. Измените значение уникальных атрибутов.");
			} else {
			  record.set("logicalname", logicalname);
			}
			} else {
			    throw new Exception("Ошибка генерации кода поиска. Убедитесь, что код поиска для данного типа КЕ сформирован.");
			}
			//</body>
		}
		//endregion

	}

}
