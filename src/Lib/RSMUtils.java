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

public class RSMUtils {

private static final Log log = Log.getLogger(RSMUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static EntityDTO createCIRelation(Integer citype1, Integer ci1, Integer citype2, Integer ci2, String relationType){
    EntityDTO relation = new EntityDTO("cirelation");
    if (!CommonUtils.isSame(relationType, "child_parent")){
        relation.set("citype1",citype1);
        relation.set("citype2", citype2);
        relation.set("ci1",ci1);
        relation.set("ci2",ci2);
        relation.set("relation_type",relationType);
    }
    else{
        relation.set("citype1",citype2);
        relation.set("citype2", citype1);
        relation.set("ci1",ci2);
        relation.set("ci2",ci1);
        relation.set("relation_type","parent_child");
    }
    //ContextUtils.addMessage("createCIRelation relation="+relation);
    relation.doInsert();
    return relation;
}

public static void deleteCiRelation(EntityDTO ci1, EntityDTO ci2){
   //ContextUtils.addMessage("start deleteCIRelation");
    if (CommonUtils.isEmpty(ci1.getAsInteger("citype")) || CommonUtils.isEmpty(ci2.getAsInteger("citype")))
        return;
    //ContextUtils.addMessage("deleteCiRelation befor relation delete");
    //ContextUtils.addMessage("query="+"citype2="+ci2.get("citype")+" and ci2="+ci2.getId()+" and citype1="+ci1.get("citype")+" and ci1="+ci1.getId());
    
    List<EntityDTO> citypeRelationList = QueryUtils.getRecordList("cirelation", "citype2="+ci2.get("citype")+" and ci2="+ci2.getId()+" and citype1="+ci1.get("citype")+" and ci1="+ci1.getId());
    for (EntityDTO citypeRelation:citypeRelationList){
        //ContextUtils.addMessage("citypeRelation = "+citypeRelation+", citype2="+ci2.get("citype")+", ci2="+ci2.getId());
        citypeRelation.doDelete();
    }
    
    List<EntityDTO> citypeChildRelationList = QueryUtils.getRecordList("cirelation", "citype2="+ci1.get("citype")+" and ci2="+ci1.getId()+" and citype1="+ci2.get("citype")+" and ci1="+ci2.getId());
    for (EntityDTO citypeRelation:citypeChildRelationList){
        //ContextUtils.addMessage("citypeRelation = "+citypeRelation+", citype2="+ci2.get("citype")+", ci2="+ci2.getId());
        citypeRelation.doDelete();
    }
    
}

public static Integer getServiceManager(EntityDTO ci) {
    List<EntityDTO> citypeRelationList = QueryUtils.getRecordList("cirelation", "citype2 = " + ci.getAsInteger("citype") + " and ci2 = " + ci.getId());
    for (EntityDTO citypeRelation : citypeRelationList) {
        if (CommonUtils.isSame(citypeRelation.getAsInteger("citype1->entitytype"), SystemUtils.getEntityTypeIdByTableName("service"))) {
            EntityDTO service = QueryUtils.getRecordById("service", citypeRelation.getAsInteger("ci1"));
            if (!CommonUtils.isEmpty(service.get("owner"))) {
                return  service.getAsInteger("owner->userid");
            }
        } else {
            String ci1TableName = SystemUtils.getTableNameByEntityTypeId( citypeRelation.getAsInteger("citype1->entitytype"));
            return getServiceManager(QueryUtils.getRecordById(ci1TableName, citypeRelation.getAsInteger("ci1")));
        }
    }
    return null;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}