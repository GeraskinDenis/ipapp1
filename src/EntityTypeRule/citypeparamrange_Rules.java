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
DisplayName: Диапазон значений параметра КЕ
Fields:
 - citypeparamid (Целевой параметр КЕ) - INTEGER
 - citypeparamrangeid (ID) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - displayname (Наименование) - STRING
 - isactive (Активно) - BOOLEAN
 - paramvalue (Целевое значение параметра) - STRING
 - shortname (Внутреннее имя) - STRING
 - sourcecitypeparamid (Референсный параметр типа КЕ) - INTEGER
 - sourcelowervalue (Нижнее значение параметра) - STRING
 - sourceuppervalue (Верхнее значение параметра) - STRING
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
*/

public class citypeparamrange_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void beforeInsert(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region CITypeParamRangeValidations
		//rule <sn>e93c1bfb-ddb9-63a7-13cb-f20fb3f63e44</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.isFieldEmpty("sourcelowervalue") && record.isFieldEmpty("sourceuppervalue")){
			    throw new ValidationException("Укажите нижнюю и/или верхнюю границу диапазона");
			}
			
			Integer targetCiType = record.getAsInteger("citypeparamid->citype");
			Integer sourceCiType = record.getAsInteger("sourcecitypeparamid->citype");
			
			if (targetCiType == null || sourceCiType == null){
			    throw new ValidationException("Не удалось определить родительский тип КЕ для полей citypeparamid и/или sourcecitypeparamid");
			}
			
			if (!CommonUtils.isSame(targetCiType, sourceCiType)){
			    throw new ValidationException("Целевой и Референсный параметры типа КЕ должны относитья к одному типу КЕ");
			}
			
			
			
			
			//</body>
		}
		//endregion

	}

	public static void beforeUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region CITypeParamRangeValidations
		//rule <sn>e93c1bfb-ddb9-63a7-13cb-f20fb3f63e44</sn>
		if (/*if*/true/*if*/) {
			//<body>
			if (record.isFieldEmpty("sourcelowervalue") && record.isFieldEmpty("sourceuppervalue")){
			    throw new ValidationException("Укажите нижнюю и/или верхнюю границу диапазона");
			}
			
			Integer targetCiType = record.getAsInteger("citypeparamid->citype");
			Integer sourceCiType = record.getAsInteger("sourcecitypeparamid->citype");
			
			if (targetCiType == null || sourceCiType == null){
			    throw new ValidationException("Не удалось определить родительский тип КЕ для полей citypeparamid и/или sourcecitypeparamid");
			}
			
			if (!CommonUtils.isSame(targetCiType, sourceCiType)){
			    throw new ValidationException("Целевой и Референсный параметры типа КЕ должны относитья к одному типу КЕ");
			}
			
			
			
			
			//</body>
		}
		//endregion

	}

}
