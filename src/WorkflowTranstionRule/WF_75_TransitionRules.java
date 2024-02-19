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
DisplayName: ЖЦ Изменения
Steps:
 - rfc (Регистрация запроса на изменение)
 - planning (Оценка и планирование)
 - approvment (Согласование)
 - implementation (Выполнение изменения)
 - review (Ревью результатов)
 - completed (Выполнено)
 - change_rollback (Возврат в исходное состояние)
 - cancelled (Отменено)
 - not_completed (Не выполнено)
*/

public class WF_75_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//На оценку
	//after <sn>change_to_planning</sn>
	//steps: rfc (Регистрация запроса на изменение) -> planning (Оценка и планирование)
	public static void change_to_planning_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Create Change Tasks From Template
		//rule <sn>d7200128-0d42-294d-0a25-8c8914db5b57</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Lib.ChangeUtils.createChangeTasksFromTemplate(record);
			//</body>
		}
		//endregion

	}

	//На согласование
	//before <sn>change_to_approve</sn>
	//steps: planning (Оценка и планирование) -> approvment (Согласование)
	public static void change_to_approve_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check Change Plan created
		//rule <sn>be2bd7e2-4938-9e82-a149-eeca9aeb61c9</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Integer taskCount = QueryUtils.getCount("task","parent_entitytype = '"+record.getTableName()+"' and parent_entityid="+record.getId()+" and not workflowstepname in ('cancelled')");
			if (taskCount==0)
			    throw new Exception("Заполните план работ по выполнению Изменения");
			//</body>
		}
		//endregion

		//region Проверка на наличие изменений КЕ при переходе в статус согласование
		//rule <sn>85534ca1-0566-ae22-4b45-42a56dd0e9bc</sn>
		if (/*if*/true/*if*/) {
			//<body>
			Integer citypeChangeCount = QueryUtils.getCount("citype_change", "change = "+record.getId());
			if (citypeChangeCount == 0) {
			    ContextUtils.addMessage("Не были указаны изменения КЕ", "WARNING");
			}
			//</body>
		}
		//endregion

	}

	//На ревью
	//before <sn>change_to_review</sn>
	//steps: implementation (Выполнение изменения) -> review (Ревью результатов)
	public static void change_to_review_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check All Object Tasks Completed
		//rule <sn>9e978f6f-51c3-26e8-c3cf-0e3ba1777420</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!Lib.TaskUtils.checkTasksCompleted(record))
			    throw new Exception("Выполните или отмените все связанные наряды");
			//</body>
		}
		//endregion

	}

	//Применить план отката
	//before <sn>change_torollback</sn>
	//steps: implementation (Выполнение изменения) -> change_rollback (Возврат в исходное состояние)
	public static void change_torollback_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check All Object Tasks Completed
		//rule <sn>9e978f6f-51c3-26e8-c3cf-0e3ba1777420</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!Lib.TaskUtils.checkTasksCompleted(record))
			    throw new Exception("Выполните или отмените все связанные наряды");
			//</body>
		}
		//endregion

	}

	//Исходное состояние восстановлено
	//before <sn>change_rollback_completed</sn>
	//steps: change_rollback (Возврат в исходное состояние) -> not_completed (Не выполнено)
	public static void change_rollback_completed_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Check All Object Tasks Completed
		//rule <sn>9e978f6f-51c3-26e8-c3cf-0e3ba1777420</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!Lib.TaskUtils.checkTasksCompleted(record))
			    throw new Exception("Выполните или отмените все связанные наряды");
			//</body>
		}
		//endregion

	}

}
