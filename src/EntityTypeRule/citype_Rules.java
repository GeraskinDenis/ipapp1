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
DisplayName: Типы ресурсов
Fields:
 - activeitemsquery (Запрос для выборки активных КЕ) - TEXT
 - citype_uniquefieldsline (Поля кода поиска КЕ) - STRING
 - citypeid (ID) - INTEGER
 - color (Цвет) - STRING
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - description (Описание) - TEXT
 - displayname (Наименование) - STRING
 - entitytype (Таблица) - INTEGER
 - fieldformat_integer (Формат для выбора данных) - INTEGER
 - icon (Изображение) - STRING
 - is_uniquefields (Код поиска сформирован) - BOOLEAN
 - isactive (Активно) - BOOLEAN
 - level (Уровень) - STRING
 - parent_citype (Родительский тип) - INTEGER
 - prefix (Префикс) - STRING
 - row_order (Порядковый номер) - INTEGER
 - shortname (Внутреннее наименование) - STRING
 - tablename (Системное наименование) - STRING
 - unitofmeasure (Единицы измерения) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
*/

public class citype_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Проверка на уникальность полей кода поиска типа КЕ
		//rule <sn>aba19249-48b0-e8fa-3911-c42364ea9b95</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO commonPrefix = QueryUtils.getRecord("citype", "prefix ='"+record.getAsString("prefix")+"'"+(record.getId()==null ? " " : " and citypeid <> " + record.getId()));
			if (!CommonUtils.isEmpty(commonPrefix)) {
			  throw new Exception("Ошибка уникальности префикса кода поиска");
			}
			
			String sqlQuery = "SELECT COUNT(*) FROM fkv39o1z.v_citype citype WHERE citype.citype_uniquefieldsline=:_citype_uniquefieldsline AND citype.prefix =:_prefix group by citype.citype_uniquefieldsline HAVING COUNT(*) > 1";
			
			if (!CommonUtils.isEmpty(record.get("citype_uniquefieldsline")) && !CommonUtils.isEmpty(record.get("prefix"))){ 
			    List<Integer> result = QueryUtils.selectSql(sqlQuery)
			                                            .bind("_citype_uniquefieldsline", record.getAsString("citype_uniquefieldsline"))
			                                            .bind("_prefix", record.getAsString("prefix"))
			                                            .getIntegerList();
			                                            
			    if (result.size() > 0 && result.get(0) > 1) {
			        throw new Exception("Ошибка уникальности полей кода поиска");
			    }
			}
			
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Add Health CITypeParam by CIType
		//rule <sn>1de2be58-126a-5bc2-205f-3397f0a122c0</sn>
		if (/*if*/true/*if*/) {
			//<body>
			// Проверяем, что такого параметра нет
			Boolean isExists = QueryUtils.select().from("citypeparams")
			                .where("ciType=:_ci_type_id and paramName=:_ci_param_name")
			                .bind("_ci_type_id", record.getId())
			                .bind("_ci_param_name", "health")
			                .getCount() > 0;
			
			if (!isExists){
			    
			    EntityDTO hParam = new EntityDTO("citypeparams");
			
			    hParam.set("displayName", "Состояние здоровья (Создано автоматически, Необходимо заполнить обязательные поля!)");
			    hParam.set("ciType", record.getId());
			    hParam.set("paramName", "health");
			    hParam.set("calculationType", "MANUAL");
			    hParam.set("sortOrder", 1);
			    
			    hParam.set("value_format", 2);
			        
			    hParam.doInsert();
			    
			    ContextUtils.addMessage("Для завершения регистрации типа КЕ задайте параметры КЕ и их показатели здоровья");
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

		//region Заполнение поля кода поиска КЕ (строка)
		//rule <sn>800a6ba7-0f84-67b2-142e-1b9c828d7b5e</sn>
		if (/*if*/true/*if*/) {
			//<body>
			List <EntityDTO> uniqueFields = QueryUtils.getRecordList("citype_uniquefields", "citype ="+record.getId(), "sort_order");
			String value = "";
			
			if (uniqueFields.size() > 0) {
			for (EntityDTO uField : uniqueFields) {
			    value = value + uField.getAsString("entityfield->fieldname") +",";
			}
			
			value = value.replaceAll(",$", "");
			record.set("citype_uniquefieldsline", value);
			}
			//</body>
		}
		//endregion

		//region Проверка на уникальность полей кода поиска типа КЕ
		//rule <sn>aba19249-48b0-e8fa-3911-c42364ea9b95</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO commonPrefix = QueryUtils.getRecord("citype", "prefix ='"+record.getAsString("prefix")+"'"+(record.getId()==null ? " " : " and citypeid <> " + record.getId()));
			if (!CommonUtils.isEmpty(commonPrefix)) {
			  throw new Exception("Ошибка уникальности префикса кода поиска");
			}
			
			String sqlQuery = "SELECT COUNT(*) FROM fkv39o1z.v_citype citype WHERE citype.citype_uniquefieldsline=:_citype_uniquefieldsline AND citype.prefix =:_prefix group by citype.citype_uniquefieldsline HAVING COUNT(*) > 1";
			
			if (!CommonUtils.isEmpty(record.get("citype_uniquefieldsline")) && !CommonUtils.isEmpty(record.get("prefix"))){ 
			    List<Integer> result = QueryUtils.selectSql(sqlQuery)
			                                            .bind("_citype_uniquefieldsline", record.getAsString("citype_uniquefieldsline"))
			                                            .bind("_prefix", record.getAsString("prefix"))
			                                            .getIntegerList();
			                                            
			    if (result.size() > 0 && result.get(0) > 1) {
			        throw new Exception("Ошибка уникальности полей кода поиска");
			    }
			}
			
			//</body>
		}
		//endregion

		//region После обновление префикса обновить признак кода поиска КЕ
		//rule <sn>184ac4e8-9069-a99a-9967-bcad12cb0c14</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (CommonUtils.isEmpty(oldrecord.get("prefix")) || !CommonUtils.isSame(oldrecord.getAsString("prefix"), record.getAsString("prefix"))) {
			    ContextUtils.addMessage("Префикс обновлён. Не забудьте пересчитать код поиска КЕ.", "WARNING");
			    record.set("is_uniquefields", false);
			}
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

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
