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

public class WorkgroupUtils {

private static final Log log = Log.getLogger(WorkgroupUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static Boolean isMemberOfWorkGroup(int userID, EntityDTO workgroup) {
    
    boolean val = false;
    
            List<EntityDTO> wgUsers = QueryUtils.getRecordList("workgroup_user", "workgroup = " + workgroup.getKeyValue());
            if ((wgUsers != null) && (!wgUsers.isEmpty())) {
                val = wgUsers.stream()
                    .anyMatch( 
                        e -> (!CommonUtils.isEmpty(e.get("user"))) && (CommonUtils.isSame(e.getAsInteger("user"), userID))
                    );
            }
    return val;
}


/*
 * Метод создает записи в таблице WorkGroup_user из пользователей группы доступа, 
 * созданной при Типе формирования По Алгоритму или По Оргструктуре
*/
public static void createWorkGroupUsersFromAccessGroup(EntityDTO record) {
    
    if (!CommonUtils.isEmpty(record.get("accessgroup")) && !CommonUtils.isEmpty(record.getId())) {
        
        List<Integer> users = AccessGroupUtils.getUserIds(record.getAsInteger("accessgroup"));
        
        if ((users != null) && (!users.isEmpty())) {
            
            users.forEach(r -> {
                Integer userCnt = QueryUtils.getCount("workgroup_user", "user = " + r 
                    + " and workgroup = " + record.getId());
                
                if(userCnt == null || userCnt < 1) {
                    EntityDTO wgUser = new EntityDTO("workgroup_user");
                    wgUser.set("user", r);
                    wgUser.set("workgroup", record.getId());
                    wgUser.doInsert();
                }
            });
            
        }
    }
    
}


/*
 * Метод удаляет связанные с группой записи в таблице workgroup_user
*/
public static void deleteWorkGroupUsers(EntityDTO record) {
    
    if (!CommonUtils.isEmpty(record.getId())) {
        
        List<EntityDTO> users = QueryUtils.getRecordList("workgroup_user", "workgroup = " + record.getId());
        
        if ((users != null) && (!users.isEmpty())) {
            
            users.forEach( r -> r.doDelete() );
            
        }
    }
    
}

public static String getAccessGroupUsersAsString(Integer workGroupId){
    String result = "";
    
    if(workGroupId != null) {
        
        EntityDTO wGroup = new EntityDTO("workgroup", workGroupId);
        
        if(wGroup != null && !wGroup.isEmpty()) {
            result = getAccessGroupUsersAsString(wGroup);
        }
    }
    
    return result;
}

public static String getAccessGroupUsersAsString(EntityDTO record) {
    
    StringBuilder query = new StringBuilder();
        
        if (!CommonUtils.isEmpty(record.get("accessgroup"))) {
            
            List<Integer> users = AccessGroupUtils.getUserIds(record.getAsInteger("accessgroup"));
            users.forEach(r -> log.info("--------------------- " + r)); ///////////
            if ((users != null) && (!users.isEmpty())) {
                query.append(
                    users.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(",", "(", ")"))
                );
            }
            else {
                query.append("(-1)");
            }
        }
        
    log.info("####################### WORKGROUP ---- ORGUNIT ---- QUERY ---- " + query.toString());
    return query.toString();
}

public static String getQueryForOrgunitAccessGroup(EntityDTO record) {
    
    int init_orgunit = -1;
    boolean includeSubdivisions = record.getAsBooleanNullSub("subdivisions", false);
    
    if (!CommonUtils.isEmpty(record.get("orgunit")))
        init_orgunit = record.getAsInteger("orgunit");
    StringBuilder query = new StringBuilder();
        
    if (includeSubdivisions) {
        query.append("WITH RECURSIVE r AS ( ");
            query.append("SELECT r_orgunitid, parent_orgunit ");
            query.append("FROM v_r_orgunit ou ");
            query.append("WHERE r_orgunitid = " + init_orgunit + " ");

            query.append("UNION ");

            query.append("SELECT ou2.r_orgunitid, ou2.parent_orgunit ");
            query.append("FROM v_r_orgunit ou2 ");
                query.append("JOIN r ");
                    query.append("ON ou2.parent_orgunit = r.r_orgunitid ");
        query.append(") ");
        query.append("SELECT u.user_id AS userId FROM r JOIN v_r_orgunituser ouu ON r.r_orgunitid = ouu.orgunit JOIN system.ipuser u ON u.user_id = ouu.user ");
        query.append("WHERE lower(u.status) in ('registered', 'invited') ");
    }
    else {
        // query.append("SELECT ouu.user as userId FROM v_r_orgunituser ouu WHERE ouu.orgunit = " + init_orgunit);
        // query.append("select u.user_id as userId from  system.ipuser u where lower(u.status) = 'registered' and u.orgunit = " + init_orgunit); ///////////////////////////////////////////
        query.append("SELECT u.user_id AS userId FROM  system.ipuser u INNER JOIN v_r_orgunituser ouu ON u.user_id = ouu.user WHERE ouu.orgunit = " + init_orgunit + " ");
        query.append("AND lower(u.status) in ('registered', 'invited') ");
    }
    return query.toString();
}

public static String getQueryForAccessGroup(EntityDTO record) {
    StringBuilder query = new StringBuilder();
    
    query.append("SELECT u.user_id AS userId FROM system.ipuser u INNER JOIN v_workspace_user ws_u ON ws_u.userid = u.user_id INNER JOIN v_workgroup_user wg_u ON wg_u.userx = ws_u.workspace_userid WHERE wg_u.workgroup = " + record.getId() + " ");
    
    query.append("AND lower(u.status) in ('registered', 'invited') ");
    
    return query.toString();
}

public static void createAccessGroup(EntityDTO record) {
        StringBuilder queryAG = new StringBuilder();
        queryAG.append(getQueryForAccessGroup(record));
        
        EntityDTO group = null;
        if (CommonUtils.isEmpty(record.get("access_group"))) {
            log.info("####################### WORKGROUP ---- CREATE ACCESS GROUP");
            group = new EntityDTO("accessgroup");
            group.set("grouptype", "QUERY");
            group.set("groupquery", queryAG.toString());
            group.set("displayname", record.getAsString("displayname") + " - РГ");
            
            group.doInsert();
            
            record.set("access_group", group.getKeyValue());
            
            log.info("####################### WORKGROUP ---- ACCESS GROUP CREATED ---- " + group.getKeyValue());
        }
        else {
            group = new EntityDTO("accessgroup", record.getAsInteger("accessgroup"));
            if (CommonUtils.isEmpty(group))
                throw new ValidationException("Группа доступа текущей рабочей группы не существует!");
            
            // if (!CommonUtils.isSame(group.getAsString("grouptype"), "query"))
            //     group.set("grouptype", "query");
            
            if (CommonUtils.isEmpty(group.get("groupquery"))) {
                group.set("groupquery", queryAG.toString());
            }
            else {
                if (!CommonUtils.isSame(group.getAsString("groupquery"), queryAG.toString()))
                    group.set("groupquery", queryAG.toString());
            }
            
            {
                log.info("####################### WORKGROUP ---- UPDATE ACCESS GROUP ---- " + group.getKeyValue());
                
                group.doUpdate();
                
                // //delete records from workgroup_user
                // deleteWorkGroupUsers(record);
                // // create records in table workgroup_user
                // createWorkGroupUsersFromAccessGroup(record);
                
                log.info("####################### WORKGROUP ---- ACCESS GROUP UPDATED ---- " + group.getKeyValue());
            }
        }
        
}

public static void removeAccessGroup(EntityDTO record) {
    if (!CommonUtils.isEmpty(record.get("access_group"))) {
        EntityDTO group = new EntityDTO("accessgroup", record.getAsInteger("access_group"));
        if (!CommonUtils.isEmpty(group)) {
            log.info("####################### WORKGROUP ---- REMOVE ACCESS GROUP ---- " + group.getKeyValue());
            group.doDelete();
            log.info("####################### WORKGROUP ---- ACCESS GROUP REMOVED");
        }
    }
    
    // //delete records from workgroup_user
    // deleteWorkGroupUsers(record);
}


public static Integer getWorkgroupUsersCount(Integer wGroupId) {
    Integer usCount = 0;
    
    if (wGroupId == null)
        return -1;
        
    EntityDTO wGroup = new EntityDTO("workgroup", wGroupId);
    if (!wGroup.isFieldEmpty("formation_type")) {
        if (CommonUtils.isSame(wGroup.getAsString("formation_type"), "man")) {
            usCount = QueryUtils.getCount("workgroup_user", "workgroup = " + wGroup.getId());
        }
        else {
            if (!wGroup.isFieldEmpty("accessgroup")) {
                usCount = AccessGroupUtils.getUserIds(wGroup.getAsInteger("accessgroup")).size();
            }
        }
            
    }
    
    return usCount;
}

public static boolean hasChildWorkgroups(EntityDTO record) {
    List<EntityDTO> workgroups = QueryUtils.getRecordList("workgroup", "parent_workgroup="+record.get("workgroupid"));
    if (workgroups.size() > 0) {
        return true;
    } else {
        return false;
    }
    
}

public static boolean hasParentWorkgroup(EntityDTO record) {
    EntityDTO workgroup = QueryUtils.getRecord("workgroup", "workgroupid="+record.get("parent_workgroup"));
    if (workgroup != null) {
        return true;
    } else {
        return false;
    }
}

public static EntityDTO getParentWorkgroup(EntityDTO record) {
    EntityDTO workgroup = QueryUtils.getRecord("workgroup", "workgroupid="+record.get("parent_workgroup"));
    if (workgroup != null) {
        return workgroup;
    } else {
        return null;
    }
}

public static Integer getChildWorkgroupCount(EntityDTO record) {
    return QueryUtils.getCount("workgroup", "parent_workgroup="+record.get("workgroupid"));
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}