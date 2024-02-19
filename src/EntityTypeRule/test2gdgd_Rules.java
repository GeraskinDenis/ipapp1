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
DisplayName: test2GDGD
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
 - test2gdgdid (ID) - INTEGER
 - unnumber (Серийный номер) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Ответственная рабочая группа) - INTEGER
*/

public class test2gdgd_Rules {

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

		//region RSM Create CIParams
		//common rule <sn>ba885ffb-61a4-8af4-a68e-c5817eac303a</sn>
		//entities: server, informationsystem, skvazhina, software, printer, documentation, meeting_room, monitor, commonci, network_device, cartridge, computer, test2gdgd, citemplate, av_device, service, subnetwork, workspace_parts, workplace, mobile_device, studycitypetable
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.createParameters(record);
			//</body>
		}
		//endregion

		//region Update Relations on CI Update
		//common rule <sn>22c9df29-915e-2450-c010-fcfd9e84398a</sn>
		//entities: server, network_device, informationsystem, computer, test2gdgd, citemplate, av_device, service, printer, meeting_room, workplace, mobile_device
		if (/*if*/!CommonUtils.isEmpty(record.getAsInteger("citype"))/*if*/) {
			//<body>
			List<EntityDTO> relConfigList = QueryUtils.getRecordList("cirelation_config","citype="+record.get("citype"));
			for (EntityDTO relConfig:relConfigList){
			    if (!CommonUtils.isEmpty(relConfig.getAsString("related_citype->entitytype->tablename"))){
			        String fieldName = relConfig.getAsString("citype_tablename_field");
			        String tableName = relConfig.getAsString("related_citype->entitytype->tablename");
			        String relatonType = CommonUtils.nullSub(relConfig.getAsString("relation_type"),"parent_child");
			        //ContextUtils.addMessage("rule 998 tablename ="+tableName);
			        ru.ip.server.database.table.single.EntityField entityField = ru.ip.server.cache.CacheManager.getInstance().getEntityDescription(record.getTableName()).getEntityField(fieldName);
			        List<Integer> ciList = new ArrayList<Integer>();
			        List<Integer> oldCiList = new ArrayList<Integer>();
			        if (entityField != null && entityField.isArray()){
			            ciList = record.getIntegerArray(fieldName);
			            oldCiList = oldrecord.getIntegerArray(fieldName);
			            //ContextUtils.addMessage("rule 998 array old List Data = "+oldCiList);
			        }
			        else{
			            if (!CommonUtils.isEmpty(record.get(fieldName)))
			                ciList.add(record.getAsInteger(fieldName));
			            if (!CommonUtils.isEmpty(oldrecord.get(fieldName)))
			                oldCiList.add(oldrecord.getAsInteger(fieldName));
			        }
			        List<Integer> removeCiList = new ArrayList(oldCiList);
			        removeCiList.removeAll(ciList);
			        
			        List<Integer> addCiList = new ArrayList(ciList);
			        addCiList.removeAll(oldCiList);
			        
			        //ContextUtils.addMessage("addCiList = "+addCiList);
			        //ContextUtils.addMessage("removeCiList = "+removeCiList);
			        for (Integer ci:addCiList){    
			            EntityDTO relatedCi = QueryUtils.getRecordById(tableName,ci);
			            //ContextUtils.addMessage("add relatedCi = "+relatedCi.getId()+", tableName="+tableName+", ci="+ci+", relatedCi citype ="+relatedCi.getAsInteger("citype"));
			            if (!CommonUtils.isEmpty(relatedCi) && !CommonUtils.isEmpty(relatedCi.getAsInteger("citype"))){
			                //ContextUtils.addMessage("rule 999 before relation add");
			                Lib.RSMUtils.createCIRelation(relatedCi.getAsInteger("citype"), relatedCi.getId(),record.getAsInteger("citype"), record.getId(), relatonType);
			        
			            }    
			        }
			        
			
			        for (Integer ci:removeCiList){    
			            EntityDTO relatedCi = QueryUtils.getRecordById(tableName,ci);
			            //ContextUtils.addMessage("remove relatedCi = "+relatedCi.getId());
			            if (!CommonUtils.isEmpty(relatedCi))
			                Lib.RSMUtils.deleteCiRelation(record,relatedCi);
			        }
			    }
			}
			  
			//</body>
		}
		//endregion

	}

}
