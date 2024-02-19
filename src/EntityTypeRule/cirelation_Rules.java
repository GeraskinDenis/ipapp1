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
DisplayName: Связи КЕ
Fields:
 - ci1 (КЕ1) - INTEGER
 - ci1_format (Формат КЕ1) - INTEGER
 - ci2 (КЕ2) - INTEGER
 - ci2_format (Формат КЕ1) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - cirelationid (ID) - INTEGER
 - citype1 (Тип КЕ1) - INTEGER
 - citype2 (Тип КЕ2) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - isactive (Активно) - BOOLEAN
 - relation_type (Тип связи) - STRING
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
*/

public class cirelation_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set CI1 format for cirelation
		//rule <sn>20412a54-4670-5937-0ea4-8bd43ddea5e3</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("ci1_format")) && !CommonUtils.isEmpty(record.get("citype1")) /*if*/) {
			//<body>
			EntityDTO ciType1 = QueryUtils.getRecordById("citype", record.getAsInteger("citype1"));
			if ((ciType1 != null) && (ciType1.get("fieldformat_integer") != null)){
			    record.set("ci1_format", ciType1.get("fieldformat_integer"));
			    //log.info('ci1_format='+record.get("ci_format"));
			}
			else 
			    record.set("ci1_format", 1);
			
			
			
			//</body>
		}
		//endregion

		//region Set CI2 format for cirelation
		//rule <sn>9b93134f-4ec3-e5dd-cad4-d5df91142c6e</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("ci2_format")) && !CommonUtils.isEmpty(record.get("citype2")) /*if*/) {
			//<body>
			EntityDTO ciType2 = QueryUtils.getRecordById("citype", record.getAsInteger("citype2"));
			if ((ciType2 != null) && (ciType2.get("fieldformat_integer") != null)){
			    record.set("ci2_format", ciType2.getAsInteger("fieldformat_integer"));
			    //log.info('ci1_format='+record.get("ci_format"));
			}
			else 
			    record.set("ci2_format", 1);
			
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM CIRelation Update CI Params
		//rule <sn>0cd2b7a6-7add-d3b3-b5d0-ce268ec35613</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.getAsInteger("citype1") != null && record.getAsInteger("ci1") != null){
			    RSMUtils.updateParameters(record.getAsInteger("citype1"), record.getAsInteger("ci1"));
			}
			
			
			//</body>
		}
		//endregion

		//region Update CI1 in Object
		//rule <sn>3e212d06-6289-49f6-40ae-f6835b3d3204</sn>
		if (/*if*/!CommonUtils.isEmpty(record.get("ci1")) && CommonUtils.isSame(record.getAsString("citype1->entitytype->tablename"),"workplace") && !CommonUtils.isEmpty(record.get("citype2")) && !CommonUtils.isEmpty(record.get("ci2"))/*if*/) {
			//<body>
			String tableName =record.getAsString("citype2->entitytype->tablename");
			if (!CommonUtils.isEmpty(tableName)){
			    EntityDTO ci2 = QueryUtils.getRecordById(tableName, record.getAsInteger("ci2"));
			    if (!CommonUtils.isEmpty(ci2)){
			        ci2.set("workplace", record.getAsInteger("ci1"));
			        ci2.doUpdate();
			    }
			}
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set CI1 format for cirelation
		//rule <sn>20412a54-4670-5937-0ea4-8bd43ddea5e3</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("ci1_format")) && !CommonUtils.isEmpty(record.get("citype1")) /*if*/) {
			//<body>
			EntityDTO ciType1 = QueryUtils.getRecordById("citype", record.getAsInteger("citype1"));
			if ((ciType1 != null) && (ciType1.get("fieldformat_integer") != null)){
			    record.set("ci1_format", ciType1.get("fieldformat_integer"));
			    //log.info('ci1_format='+record.get("ci_format"));
			}
			else 
			    record.set("ci1_format", 1);
			
			
			
			//</body>
		}
		//endregion

		//region Set CI2 format for cirelation
		//rule <sn>9b93134f-4ec3-e5dd-cad4-d5df91142c6e</sn>
		if (/*if*/CommonUtils.isEmpty(record.get("ci2_format")) && !CommonUtils.isEmpty(record.get("citype2")) /*if*/) {
			//<body>
			EntityDTO ciType2 = QueryUtils.getRecordById("citype", record.getAsInteger("citype2"));
			if ((ciType2 != null) && (ciType2.get("fieldformat_integer") != null)){
			    record.set("ci2_format", ciType2.getAsInteger("fieldformat_integer"));
			    //log.info('ci1_format='+record.get("ci_format"));
			}
			else 
			    record.set("ci2_format", 1);
			
			//</body>
		}
		//endregion

	}

	public static void beforeDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Clear CI1 in Object
		//rule <sn>497e6af5-2ccf-f825-667e-9f7f76c0bf4f</sn>
		if (/*if*/true/*if*/) {
			//<body>
			//log.info("start rule 1024, ci1="+record.get("ci1")+", tablename="+record.getAsString("citype1->entitytype->tablename"));
			if (!CommonUtils.isEmpty(record.get("ci1")) && CommonUtils.isSame(record.getAsString("citype1->entitytype->tablename"),"workplace") && !CommonUtils.isEmpty(record.get("citype2")) && !CommonUtils.isEmpty(record.get("ci2"))){
			    String tableName =record.getAsString("citype2->entitytype->tablename");
			    //ContextUtils.addMessage("rule 1024 - tableName="+tableName);
			    //log.info("rule 1024 - tableName="+tableName);
			    if (!CommonUtils.isEmpty(tableName) && !CommonUtils.isSame(tableName, "software")){
			        EntityDTO ci2 = QueryUtils.getRecordById(tableName, record.getAsInteger("ci2"));
			        if (!CommonUtils.isEmpty(ci2)){
			            ci2.set("workplace", null);
			            ci2.set("owner",null);
			            ci2.doUpdate();
			        }
			    }
			}
			//</body>
		}
		//endregion

		//region RSM CIRelation Update CI Params on Delete
		//rule <sn>3c249c80-e6f0-1607-218e-85b627750f0f</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (oldrecord.getAsInteger("citype1") != null && oldrecord.getAsInteger("ci1") != null){
			    RSMUtils.updateParameters(oldrecord.getAsInteger("citype1"), oldrecord.getAsInteger("ci1"));
			}
			
			//</body>
		}
		//endregion

	}

}
