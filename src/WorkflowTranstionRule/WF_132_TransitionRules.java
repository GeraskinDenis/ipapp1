package WorkflowTranstionRule;

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
DisplayName: ЖЦ Шаблон наряда
Steps:
 - new (Черновик)
 - activated (Активен)
 - deactivated (Деактивирован)
*/

public class WF_132_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Деактивировать
	//before <sn>activated_to_deactivated</sn>
	//steps: activated (Активен) -> deactivated (Деактивирован)
	public static void activated_to_deactivated_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Проверка на предшественников при деактивации
		//rule <sn>50aab07b-b7df-8437-2a52-a0a8e7493101</sn>
		if (/*if*/true/*if*/) {
			//<body>
			List<EntityDTO> predecessors = QueryUtils.getRecordList("task_template", "predecessor = "+record.get("task_templateid"));
			if (predecessors.size() != 0) {
			   throw new Exception("Для деактивации шаблона наряда, очистите поле \"Предшественник\" у связанных объектов.");
			}
			//</body>
		}
		//endregion

	}

}
