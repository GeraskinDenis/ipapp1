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
DisplayName: Переговорная
Fields:
 - av_devices (Аудио-видео устройства) - INTEGER
 - citype (Тип КЕ - число) - INTEGER
 - citype_shortname (Тип) - STRING
 - computers (Компьютеры) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Название) - STRING
 - employee_count (Максимальное количество сотрудников) - INTEGER
 - isactive (Активно) - BOOLEAN
 - lifeduration (Срок службы, лет) - DOUBLE, определение соединения: model->lifeduration
 - location (Расположение) - INTEGER
 - logicalname (Код поиска) - STRING
 - meeting_roomid (ID) - INTEGER
 - model (Модель) - STRING
 - monitors (Мониторы) - INTEGER
 - owner (Ответственный) - INTEGER
 - parts (Комплектующие) - INTEGER
 - service (Услуга) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - software (ПО для видео-конференц связи) - INTEGER
 - startdate (Дата ввода в эксплуатацию) - DATE
 - unnumber (Уникальный номер) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class meeting_room_Rules {

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

		//region Create CI Relation
		//common rule <sn>11eaadfa-d4c1-68ad-a6ae-484ada536515</sn>
		//entities: server, network_device, informationsystem, computer, av_device, service, printer, meeting_room, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			if (!CommonUtils.isEmpty(record.getAsInteger("citype"))){
			    List<EntityDTO> relConfigList = QueryUtils.getRecordList("cirelation_config","citype="+record.get("citype"));
			    //ContextUtils.addMessage("rule 998 relConfig size="+relConfigList.size());
			    for (EntityDTO relConfig:relConfigList){
			        //ContextUtils.addMessage("rule 998 relConfig="+relConfig);
			        if (!CommonUtils.isEmpty(relConfig.getAsString("related_citype->entitytype->tablename")) && !CommonUtils.isEmpty(relConfig.getAsString("citype_tablename_field"))){
			            String fieldName = relConfig.getAsString("citype_tablename_field");
			            String tableName = relConfig.getAsString("related_citype->entitytype->tablename");
			            //ContextUtils.addMessage("rule 998 tableName="+tableName);
			            //ContextUtils.addMessage("rule 998 fieldName="+fieldName);
			            if (!CommonUtils.isEmpty(record.get(fieldName))){
			                ru.ip.server.database.table.single.EntityField entityField = ru.ip.server.cache.CacheManager.getInstance().getEntityDescription(record.getTableName()).getEntityField(fieldName);
			    		    List<Integer> ciList = new ArrayList<Integer>();
			    		    if (entityField != null && entityField.isArray()){
			                    ciList = record.getIntegerArray(fieldName);
			                    //ContextUtils.addMessage("rule 998 array Data = "+record.getIntegerArray(fieldName));
			    		    }
			                else
			                    ciList.add(record.getAsInteger(fieldName));
			                //ContextUtils.addMessage("rule 998 ciList="+ciList);
			                String relatonType = CommonUtils.nullSub(relConfig.getAsString("relation_type"),"parent_child");
			                for (Integer ci:ciList){    
			                    EntityDTO relatedCi = QueryUtils.getRecordById(tableName,ci);
			                    if (!CommonUtils.isEmpty(relatedCi) && !CommonUtils.isEmpty(relatedCi.getAsInteger("citype")))
			                        Lib.RSMUtils.createCIRelation(relatedCi.getAsInteger("citype"), relatedCi.getId(),record.getAsInteger("citype"), record.getId(), relatonType);
			                }
			            }
			        }
			    }
			}
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
