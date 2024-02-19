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

public class IncidentProblemUtils {

private static final Log log = Log.getLogger(IncidentProblemUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static String getDescription (EntityDTO record, Map<String, Object> updateFields) throws Exception {
    
// Формируем описание из полей поиска
        StringBuffer description = new StringBuffer();
        // if (!updateFields.isEmpty()) {
            for (Map.Entry<String, Object> updateField : updateFields.entrySet()) {
                
                description.append("<p>" + Lib.IncidentProblemUtils.getFieldname(updateField.getKey()));
                description.append(" : ");
                
                switch (updateField.getKey()) {
                    case "service":
                        String[] service = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String serviceLookupValue = service[0];
                        EntityDTO serviceValue = QueryUtils.getRecordById("service", Integer.parseInt(serviceLookupValue));
                        description.append(""+serviceValue.getAsString("displayname") + "</p>");
                        break;
                    case "citypicalfault":
                        String[] citypicalfault = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String citypicalfaultLookupValue = citypicalfault[0];
                        EntityDTO citypicalfaultValue = QueryUtils.getRecordById("citypicalfault", Integer.parseInt(citypicalfaultLookupValue));
                        description.append(""+citypicalfaultValue.getAsString("displayname") + "</p>");
                        break;
                    case "citype":
                        String[] citype = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String citypeLookupValue = citype[0];
                        EntityDTO citypeValue = QueryUtils.getRecordById("citype", Integer.parseInt(citypeLookupValue));
                        description.append(""+citypeValue.getAsString("displayname") + "</p>");
                        break;
                    case "model":
                        String[] model = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String modelLookupValue = model[0];
                        EntityDTO modelValue = QueryUtils.getRecord("cimodel", "shortname = '" + modelLookupValue + "'");
                        description.append(""+modelValue.getAsString("displayname") + "</p>");
                        break;
                    case "workgroup":
                        String[] workgroup = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String workgroupLookupValue = workgroup[0];
                        EntityDTO workgroupValue = QueryUtils.getRecordById("workgroup", Integer.parseInt(workgroupLookupValue));
                        description.append(""+workgroupValue.getAsString("displayname") + "</p>");
                        break;
                    case "period_for":
                        description.append(CastUtils.getTimestampValue(updateField.getValue()) + "</p>");
                        break;
                    case "period_from": 
                        description.append(CastUtils.getTimestampValue(updateField.getValue()) + "</p>");
                        break;
                    default:
                        description.append(CastUtils.getStringValue(updateField.getValue()) + "</p>");
                        break;
                        
                }
                
                // description.append(CastUtils.getStringValue(updateField.getValue()) + "</p>");
                //description.append("\n");
        
            }
            //description.deleteCharAt(description.length() - 1);
        // }
        return description.toString();
}

public static String getFieldname (String fieldname) {
    switch (fieldname) {
        case "displayname":
            return "Наименование содержит";
        case "service":
            return "Услуга";
        case "citypicalfault":
            return "Типовая неисправность";
        case "period_from":
            return "Период: с";
        case "period_for":
            return "Период: по";
        case "citype":
            return "Тип КЕ";
        case "model":
            return "Модель КЕ";
        case "description":
            return "Описание содержит";
        case "workgroup":
            return "Рабочая группа";
        case "maxcount":
            return "Количество инцидентов больше";
        default:
            return null;
    }
}

public static String getDisplayname (EntityDTO record, Map<String, Object> updateFields) throws Exception {
    
// Формируем описание из полей поиска
        StringBuffer description = new StringBuffer();
        // if (!updateFields.isEmpty()) {
            for (Map.Entry<String, Object> updateField : updateFields.entrySet()) {
                
                description.append("" + Lib.IncidentProblemUtils.getFieldname(updateField.getKey()));
                description.append(" : ");
                
                switch (updateField.getKey()) {
                    case "service":
                        String[] service = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String serviceLookupValue = service[0];
                        EntityDTO serviceValue = QueryUtils.getRecordById("service", Integer.parseInt(serviceLookupValue));
                        description.append(""+serviceValue.getAsString("displayname") + " ");
                        break;
                    case "citypicalfault":
                        String[] citypicalfault = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String citypicalfaultLookupValue = citypicalfault[0];
                        EntityDTO citypicalfaultValue = QueryUtils.getRecordById("citypicalfault", Integer.parseInt(citypicalfaultLookupValue));
                        description.append(""+citypicalfaultValue.getAsString("displayname") + " ");
                        break;
                    case "citype":
                        String[] citype = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String citypeLookupValue = citype[0];
                        EntityDTO citypeValue = QueryUtils.getRecordById("citype", Integer.parseInt(citypeLookupValue));
                        description.append(""+citypeValue.getAsString("displayname") + " ");
                        break;
                    case "model":
                        String[] model = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String modelLookupValue = model[0];
                        EntityDTO modelValue = QueryUtils.getRecord("cimodel", "shortname = '" + modelLookupValue + "'");
                        description.append(""+modelValue.getAsString("displayname") + " ");
                        break;
                    case "workgroup":
                        String[] workgroup = (CastUtils.getStringValue(updateField.getValue())).split("\\$>\\$>\\$");
                        String workgroupLookupValue = workgroup[0];
                        EntityDTO workgroupValue = QueryUtils.getRecordById("workgroup", Integer.parseInt(workgroupLookupValue));
                        description.append(""+workgroupValue.getAsString("displayname") + " ");
                        break;
                    case "period_for":
                        description.append(CastUtils.getTimestampValue(updateField.getValue()) + " ");
                        break;
                    case "period_from": 
                        description.append(CastUtils.getTimestampValue(updateField.getValue()) + " ");
                        break;
                    default:
                        description.append(CastUtils.getStringValue(updateField.getValue()) + " ");
                        break;
                        
                }
                
                // description.append(CastUtils.getStringValue(updateField.getValue()) + "</p>");
                //description.append("\n");
        
            }
            //description.deleteCharAt(description.length() - 1);
        // }
        return description.toString();
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}