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

public class FieldFormatUtils {

private static final Log log = Log.getLogger(FieldFormatUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

/*
    Метод получает tablename (Системное имя) таблицы и возвращает (при наличии)
    id строкового филдформата, который необходимо установить для того, чтобы 
    корректно отображался объект.
    
    Если строкового формата у этого объекта нет, то возвращает null
*/
public static Integer getComplexFieldFormatId(String tableName) {
    
    EntityDTO format = QueryUtils.getRecord("fieldFormat", "isactive = 1"
        + " and patternType = 'lookupEntity'"
        + " and fieldStyle = 'autocomplete'"
        + " and lookupEntity = '" + tableName + "'"
        + " and lookupField = 'shortname'"
        + " and fieldType = 'STRING'");
    
    if (format == null || format.isEmpty() || CommonUtils.isEmpty(format.getId()))
        return null;
    
    return format.getId();
    
}

/*
    Метод получает id записи в таблице citype и возвращает (при наличии)
    id строкового филдформата, который необходимо установить для того, чтобы 
    корректно отображался объект.
    
    Если строкового формата у этого объекта нет, то возвращает null
*/
public static Integer getComplexFieldFormatIdByCiTypeId(Integer ciTypeId) {
    
    String tableName = CIType.getCiTypeTableNameById(ciTypeId);
    
    EntityDTO format = QueryUtils.getRecord("fieldFormat", "isactive = 1"
        + " and patternType = 'lookupEntity'"
        + " and fieldStyle = 'autocomplete'"
        + " and lookupEntity = '" + tableName + "'"
        + " and lookupField = 'shortname'"
        + " and fieldType = 'STRING'");
    
    if (format == null || format.isEmpty() || CommonUtils.isEmpty(format.getId()))
        return null;
    
    return format.getId();
    
}

/*

    Метод сопоставляет объект имущества (тип объекта + shortname) 
    и его Назначения (записи из citype), которые могут быть показаны 
    выборочно (по Типу/Виду/Категории)
*/

public static Integer getPurposeCatalog(String citype_shortname, String ci_shortname) {

    EntityDTO obj = QueryUtils.getRecordByShortName(citype_shortname, ci_shortname);

    if (CommonUtils.isEmpty(obj)) return -1;

    if (CommonUtils.isEmpty(obj.getAsString("citype_shortname"))) return -1;

    String parent_type = null;

    switch(obj.getAsString("citype_shortname")) {
        case "land_plot":
            parent_type = "land_plot_purpose";
        break;
        case "zone":
            parent_type = "zone_purpose";
        break;
        case "build":
            if (CommonUtils.isSame(obj.getAsString("resource_type"), "nonresidential_building_type"))
                parent_type = "nonresidential_building_purpose";
        break;
        case "facility":
            if (!CommonUtils.isEmpty(obj.getAsString("resource_type")))
                parent_type = obj.getAsString("resource_type");
        break;
        case "room":
            if (!CommonUtils.isEmpty(obj.getAsString("usage_category")))
                parent_type = obj.getAsString("usage_category");
        break;
    }
    
    EntityDTO parent_citype = QueryUtils.getRecordByShortName("citype", parent_type);
    
    if (!CommonUtils.isEmpty(parent_citype))
        return parent_citype.getKeyValue();
    else
        return -1;
    //return CommonUtils.nullSub(QueryUtils.getRecordByShortName("citype", parent_type).getKeyValue(), -1);
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}