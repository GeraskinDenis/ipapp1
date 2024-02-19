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

public class HttpUtils {

private static final Log log = Log.getLogger(HttpUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

// Базовая часть адреса для подключения интеграции
private static final String BASE_URL             = "http://localhost:8080/json";
// Получение токена
private static final String LOGIN_URL            = "/login";

private static final String API_LOGIN    = "ipdeveloper";
private static final String API_PASSWORD = "Qwerty123!";

public static String getToken() throws Exception {
    HttpPostRequest request = new HttpPostRequest(BASE_URL + LOGIN_URL);
    request.addHeader("Content-Type", "application/json");
    
    Map payload = new HashMap();
    payload.put("user", API_LOGIN);
    payload.put("password", API_PASSWORD);
    
    request.setPayload(payload);
    
    Map<String, Map<String, Object>> response = JSONUtils.json2Map(request.execute());
        
	return (String) ((Map) response.get("token")).get("value");
}


// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}