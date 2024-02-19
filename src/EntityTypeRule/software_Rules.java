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
DisplayName: Программное обеспечение
Fields:
 - citype (Тип КЕ - число) - INTEGER
 - citype_shortname (Тип КЕ) - STRING
 - code (Код поиска) - STRING
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - description (Описание) - TEXT
 - displayname (Наименование) - STRING
 - isactive (Активно) - BOOLEAN
 - location (Местонахождение) - INTEGER
 - logicalname (Логическое имя) - STRING
 - model (Модель) - STRING
 - model_int (Модель число) - INTEGER
 - owner (Сотрудник) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - softwareid (ID) - INTEGER
 - startdate (Дата ввода в эксплуатацию) - DATE
 - unnumber (Серийный номер) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Рабочая группа) - INTEGER
 - workplace (Рабочее место) - INTEGER
 - workplace_owner (Владелец рабочего места) - INTEGER, определение соединения: workplace->owner
*/

public class software_Rules {

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

		//region Create CI Params on creation
		//rule <sn>ec315e1a-3e9d-55b7-35e5-a0423224497d</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.get("citype")!=null){
			    List<EntityDTO> paramList = QueryUtils.getRecordList("citypeparams","citype =" +record.get("citype")+" and isactive = 1");
			    for (EntityDTO typeParam:paramList){
			    	EntityDTO newparam = new EntityDTO("ciparam");
			    	newparam.set("citype",typeParam.getAsInteger("citype"));
			    	newparam.set("displayname",typeParam.getAsString("displayname"));
			    	newparam.set("unitofmeasure",typeParam.getAsInteger("unitofmeasure"));
			        newparam.set("ciparam",typeParam.getKeyValue());
			    	newparam.set("ci",record.getKeyValue());
			    	if (typeParam.get("value_format")!=null)
			    	    newparam.set("value_format",typeParam.getAsInteger("value_format"));
			    	else
			    	    newparam.set("value_format",5);
			    	newparam.doInsert();
			    }
			}
			
			//</body>
		}
		//endregion

		//region Create RSM Relation software with workspace after integration
		//rule <sn>d72d3e99-9125-57a9-3bd1-705c8bd4f460</sn>
		if (/*if*/!record.isFieldEmpty("workplace") && !CommonUtils.isSame(record.getAsInteger("workplace"),oldrecord.getAsInteger("workplace"))/*if*/) {
			//<body>
			EntityDTO workplace = QueryUtils.getRecordById("workplace", record.getAsInteger("workplace"));
			List <Integer> list = workplace.getIntegerArray("software");
			if (!list.contains(record.getId())) {
			list.add(record.getId());
			workplace.set("software", list);
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

		//region Delete RSM Relation software with workspace after integration
		//rule <sn>267ebf6b-8258-a6d3-76fe-5257a6c0d326</sn>
		if (/*if*/!record.isFieldEmpty("workplace")/*if*/) {
			//<body>
			EntityDTO workplace = QueryUtils.getRecordById("workplace", record.getAsInteger("workplace"));
			List <Integer> list = workplace.getIntegerArray("software");
			if (list.contains(record.getId())) {
			list.remove(record.getId());
			workplace.set("software", list);
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
