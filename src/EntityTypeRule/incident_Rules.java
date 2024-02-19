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
DisplayName: Инцидент
Fields:
 - assignedto (Исполнитель) - INTEGER
 - ci (КЕ ) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - ci_shortname (КЕ строка) - STRING
 - citype (Тип КЕ) - INTEGER
 - citypeparam (Параметр КЕ) - INTEGER
 - citypeparamvalue (Значение Параметра КЕ) - STRING
 - citypicalfault (Типовая неисправность) - INTEGER
 - closure_code (Код закрытия) - STRING
 - coordinator (Координатор) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - deadline (Крайний срок) - DATETIME
 - description (Описание) - TEXT
 - displayname (Наименование) - STRING
 - enableautotransition (Автодействия разрешены) - BOOLEAN
 - entityid (Родительский объект) - INTEGER
 - fact_finish (Фактическое окончание) - DATE
 - fact_start (Фактическое начало) - DATE
 - incidentid (ID) - INTEGER
 - isactive (Активно) - BOOLEAN
 - iserror (Ошибка показана) - BOOLEAN
 - location (Местонахождение) - INTEGER
 - model (Модель КЕ) - STRING
 - parent_entity (Родительский объект) - INTEGER
 - parent_entitytype (Тип родительского объекта) - STRING
 - parentid (Родительский объект) - INTEGER
 - planned_finish (Плановое окончание) - DATETIME
 - planned_start (Плановое начало) - DATETIME
 - priority (Приоритет) - INTEGER
 - service (Услуга) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - solution (Решение) - TEXT
 - source (Источник поступления) - STRING
 - unsuspend_date (Дата возобновления работ) - DATE
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Рабочая группа) - INTEGER
*/

public class incident_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region IM Calculate ParamValue by FaultId on Create
		//common rule <sn>881d2846-84f8-8f73-7bbe-df26bb105621</sn>
		//entities: change, incident
		if (/*if*/!"RSM".equalsIgnoreCase(record.getAsString("source")) && !record.isFieldEmpty("ci") && !record.isFieldEmpty("ciType") &&  !record.isFieldEmpty("citypicalfault")/*if*/) {
			//<body>
			Object faultValue = RSMUtils.getCITypeParamTypicalFaultValue(record.getAsInteger("citypicalfault"));
			record.set("citypeparamvalue", faultValue);
			//</body>
		}
		//endregion

		//region IM Set isActive Field
		//common rule <sn>c7e6f9d9-3520-e2e9-abd0-ce2b66278235</sn>
		//entities: change, incident
		if (/*if*/true/*if*/) {
			//<body>
			String wsStepName = record.getAsString("workflowstepname");
			Boolean isActive= !("completed".equalsIgnoreCase(wsStepName) || "rejected".equalsIgnoreCase(wsStepName));
			
			record.set("isActive", isActive);
			//</body>
		}
		//endregion

		//region Установить значение Модель КЕ
		//rule <sn>81a9dafd-3e21-fafd-171e-15678dbe37ca</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!record.isFieldEmpty("citype") && !record.isFieldEmpty("ci")) {
			    String tableName = record.getAsString("citype->entitytype->tablename");
			    String keyValue = tableName + "id";
			    EntityDTO ci = QueryUtils.getRecord(tableName, keyValue + " = " + record.getAsInteger("ci"));
			    //record.set("model", record.getAsString("ci->model"));
			    record.set("model", ci.getAsString("model"));
			}
			
			    // String tableName = record.getAsString("citype->entitytype->tablename");
			    // EntityDTO ci = QueryUtils.getRecord(tableName, tableName+"id = "+ record.getAsInteger("ci"));
			    // record.set("model", ci.getId());
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Create Task From Incident
		//rule <sn>2d221db1-a733-7044-3609-7c90c8a68fc6</sn>
		if (/*if*/true/*if*/) {
			//<body>
			//ContextUtils.addMessage("rule 1067 start before task start");
			if (record.get("template")==null)
			    Lib.ITSMIncidentUtils.createTaskFromIncident(record);
			
			//</body>
		}
		//endregion

		//region IM-RSM Update CI Param Value on Create
		//common rule <sn>a98ccc48-7f09-1009-6ad1-50ead33c008c</sn>
		//entities: change, incident
		if (/*if*/!"RSM".equalsIgnoreCase(record.getAsString("source")) && !record.isFieldEmpty("citypeparamvalue")/*if*/) {
			//<body>
			// TODO replace wiht native method
			RSMUtils.updateCITypeParamValueByIncident(record);
			//RSMUtils.setCITypeParamValue(record.getAsInteger("citype"), record.getAsInteger("ci"), record.getAsInteger("citypeparam"), record.getAsString("citypeparamvalue"));
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region IM Set isActive Field
		//common rule <sn>c7e6f9d9-3520-e2e9-abd0-ce2b66278235</sn>
		//entities: change, incident
		if (/*if*/true/*if*/) {
			//<body>
			String wsStepName = record.getAsString("workflowstepname");
			Boolean isActive= !("completed".equalsIgnoreCase(wsStepName) || "rejected".equalsIgnoreCase(wsStepName));
			
			record.set("isActive", isActive);
			//</body>
		}
		//endregion

		//region IM-RSM Check CIParam Value is OK before Closing
		//common rule <sn>c2e20a96-7715-2986-bf0e-b02c2f1465a8</sn>
		//entities: change, incident
		if (/*if*/"RSM".equalsIgnoreCase(record.getAsString("source")) && !record.isActive() && oldrecord.isActive() && !CommonUtils.isEmpty(record.get("ci")) && !CommonUtils.isEmpty(record.get("citype")) && !CommonUtils.isEmpty(record.get("citypeparam"))/*if*/) {
			//<body>
			EntityDTO ciParamDTO = RSMUtils.getCIParam(record.getAsInteger("ci"), record.getAsInteger("citypeparam"));
			if (!CommonUtils.isEmpty(ciParamDTO)){
			    CI ci = new CI(record.getAsInteger("citype"), record.getAsInteger("ci"));
			    String paramHealthState = RSMUtils.getCIParamHealthIndicator(ci, ciParamDTO.getAsInteger("ciparam"), ciParamDTO.get("value"));
			    if (!"OK".equalsIgnoreCase(paramHealthState)){
			        throw new ValidationException("Невозможно выполнить Инцидент, т.к. показатель здоровья параметра КЕ находится вне пределах нормы.");
			    }
			}
			//</body>
		}
		//endregion

		//region Установить значение Модель КЕ
		//rule <sn>81a9dafd-3e21-fafd-171e-15678dbe37ca</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (!record.isFieldEmpty("citype") && !record.isFieldEmpty("ci")) {
			    String tableName = record.getAsString("citype->entitytype->tablename");
			    String keyValue = tableName + "id";
			    EntityDTO ci = QueryUtils.getRecord(tableName, keyValue + " = " + record.getAsInteger("ci"));
			    //record.set("model", record.getAsString("ci->model"));
			    record.set("model", ci.getAsString("model"));
			}
			
			    // String tableName = record.getAsString("citype->entitytype->tablename");
			    // EntityDTO ci = QueryUtils.getRecord(tableName, tableName+"id = "+ record.getAsInteger("ci"));
			    // record.set("model", ci.getId());
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region IM-RSM Recalculate CI Params when Incident becomes inactive 
		//common rule <sn>d5c45ce8-a365-6902-25e7-0b30ec0e37cb</sn>
		//entities: change, incident
		if (/*if*/!record.isActive() && oldrecord.isActive() && !record.isFieldEmpty("ci") && !record.isFieldEmpty("ciType")/*if*/) {
			//<body>
			// Инцидент создан вручную (не из РСМ) и в нем указан сбойный параметр
			Boolean isNotFromRSMAndHasParam = !"RSM".equalsIgnoreCase(record.getAsString("source")) && !record.isFieldEmpty("citypeparam");
			
			if (isNotFromRSMAndHasParam){
			    // Возвращаем значение параметра на уровень ОК и при изменении парамтера вызывается перерасчет параметров всех РСМ
			    Object okValue = RSMUtils.getCITypeParamOKValue(record.getAsInteger("citypeparam"));
			    log.info("Rule 828: okValue = " + okValue);
			    RSMUtils.setCITypeParamValue(record.getAsInteger("citype"), record.getAsInteger("ci"), record.getAsInteger("citypeparam"), okValue);
			}
			else{
			    // Вызываем перерасчетр параметров РСМ начиная с КЕ из Инцидента
			    RSMUtils.updateParameters(record.getAsInteger("ciType"), record.getAsInteger("ci"));
			}
			
			
			
			//</body>
		}
		//endregion

	}

}
