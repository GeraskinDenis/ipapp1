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
DisplayName: Комплектующие для рабочего места
Fields:
 - citype (Тип КЕ - число) - INTEGER
 - citype_shortname (Тип) - STRING
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Название) - STRING
 - isactive (Активно) - BOOLEAN
 - lifeduration (Срок службы, лет) - DOUBLE, определение соединения: model->lifeduration
 - logicalname (Код поиска) - STRING
 - manufacturer (Производитель) - STRING
 - model (Модель) - STRING
 - owner (Сотрудник) - INTEGER
 - serialno (Серийный номер) - STRING
 - service (Услуга) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - startdate (Дата ввода в эксплуатацию) - DATE
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
 - workplace (Рабочее место) - INTEGER
 - workplace_owner (Владелец рабочего места) - INTEGER, определение соединения: workplace->owner
 - workspace_partsid (ID) - INTEGER
*/

public class workspace_parts_Rules {

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

		//region Set Owner from workplace on update
		//common rule <sn>fdb59ca4-a2b3-eaef-4150-1a675b875926</sn>
		//entities: computer, av_device, monitor, workspace_parts
		if (/*if*/!CommonUtils.isEmpty(record.get("workplace")) && (CommonUtils.isEmpty(record.get("owner")) || !CommonUtils.isSame(record.get("workplace"), oldrecord.get("workplace")))/*if*/) {
			//<body>
			EntityDTO workPlace = QueryUtils.getRecordById("workplace",record.getAsInteger("workplace"));
			if (!CommonUtils.isEmpty(workPlace)){
			    record.set("owner",workPlace.getAsInteger("owner"));
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

		//region Create RSM Relation workspace_parts with workspace after integration
		//rule <sn>3c28f2fa-293a-c605-bbb7-0e0f9bbff235</sn>
		if (/*if*/!record.isFieldEmpty("workplace") && !CommonUtils.isSame(record.getAsInteger("workplace"),oldrecord.getAsInteger("workplace"))/*if*/) {
			//<body>
			EntityDTO workplace = QueryUtils.getRecordById("workplace", record.getAsInteger("workplace"));
			List <Integer> list = workplace.getIntegerArray("parts");
			if (!list.contains(record.getId())) {
			list.add(record.getId());
			workplace.set("parts", list);
			workplace.doUpdate();
			}
			//</body>
		}
		//endregion

		//region Kafka Data Sync - After Create
		//common rule <sn>77c7c793-b3b0-4310-8191-77e08aeb3c83</sn>
		//entities: interaction_template, server, software, printer, monitor, citype, network_device, computer, av_device, service, subnetwork, interaction, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			KafkaUtils.recordCreateDataForKafka(record);
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

		//region Set Owner from workplace on update
		//common rule <sn>fdb59ca4-a2b3-eaef-4150-1a675b875926</sn>
		//entities: computer, av_device, monitor, workspace_parts
		if (/*if*/!CommonUtils.isEmpty(record.get("workplace")) && (CommonUtils.isEmpty(record.get("owner")) || !CommonUtils.isSame(record.get("workplace"), oldrecord.get("workplace")))/*if*/) {
			//<body>
			EntityDTO workPlace = QueryUtils.getRecordById("workplace",record.getAsInteger("workplace"));
			if (!CommonUtils.isEmpty(workPlace)){
			    record.set("owner",workPlace.getAsInteger("owner"));
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

		//region Kafka Data Sync - After Update
		//common rule <sn>5877a964-65c9-538c-116a-983026feb9d0</sn>
		//entities: interaction_template, server, software, printer, monitor, citype, network_device, computer, av_device, service, subnetwork, interaction, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			KafkaUtils.recordUpdateDataForKafka(record, oldrecord);
			//</body>
		}
		//endregion

	}

	public static void beforeDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Delete RSM Relation workspace_parts with workspace after integration
		//rule <sn>641edea5-1084-e458-d14c-b6a054367d13</sn>
		if (/*if*/!record.isFieldEmpty("workplace")/*if*/) {
			//<body>
			EntityDTO workplace = QueryUtils.getRecordById("workplace", record.getAsInteger("workplace"));
			List <Integer> list = workplace.getIntegerArray("parts");
			if (list.contains(record.getId())) {
			list.remove(record.getId());
			workplace.set("parts", list);
			workplace.doUpdate();
			}
			//</body>
		}
		//endregion

	}

	public static void afterDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Kafka Data Sync - After Delete
		//common rule <sn>70eba929-b998-ce6d-6a29-79fcbb5e6ed1</sn>
		//entities: interaction_template, server, software, printer, monitor, citype, network_device, computer, av_device, service, subnetwork, interaction, workspace_parts, workplace, mobile_device
		if (/*if*/true/*if*/) {
			//<body>
			KafkaUtils.recordDeleteDataForKafka(oldrecord);
			//</body>
		}
		//endregion

	}

}
