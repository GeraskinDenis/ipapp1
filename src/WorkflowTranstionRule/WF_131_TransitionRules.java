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
DisplayName: ЖЦ Генератор работ
Steps:
 - created (Черновик)
 - activated (Активен)
 - deactivated (Деактивирован)
*/

public class WF_131_TransitionRules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	//Активировать
	//before <sn>created_activated</sn>
	//steps: created (Черновик) -> activated (Активен)
	public static void created_activated_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(!Lib.PlannedActivityTemplateUtils.isTasksTemplateListEmpty(record) && Lib.PlannedActivityTemplateUtils.isAnyActiveTasks(record))) return;

		//region Проверить выход за крайний срок для перехода ЖЦ
		//rule <sn>dc1d5bbd-b2da-cd4b-8ffc-e5fb1438767d</sn>
		if (/*if*/true/*if*/) {
			//<body>
			List < EntityDTO > eTaskTemplates = QueryUtils.getRecordList("task_template", "parent_entityid =" + record.get("plannedactivity_templateid"));
			for (EntityDTO taskTemplate: eTaskTemplates) {
			  Double deadlineDuration = taskTemplate.getAsDouble("duration");
			  Integer parentDeadlineDuration = record.getAsInteger("deadline_duration");
			  if (deadlineDuration > CastUtils.getDoubleValue(parentDeadlineDuration)) {
			    throw new Exception("Длительность шаблона наряда " + taskTemplate.get("displayname") + " превышает длительность генератора работ.");
			  }
			
			    List < EntityDTO > eTaskPredecessor = QueryUtils.getRecordList("with recursive r as (select task_templateid, predecessor, displayname, duration, 1 as level from fkv39o1z.v_task_template where task_templateid ="+taskTemplate.get("task_templateid")+"union all select task.task_templateid, task.predecessor, task.displayname, task.duration ,(r.level + 1) as level from fkv39o1z.v_task_template task join r on task.predecessor = r.task_templateid) select * from r");
			    Double sum = 0d;
			    for (EntityDTO taskPredecessor : eTaskPredecessor) {
			        sum += taskPredecessor.getAsDouble("duration");
			    }
			    if (sum > CastUtils.getDoubleValue(parentDeadlineDuration)){
			    throw new Exception("Суммарная длительность последовательных шаблонов наряда превышает длительность генератора работ. Подсказка: длительность последовательности из " + eTaskPredecessor.size() + " шаблонов наряда больше, чем длительность генератора работ (" + sum + ">" + CastUtils.getDoubleValue(parentDeadlineDuration) + ")");
			    }
			  
			
			}
			//task_template deadline_duration (integer) на duration (double)
			//</body>
		}
		//endregion

	}

	//Деактивировать
	//after <sn>activated_deactivated</sn>
	//steps: activated (Активен) -> deactivated (Деактивирован)
	public static void activated_deactivated_afterTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region CancelPlannedActivitiesByTemplate
		//rule <sn>776e8ec0-f839-3f0e-483f-e64ad8723be6</sn>
		if (/*if*/true/*if*/) {
			//<body>
			// ContextUtils.addMessage("Выполняется отмена ППР по заданному расписанию, "
			// +"по окончанию генерации будет выведено сообщение о количестве отмененных ППР");
			//QueryUtils.deleteRecords("cilastplanneddate", "templateId=" + record.getId());
			Lib.PlannedActivityTemplateUtils.cancelPlannedActivities(record);
			//</body>
		}
		//endregion

	}

	//Активировать
	//before <sn>deactivated_to_activated</sn>
	//steps: deactivated (Деактивирован) -> activated (Активен)
	public static void deactivated_to_activated_beforeTranstion(EntityDTO record, EntityDTO oldrecord) throws Exception {

		if (!(!Lib.PlannedActivityTemplateUtils.isTasksTemplateListEmpty(record) && Lib.PlannedActivityTemplateUtils.isAnyActiveTasks(record))) return;

		//region Проверить выход за крайний срок для перехода ЖЦ
		//rule <sn>dc1d5bbd-b2da-cd4b-8ffc-e5fb1438767d</sn>
		if (/*if*/true/*if*/) {
			//<body>
			List < EntityDTO > eTaskTemplates = QueryUtils.getRecordList("task_template", "parent_entityid =" + record.get("plannedactivity_templateid"));
			for (EntityDTO taskTemplate: eTaskTemplates) {
			  Double deadlineDuration = taskTemplate.getAsDouble("duration");
			  Integer parentDeadlineDuration = record.getAsInteger("deadline_duration");
			  if (deadlineDuration > CastUtils.getDoubleValue(parentDeadlineDuration)) {
			    throw new Exception("Длительность шаблона наряда " + taskTemplate.get("displayname") + " превышает длительность генератора работ.");
			  }
			
			    List < EntityDTO > eTaskPredecessor = QueryUtils.getRecordList("with recursive r as (select task_templateid, predecessor, displayname, duration, 1 as level from fkv39o1z.v_task_template where task_templateid ="+taskTemplate.get("task_templateid")+"union all select task.task_templateid, task.predecessor, task.displayname, task.duration ,(r.level + 1) as level from fkv39o1z.v_task_template task join r on task.predecessor = r.task_templateid) select * from r");
			    Double sum = 0d;
			    for (EntityDTO taskPredecessor : eTaskPredecessor) {
			        sum += taskPredecessor.getAsDouble("duration");
			    }
			    if (sum > CastUtils.getDoubleValue(parentDeadlineDuration)){
			    throw new Exception("Суммарная длительность последовательных шаблонов наряда превышает длительность генератора работ. Подсказка: длительность последовательности из " + eTaskPredecessor.size() + " шаблонов наряда больше, чем длительность генератора работ (" + sum + ">" + CastUtils.getDoubleValue(parentDeadlineDuration) + ")");
			    }
			  
			
			}
			//task_template deadline_duration (integer) на duration (double)
			//</body>
		}
		//endregion

	}

}
