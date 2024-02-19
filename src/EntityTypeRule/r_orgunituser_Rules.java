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
DisplayName: Сотрудник - Орг. единица
Fields:
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - isactive (Активно) - BOOLEAN
 - ismain (Основная организационная единица) - BOOLEAN
 - orgunit (Организационная единица) - INTEGER
 - r_orgunituserid (Внутренний код) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - title (Должность) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - user (Пользователь) - INTEGER
*/

public class r_orgunituser_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check R_ORGUNITUSER on single ISMAIN=TRUE on insert
		//rule <sn>checkISMAINOrgunituserOnInsert</sn>
		if (/*if*/record.get("ISMAIN") == true/*if*/) {
			//<body>
			EntityDTO link = QueryUtils.getRecord("R_ORGUNITUSER", "ISMAIN=true and USER = " + record.get("USER"));
			if (link!=null){
			    //record.set("ISMAIN",false);  
			    throw new Exception("У пользователя с кодом - " + record.get ( "USER" ) + " уже существует связь с основной орг. единицей, код записи связи - " + record.get ( "R_ORGUNITUSERID"));
			}
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set TITLE to USER on insert R_ORGUNITUSER
		//rule <sn>setTitleToUserTableOnInsert</sn>
		if (/*if*/(record.get("TITLE") != null) && (record.get("ISMAIN") == true)/*if*/) {
			//<body>
			EntityDTO user = new EntityDTO("USER", record.get("USER"));
			if (user!=null){
			     user.set("TITLE", record.get("TITLE"));
			     //user.set("ORGUNIT", record.get("ORGUNIT"));
			     user.doUpdate();
			}
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Set TITLE to USER on update R_ORGUNITUSER
		//rule <sn>setTitleToUserTableOnUpdate</sn>
		if (/*if*/(record.get("TITLE")!=null) && (record.get("TITLE")!=oldrecord.get("TITLE")) && (record.get("ISMAIN") == true)/*if*/) {
			//<body>
			EntityDTO user = new EntityDTO("USER", record.get("USER"));
			if (user!=null){
			     user.set("TITLE", record.get("TITLE"));
			     //user.set("ORGUNIT", record.get("ORGUNIT"));
			     user.doUpdate();
			}
			//</body>
		}
		//endregion

		//region Check R_ORGUNITUSER on single ISMAIN=TRUE on update
		//rule <sn>checkISMAINOrgunituserOnUpdate</sn>
		if (/*if*/(record.get("ISMAIN")!=oldrecord.get("ISMAIN")) && (record.get("ISMAIN") == true)/*if*/) {
			//<body>
			EntityDTO link = QueryUtils.getRecord("R_ORGUNITUSER", "ISMAIN=true and USER = " + record.get("USER"));
			if (link!=null){
			     //record.set("ISMAIN", false); 
			     throw new Exception("У пользователя с кодом - " + record.get ( "USER" ) + " уже существует связь с основной орг. единицей, код записи связи - " + record.get ( "R_ORGUNITUSERID"));
			}
			//</body>
		}
		//endregion

		//region Check R_ORGUNITUSER on ISMAIN was TRUE to FALSE value on update
		//rule <sn>setNullTitleToUserTableOnUpdate</sn>
		if (/*if*/(record.get("ISMAIN")!=oldrecord.get("ISMAIN")) && (record.get("ISMAIN") == false)/*if*/) {
			//<body>
			EntityDTO user = new EntityDTO("USER", record.get("USER"));
			user.set("TITLE", null);
			user.set("ORGUNIT", null);
			user.doUpdate();
			//</body>
		}
		//endregion

	}

}
