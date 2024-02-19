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
DisplayName: Изменение
Fields:
 - change_template (Тип изменения) - INTEGER
 - changeid (ID) - INTEGER
 - ci (ID КЕ ) - INTEGER
 - ci_format (Формат КЕ) - INTEGER
 - ci_shortname (КЕ) - STRING
 - citype (Тип КЕ) - INTEGER
 - citypeparam (Параметр КЕ) - INTEGER
 - citypeparamvalue (Значение Параметра КЕ) - STRING
 - closure_code (Код закрытия) - STRING
 - coordinator (Координатор) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - deadline (Крайний срок) - DATETIME
 - description (Описание) - TEXT
 - displayname (Краткое описание) - STRING
 - entityid (Родительский объект) - INTEGER
 - fact_finish (Фактическое окончание) - DATE
 - fact_start (Фактическое начало) - DATE
 - initiator (Инициатор) - INTEGER
 - isactive (Активно) - BOOLEAN
 - location (Местонахождение) - INTEGER
 - parent_entity (Родительский объект) - INTEGER
 - parent_entitytype (Тип родительского объекта) - STRING
 - parentid (Родительский объект) - INTEGER
 - planned_finish (Плановое окончание) - DATETIME
 - planned_start (Плановое начало) - DATETIME
 - priority (Приоритет) - INTEGER
 - rollback_actions (План отката) - TEXT
 - service (Услуга) - INTEGER
 - severity (Серьезность) - STRING
 - shortname (Внутреннее имя) - STRING
 - source (Источник поступления) - STRING
 - unsuspend_date (Дата возобновления работ) - DATE
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - workflowid (WorkFlow) - INTEGER
 - workflowstepid (Статус) - INTEGER
 - workflowstepname (Внутренний статус) - STRING
 - workgroup (Рабочая группа) - INTEGER
*/

public class change_Rules {

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

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

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
