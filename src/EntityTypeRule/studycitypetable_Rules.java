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
DisplayName: Учебная Таблица ТипаКЕ
Fields:
 - citype (Тип КЕ) - INTEGER
 - citype_shortname (Тип КЕ - строка) - STRING
 - code (Код поиска) - STRING
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - description (Описание) - TEXT
 - displayname (Наименование) - STRING
 - isactive (Активно) - BOOLEAN
 - location (Местонахождение) - INTEGER
 - owner (Сотрудник) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - startdate (Дата ввода в эксплуатацию) - DATE
 - studycitypetableid (ID) - INTEGER
 - unnumber (Серийный номер) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Ответственная рабочая группа) - INTEGER
*/

public class studycitypetable_Rules {

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

		//region Create ci Relation
		//common rule <sn>1359c15d-ddbd-9492-d4fc-d0f8faa5d2e2</sn>
		//entities: cartridge, test2gdgd, citemplate, subnetwork, monitor, workspace_parts, studycitypetable
		if (/*if*/true/*if*/) {
			//<body>
			if (!CommonUtils.isEmpty(record.getAsInteger("citype"))){
			    List<EntityDTO> relConfigList = QueryUtils.getRecordList("cirelation_config","citype="+record.get("citype"));
			    for (EntityDTO relConfig:relConfigList){
			        if (!CommonUtils.isEmpty(relConfig.getAsString("related_citype->tablename")) && !CommonUtils.isEmpty(relConfig.getAsString("citype_tablename_field"))){
			            String fieldName = relConfig.getAsString("citype_tablename_field");
			            EntityDTO relatedCi = QueryUtils.getRecordById(relConfig.getAsString("related_citype->tablename"),record.getAsInteger(fieldName));
			            if (!CommonUtils.isEmpty(relatedCi) && !CommonUtils.isEmpty(relatedCi.getAsInteger("citype"))){
			                String relatonType = CommonUtils.nullSub(relConfig.getAsString("relation_type"),"parent_child");
			                EntityDTO relation = Lib.RSMUtils.createCIRelation(relatedCi.getAsInteger("citype"), relatedCi.getId(),record.getAsInteger("citype"), record.getId(), relatonType);
			            }
			        }
			    }
			}
			//</body>
		}
		//endregion

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

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Update CI Relation
		//common rule <sn>1979c119-47a2-388b-7556-65a672d696c9</sn>
		//entities: cartridge, subnetwork, monitor, workspace_parts, studycitypetable
		if (/*if*/!CommonUtils.isEmpty(record.getAsInteger("citype"))/*if*/) {
			//<body>
			List<EntityDTO> relConfigList = QueryUtils.getRecordList("cirelation_config","citype="+record.get("citype"));
			for (EntityDTO relConfig:relConfigList){
			    if (!CommonUtils.isEmpty(relConfig.getAsString("related_citype->tablename")) && !CommonUtils.isEmpty(relConfig.getAsString("citype_tablename_field"))){
			        String fieldName = relConfig.getAsString("citype_tablename_field");
			        String tableName = relConfig.getAsString("related_citype->tablename");
			        String relatonType = CommonUtils.nullSub(relConfig.getAsString("relation_type"),"parent_child");
			        if (!CommonUtils.isSame(record.getAsInteger(fieldName),oldrecord.getAsInteger(fieldName))){
			            ContextUtils.addMessage("995 field changed, tableName="+tableName+", fieldName="+fieldName);
			            if (!CommonUtils.isEmpty(oldrecord.getAsInteger(fieldName))){
			                EntityDTO oldService = QueryUtils.getRecordById(tableName, oldrecord.getAsInteger(fieldName));
			                if (!CommonUtils.isEmpty(oldService))
			                    Lib.RSMUtils.deleteCiRelation(oldService, record);
			            }  
			            if (!CommonUtils.isEmpty(record.getAsInteger(fieldName)) && !CommonUtils.isEmpty(record.getAsInteger("citype"))){
			                EntityDTO service = QueryUtils.getRecordById(tableName,record.getAsInteger(fieldName));
			                if (!CommonUtils.isEmpty(service) && !CommonUtils.isEmpty(service.getAsInteger("citype")))
			                    Lib.RSMUtils.createCIRelation(service.getAsInteger("citype"), service.getId(),record.getAsInteger("citype"), record.getId(), relatonType);
			            }
			        }
			    }
			}
			  
			
			
			//</body>
		}
		//endregion

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
