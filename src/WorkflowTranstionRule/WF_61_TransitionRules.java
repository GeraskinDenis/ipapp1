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
DisplayName: ЖЦ Проблемы
Steps:
 - new (Новая)
 - workaround_found (Поиск обходного решения)
 - closed (Закрыта)
 - const_solution_found (Поиск постоянного решения)
 - problem_planning (Планирование устранения Проблемы)
 - problem_execution (Устранение проблемы)
 - confirmation (Подтверждение устранения проблемы)
 - persist (Не устранена)
 - cancelled (Отменена)
*/

public class WF_61_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//К устранению проблемы
	//before <sn>problem_to_execution</sn>
	//steps: problem_planning (Планирование устранения Проблемы) -> problem_execution (Устранение проблемы)
	public static void problem_to_execution_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check problem plan prepared
		//rule <sn>6eecacc2-78a2-29ec-12cb-8673db2e0d1e</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Integer taskCount = QueryUtils.getCount("task","parent_entitytype = 'problem' and parent_entityid="+record.getId()+" and not workflowstepname in ('cancelled','completed')");
			Integer changeCount = QueryUtils.getCount("problem_change_relation","problem = "+record.getId());
			if ((taskCount==0) && (changeCount==0))
			    throw new Exception("Заведите наряды для устранения или связанные изменения на вкладке План устранения");
			
			
			    
			//</body>
		}
		//endregion

	}

	//Проблема не устранена
	//before <sn>problem_not_completed</sn>
	//steps: confirmation (Подтверждение устранения проблемы) -> problem_execution (Устранение проблемы)
	public static void problem_not_completed_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check Open Tasks Exist
		//common rule <sn>f287874b-142b-b7cb-8011-42645fe6c557</sn>
		//entities: ЖЦ Проблемы, ЖЦ Инцидента
		if (/*if*/true/*if*/) {
			//<body>
			if (Lib.TaskUtils.checkTasksCompleted(record))
			    throw new Exception("Верните в работу один из нарядов или создайте новый");
			//</body>
		}
		//endregion

	}

}
