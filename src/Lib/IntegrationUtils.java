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

public class IntegrationUtils {

private static final Log log = Log.getLogger(IntegrationUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

// Базовая часть адреса для подключения интеграции
private static final String BASE_URL             = "http://87.236.22.32:8080/json";
// Получение токена
private static final String LOGIN_URL            = "/login";
// API Должности
private static final String RESOURCE_TITLE_URL   = "/integration/isop/asutr/common/service/nri/v1";
// API Сотрудники
private static final String USER_URL             = "/integration/isop/services/common/asutr/employee/v4";
// API Штатная единица
private static final String RESOURCE_ROLE_URL    = "/integration/isop/asutr/common/staffdata/service/v1";
// API Струтктура
private static final String R_ORGUNIT_URL        = "/integration/isop/asutr/common/service/orgstruct/v3/asutr-common-service-orgstruct-v3.0";
/**
 * Логин и пароль для подключения к интеграции 
 */
private static final String API_LOGIN    = "demo";
private static final String API_PASSWORD = "Qwerty123!";


public static String getUsers() throws Exception {
    HttpPostRequest request = new HttpPostRequest(BASE_URL + USER_URL);
    request.addHeader("Content-Type", "application/json");
    String token = getToken();
    ContextUtils.addMessage(token);
    request.addHeader("X-AUTH", token);
    
    return request.execute();
}

public static String getResourseTitle() throws Exception {
    HttpPostRequest request = new HttpPostRequest(BASE_URL + RESOURCE_TITLE_URL);
    request.addHeader("Content-Type", "application/json");
    String token = getToken();
    ContextUtils.addMessage(token);
    request.addHeader("X-AUTH", token);
    
    return request.execute();
}

public static String getResourseRole() throws Exception {
    HttpPostRequest request = new HttpPostRequest(BASE_URL + RESOURCE_ROLE_URL);
    request.addHeader("Content-Type", "application/json");
    String token = getToken();
    ContextUtils.addMessage(token);
    request.addHeader("X-AUTH", token);
    
    return request.execute();
}

private static String getToken() throws Exception {
    HttpPostRequest request = new HttpPostRequest(BASE_URL + LOGIN_URL);
    request.addHeader("Content-Type", "application/json");
    ContextUtils.addMessage("1");
    
    Map payload = new HashMap();
    payload.put("user", API_LOGIN);
    payload.put("password", API_PASSWORD);
    
    request.setPayload(payload);
    
    ContextUtils.addMessage("2");
    Map<String, Map<String, Object>> response = JSONUtils.json2Map(request.execute());
    ContextUtils.addMessage("3");
        
	return (String) ((Map) response.get("token")).get("value");
}

public static DateTime getNextIntegrationDateTime(EntityDTO record) throws Exception {
    PlanningService planningService = Lib.PlanningServiceFactory.createByPlannedActivityTemplate(record);
    DateTime current = new DateTime();
    return planningService.getNextTimeList(record.getDateTime("startintegrationdate"), record.getDateTime("endintegrationdate"))
	                        .stream()
	                        .filter(dateTime -> dateTime.isAfter(current))
	                        .min(DateTime::compareTo)
	                        .orElse(record.getDateTime("endintegrationdate"));
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}