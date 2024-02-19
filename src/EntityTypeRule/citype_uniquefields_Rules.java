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
DisplayName: Поля кода поиска КЕ
Fields:
 - citype (Тип КЕ) - INTEGER
 - citype_uniquefieldsid (ID) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Название) - STRING
 - entityfield (Поле) - INTEGER
 - isactive (Активно) - BOOLEAN
 - shortname (Внутреннее имя) - STRING
 - sort_order (Порядок) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (Work Flow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Название шага) - STRING
*/

public class citype_uniquefields_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Обновить признак кода поиска КЕ
		//rule <sn>e1b4e963-5f11-7915-2327-5f71740fe828</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO ciType = QueryUtils.getRecord("citype", "citypeid = "+record.getAsInteger("citype"));
			ContextUtils.addMessage("Состав уникальных атрибутов кода поиска КЕ изменён. Не забудьте пересчитать код поиска.", "WARNING");
			ciType.set("is_uniquefields", false);
			ciType.doUpdate();
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Обновить признак кода поиска КЕ
		//rule <sn>e1b4e963-5f11-7915-2327-5f71740fe828</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO ciType = QueryUtils.getRecord("citype", "citypeid = "+record.getAsInteger("citype"));
			ContextUtils.addMessage("Состав уникальных атрибутов кода поиска КЕ изменён. Не забудьте пересчитать код поиска.", "WARNING");
			ciType.set("is_uniquefields", false);
			ciType.doUpdate();
			//</body>
		}
		//endregion

	}

	public static void afterDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Обновить КЕ с этим типом после удаления
		//rule <sn>ba19fb90-52ff-edd0-9dd0-e5354ebf68ea</sn>
		if (/*if*/true/*if*/) {
			//<body>
			EntityDTO ciType = QueryUtils.getRecord("citype", "citypeid = "+oldrecord.getAsInteger("citype"));
			ContextUtils.addMessage("Состав уникальных атрибутов кода поиска КЕ изменён. Не забудьте пересчитать код поиска.", "WARNING");
			ciType.set("is_uniquefields", false);
			ciType.doUpdate();
			//</body>
		}
		//endregion

	}

}
