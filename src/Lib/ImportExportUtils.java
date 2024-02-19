package Lib;

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
* Данный класс сформирован автоматически в результате экспорта библиотеки классов.
* Код библиотеки классов содержится между строками 'START_CLASS_LIBRARY_CODE' и 'END_CLASS_LIBRARY_CODE'.
* Код вне этого блока НЕ будет сохранен при импорте в систему.
*/

public class ImportExportUtils {

private static final Log log = Log.getLogger(ImportExportUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void CreateOrUpdateAllLogicExport(String exportQuery)  throws Exception{
	EntityDTO  exportScript = QueryUtils.getRecord("EXPORTDATA","SHORTNAME = 'All Logic'");
	if (exportScript!=null){
		List<EntityDTO> tableList = QueryUtils.getRecordList("ENTITYTYPE","BASEENTITYTYPEID = 28 and not ENTITYTYPEID in (1,2,3,10,13,18, 28,31, 32, 45, 224,257,258,309,316, 317,324)");
		for (EntityDTO table:tableList){
			EntityDTO existRow = QueryUtils.getRecord("EXPORTDATAENTRY","EXPORTDATAID="+exportScript.getKeyValue()+" and ENTITYTYPEID="+ table.getKeyValue());
			if (existRow==null){
				EntityDTO exportScriptRow = new EntityDTO("EXPORTDATAENTRY");
				exportScriptRow.set("EXPORTDATAID", exportScriptRow.getKeyValue());
				exportScriptRow.set("ENTITYTYPEID", table.getKeyValue());
				exportScriptRow.set("EXPORTQUERY", exportQuery);
				exportScriptRow.doInsert();
			}
			else{
				existRow.set("EXPORTQUERY", exportQuery);
				existRow.doUpdate();
			}
		}
	}
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}