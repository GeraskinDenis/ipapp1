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
DisplayName: Версии документов
Fields:
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - document (Документ) - INTEGER
 - document_versionsid (ID) - INTEGER
 - file (Файл) - INTEGER
 - isactive (Активно) - BOOLEAN
 - isactual (Актуальная) - BOOLEAN
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - version_datetime (Дата версии) - DATETIME
 - version_number (Номер версии) - STRING
*/

public class document_versions_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region CheckIfVersionActualBeforeAdd
		//rule <sn>d1daa366-c9e3-1d62-0980-3fdf1ac2be04</sn>
		if (/*if*/true/*if*/) {
			//<body>
			// log.INFO("RULE 1494 START");
			List<EntityDTO> entLst = QueryUtils.getRecordList("document_versions", 
			        "document = " + record.get("document"));
			// log.info("documents:");
			if (entLst != null && entLst.size() > 0){
			    if (record.get("isactual") != null && record.getAsBoolean("isactual")){
			        for(EntityDTO ent : entLst){
			            // log.info("vesrion "+ent);
			            if(ent.getAsBoolean("isactual") == true){
			                ent.set("enableautoupdate", false);
			                ent.set("isactual", false);
			                ent.doUpdate();
			            }
			        }
			    }
			}
			else {
			    // log.info("no versions, set actual");
			    record.set("isactual", true);
			}
			    // log.INFO("RULE 1494 END");
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region SetDocumentActiveVersionField
		//rule <sn>8c8ba3ff-a080-7f88-af56-9bc9a111619a</sn>
		if (/*if*/record.get("document") != null && record.getAsBooleanNullSub("isactual", false)/*if*/) {
			//<body>
			EntityDTO doc = new EntityDTO("documentation", record.getAsInteger("document"));
			// log.info("doc = " + doc);
			if(doc != null) {
			    // EntityDTO docRel = QueryUtils.getRecord("document_versions", "isactual = 1 and "
			    //     + "document = " + record.getAsInteger("document"));
			    // log.info("docRel = " + docRel);
			    // if(record.get("file") != null) {
			    doc.set("actual_version", record.get("file"));
			    doc.doUpdate();
			    List<EntityDTO> docRelList = QueryUtils.getRecordList("documentations_po", 
			        "documentation = " + record.getAsInteger("document"));
			    if(docRelList != null) {
			        for(EntityDTO dr : docRelList) {
			            dr.set("doc_ver", record.get("file"));
			            dr.doUpdate();
			        }
			    }
			    // }
			    // else {
			    //     doc.set("actual_version", null);
			    //     List<EntityDTO> docRelList = QueryUtils.getRecordList("documentations_po", 
			    //         "document = " + record.getAsInteger("document"));
			    //     if(docRelList != null) {
			    //         for(EntityDTO dr : docRelList) {
			    //             dr.set("doc_ver", null);
			    //             dr.doUpdate();
			    //         }
			    //     }
			    //     log.error("rule 1494 error during setting active version to document field!");
			    // }
			}
			
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region CheckIfVersionActualBeforeSave
		//rule <sn>e8f0e8b1-97fd-9b45-1d5c-75cc70083657</sn>
		if (/*if*/record.get("enableautoupdate") == null/*if*/) {
			//<body>
			List<EntityDTO> entLst = QueryUtils.getRecordList("document_versions", 
			        "document = " + record.get("document"));
			if (entLst != null && entLst.size() > 0){
			    if (record.get("isactual") != null && record.getAsBoolean("isactual")){
			        for(EntityDTO ent : entLst){
			            if(ent.getAsBoolean("isactual") == true){
			                ent.set("enableautoupdate", false);
			                ent.set("isactual", false);
			                ent.doUpdate();
			            }
			        }
			    }
			}
			else
			    record.set("isactual", true);
			
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region SetDocumentActiveVersionField
		//rule <sn>8c8ba3ff-a080-7f88-af56-9bc9a111619a</sn>
		if (/*if*/record.get("document") != null && record.getAsBooleanNullSub("isactual", false)/*if*/) {
			//<body>
			EntityDTO doc = new EntityDTO("documentation", record.getAsInteger("document"));
			// log.info("doc = " + doc);
			if(doc != null) {
			    // EntityDTO docRel = QueryUtils.getRecord("document_versions", "isactual = 1 and "
			    //     + "document = " + record.getAsInteger("document"));
			    // log.info("docRel = " + docRel);
			    // if(record.get("file") != null) {
			    doc.set("actual_version", record.get("file"));
			    doc.doUpdate();
			    List<EntityDTO> docRelList = QueryUtils.getRecordList("documentations_po", 
			        "documentation = " + record.getAsInteger("document"));
			    if(docRelList != null) {
			        for(EntityDTO dr : docRelList) {
			            dr.set("doc_ver", record.get("file"));
			            dr.doUpdate();
			        }
			    }
			    // }
			    // else {
			    //     doc.set("actual_version", null);
			    //     List<EntityDTO> docRelList = QueryUtils.getRecordList("documentations_po", 
			    //         "document = " + record.getAsInteger("document"));
			    //     if(docRelList != null) {
			    //         for(EntityDTO dr : docRelList) {
			    //             dr.set("doc_ver", null);
			    //             dr.doUpdate();
			    //         }
			    //     }
			    //     log.error("rule 1494 error during setting active version to document field!");
			    // }
			}
			
			//</body>
		}
		//endregion

	}

	public static void beforeDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RestrictDocumentActualVersionDeletion
		//rule <sn>3a4b6334-f699-058e-9ab7-e26e606f519a</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if(record.getAsBooleanNullSub("isactual", false)){
			    List<EntityDTO> entLst = QueryUtils.getRecordList("document_versions", 
			        "document = " + oldrecord.get("document"));
			    if (entLst != null && entLst.size() > 1){
			        // ContextUtils.addMessage("Невозможно удалить актуальную версию документа! "
			        // + "Чтобы удалить эту версию, сначала необходимо другую версию сделать актуальной!", "ERROR");
			        throw new Exception("Невозможно удалить актуальную версию документа! "
			        + "Чтобы удалить эту версию, сначала необходимо другую версию сделать актуальной!");
			    }
			}
			
			//</body>
		}
		//endregion

	}

	public static void afterDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region SetDocumentActiveVersionFieldAfterDel
		//rule <sn>5e2af2fb-2160-59a7-4367-c1c65d81532d</sn>
		if (/*if*/oldrecord.get("document") != null && oldrecord.getAsBooleanNullSub("isactual", false)/*if*/) {
			//<body>
			EntityDTO doc = new EntityDTO("documentation", oldrecord.getAsInteger("document"));
			log.info("doc = " + doc);
			if(doc != null) {
			    // EntityDTO docRel = QueryUtils.getRecord("document_versions", "isactual = 1 and "
			    //     + "document = " + record.getAsInteger("document"));
			    // log.info("docRel = " + docRel);
			    // if(record.get("file") != null) {
			    doc.set("actual_version", null);
			    doc.doUpdate();
			    List<EntityDTO> docRelList = QueryUtils.getRecordList("documentations_po", 
			        "documentation = " + oldrecord.getAsInteger("document"));
			    if(docRelList != null) {
			        for(EntityDTO dr : docRelList) {
			            dr.set("doc_ver", null);
			            dr.doUpdate();
			        }
			    }
			    // }
			    // else {
			    //     doc.set("actual_version", null);
			    //     List<EntityDTO> docRelList = QueryUtils.getRecordList("documentations_po", 
			    //         "document = " + record.getAsInteger("document"));
			    //     if(docRelList != null) {
			    //         for(EntityDTO dr : docRelList) {
			    //             dr.set("doc_ver", null);
			    //             dr.doUpdate();
			    //         }
			    //     }
			    //     log.error("rule 1494 error during setting active version to document field!");
			    // }
			}
			
			//</body>
		}
		//endregion

	}

}
