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
DisplayName: Изменения КЕ
Fields:
 - change (Изменение) - INTEGER
 - ci (КЕ ) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - citype (Тип КЕ) - INTEGER
 - citype_changeid (ID) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - description (Описание изменения) - TEXT
 - displayname (Название) - STRING
 - isactive (Активно) - BOOLEAN
 - shortname (Внутреннее имя) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class citype_change_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set ci_shortname from ci
		//common rule <sn>9be65e83-7254-57a7-6dfb-e05a0fbed7ab</sn>
		//entities: citype_change, change
		if (/*if*/true/*if*/) {
			//<body>
			log.info("incid="+record.getId()+", правило 871");
			log.info("результат = "+CommonUtils.isEmpty(record.get("ci_shortname")));
			log.info("значение="+record.get("ci_shortname"));
			if (CommonUtils.isEmpty(record.get("ci_shortname")) && !CommonUtils.isEmpty(record.get("ci")) && !CommonUtils.isEmpty(record.get("citype"))){
			    log.info("правило 871 - проверка пройдена");
			    EntityDTO ciType = QueryUtils.getRecordById("citype",  record.getAsInteger("citype"));
			    if (!CommonUtils.isEmpty(ciType)) {
			        EntityDTO entityType = QueryUtils.getRecordById("entitytype", ciType.getAsInteger("entitytype"));
			        if (!CommonUtils.isEmpty(entityType) && !CommonUtils.isEmpty(entityType.get("tablename"))){
			            EntityDTO ci =  QueryUtils.getRecordById(entityType.getAsString("tablename"),  record.getAsInteger("ci"));
			            if (!CommonUtils.isEmpty(ci) && !CommonUtils.isEmpty(ci.get("shortname")))
			                record.set("ci_shortname", ci.getAsString("shortname"));
			        } 
			    }
			}
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set ci_shortname from ci
		//common rule <sn>9be65e83-7254-57a7-6dfb-e05a0fbed7ab</sn>
		//entities: citype_change, change
		if (/*if*/true/*if*/) {
			//<body>
			log.info("incid="+record.getId()+", правило 871");
			log.info("результат = "+CommonUtils.isEmpty(record.get("ci_shortname")));
			log.info("значение="+record.get("ci_shortname"));
			if (CommonUtils.isEmpty(record.get("ci_shortname")) && !CommonUtils.isEmpty(record.get("ci")) && !CommonUtils.isEmpty(record.get("citype"))){
			    log.info("правило 871 - проверка пройдена");
			    EntityDTO ciType = QueryUtils.getRecordById("citype",  record.getAsInteger("citype"));
			    if (!CommonUtils.isEmpty(ciType)) {
			        EntityDTO entityType = QueryUtils.getRecordById("entitytype", ciType.getAsInteger("entitytype"));
			        if (!CommonUtils.isEmpty(entityType) && !CommonUtils.isEmpty(entityType.get("tablename"))){
			            EntityDTO ci =  QueryUtils.getRecordById(entityType.getAsString("tablename"),  record.getAsInteger("ci"));
			            if (!CommonUtils.isEmpty(ci) && !CommonUtils.isEmpty(ci.get("shortname")))
			                record.set("ci_shortname", ci.getAsString("shortname"));
			        } 
			    }
			}
			//</body>
		}
		//endregion

	}

}
