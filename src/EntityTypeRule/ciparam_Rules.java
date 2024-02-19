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
DisplayName: Параметры КЕ
Fields:
 - ci (КЕ) - INTEGER
 - ciparam (         Параметр) - INTEGER
 - ciparamid (ID) - INTEGER
 - citype (         Тип КЕ) - INTEGER
 - citype_shortname (Тип КЕ - строка) - STRING, определение соединения: citype->shortname
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - description (Описание параметра) - TEXT
 - displayname (Наименование) - STRING
 - healthicon (Индикатор) - STRING, вычисление: case healthindicator when 'ERROR' then 'glyphicon glyphicon-remove-sign#FF0000' when 'WARN' then 'glyphicon glyphicon-adjust#FFA500' when 'OK' then 'glyphicon glyphicon-ok-sign#008000' else 'glyphicon glyphicon-ok-sign#008000' end
 - healthindicator (Показатель здоровья параметра) - STRING
 - isactive (Активно) - BOOLEAN
 - param_order (Порядок резерв.) - INTEGER
 - param_type (Тип параметра) - STRING, определение соединения: ciparam->param_type
 - paramname (Имя параметра) - STRING, определение соединения: ciparam->paramname
 - service_level (Уровень сервиса) - INTEGER
 - shortname (Внутреннее имя) - STRING
 - skipvisualization (Не показывать) - BOOLEAN, определение соединения: ciparam->skipvisualization
 - sortorder (Порядок) - INTEGER, определение соединения: ciparam->sortorder
 - unit_of_measure (Ед. измерения) - INTEGER, определение соединения: ciparam->unitofmeasure
 - unitofmeasure (Единицы измерения) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - value (Значение) - STRING
 - value_format (Формат значения) - INTEGER, определение соединения: ciparam->value_format
*/

public class ciparam_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM Set CIParam Health Indicator on Create
		//rule <sn>4bd36302-214b-fcd4-fe8f-0c672a113a3c</sn>
		if (/*if*/true/*if*/) {
			//<body>
			CI ci = new CI(record.getAsInteger("citype"), record.getAsInteger("ci"));
			
			record.set("healthindicator", RSMUtils.getCIParamHealthIndicator(ci, record.getAsInteger("ciparam"), record.get("value")));
			//</body>
		}
		//endregion

	}

	public static void afterInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM Update Up CI Params
		//rule <sn>f09d92e1-b553-a4e5-08da-4483e9f78b2b</sn>
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.updateParameters(record.getAsInteger("ciType"), record.getAsInteger("ci"));
			//</body>
		}
		//endregion

		//region RSM Log Param Health Value Change
		//rule <sn>1c86029f-76a9-58e6-a928-f4d0596f0672</sn>
		if (/*if*/true/*if*/) {
			//<body>
			CITypeParamUtils.logHealthChange(oldrecord, record);
			/*
			if (CommonUtils.isEmpty(oldrecord)){
			    CITypeParamUtils.logHealthChange(record.getAsInteger("citype"), record.getAsInteger("ci"), record.getAsInteger("ciparam"), null, record.getAsString("healthindicator"), record.getAsString("value"));
			}
			else{
			    CITypeParamUtils.logHealthChange(record.getAsInteger("citype"), record.getAsInteger("ci"), record.getAsInteger("ciparam"), oldrecord.getAsString("healthindicator"), record.getAsString("healthindicator"), record.getAsString("value"));
			}
			*/
			
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM Set CIParam Health Indicator On Update
		//rule <sn>e3d78f26-91a0-7662-407b-f6e702653234</sn>
		if (/*if*/!CommonUtils.isSame(record.getAsString("value"), oldrecord.getAsString("value"))/*if*/) {
			//<body>
			CI ci = new CI(record.getAsInteger("citype"), record.getAsInteger("ci"));
			record.set("healthindicator", RSMUtils.getCIParamHealthIndicator(ci, record.getAsInteger("ciparam"), record.get("value")));
			//</body>
		}
		//endregion

	}

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM Update Up CI Params
		//rule <sn>7c49ace3-4913-b47e-47bc-1b7fd475ea2f</sn>
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.updateParameters(record.getAsInteger("ciType"), record.getAsInteger("ci"));
			
			//</body>
		}
		//endregion

		//region RSM Update Up CI Params
		//rule <sn>f09d92e1-b553-a4e5-08da-4483e9f78b2b</sn>
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.updateParameters(record.getAsInteger("ciType"), record.getAsInteger("ci"));
			//</body>
		}
		//endregion

		//region RSM Log Param Health Value Change
		//rule <sn>1c86029f-76a9-58e6-a928-f4d0596f0672</sn>
		if (/*if*/true/*if*/) {
			//<body>
			CITypeParamUtils.logHealthChange(oldrecord, record);
			/*
			if (CommonUtils.isEmpty(oldrecord)){
			    CITypeParamUtils.logHealthChange(record.getAsInteger("citype"), record.getAsInteger("ci"), record.getAsInteger("ciparam"), null, record.getAsString("healthindicator"), record.getAsString("value"));
			}
			else{
			    CITypeParamUtils.logHealthChange(record.getAsInteger("citype"), record.getAsInteger("ci"), record.getAsInteger("ciparam"), oldrecord.getAsString("healthindicator"), record.getAsString("healthindicator"), record.getAsString("value"));
			}
			*/
			
			//</body>
		}
		//endregion

	}

	public static void afterDelete(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region RSM CIRelation Update CI Params On Delete
		//rule <sn>c4a2a27f-69eb-2017-8c83-6c71eadf0f4e</sn>
		if (/*if*/true/*if*/) {
			//<body>
			RSMUtils.updateParameters(oldrecord.getAsInteger("citype1"), oldrecord.getAsInteger("ci1"));
			//</body>
		}
		//endregion

	}

}
