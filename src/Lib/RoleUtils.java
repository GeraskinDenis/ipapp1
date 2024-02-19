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

public class RoleUtils {

private static final Log log = Log.getLogger(RoleUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static Integer getRoleGroup(Integer workspaceId, String roleName) throws Exception{
    EntityDTO role = QueryUtils.getRecord("ROLE", "SHORTNAME = '"+roleName+"'");
    if (role!=null){    
        log.info("роль найдена");
        EntityDTO group = QueryUtils.getRecord("ACCESSGROUP", "WORKSPACEID="+workspaceId+" and ROLE="+role.getKeyValue());
        if (group!=null){
            log.info("определана группа ="+group.getKeyValue());
            System.out.println("определана группа ="+group.getKeyValue());
            return group.getKeyValue();
        }    
    }
    return null;
}
public static String getWorkspaceManagerName(EntityDTO record) throws Exception{
    Integer invitedUser = CommonUtils.nullSub(record.getAsInteger("updatedbyid"), record.getAsInteger("createdbyid"));
    if (invitedUser==null){
        EntityDTO wrkspc = new EntityDTO("workspace", record.getWorkspaceId());
        if (wrkspc!=null){
            invitedUser = wrkspc.getAsInteger("manager");
            if (invitedUser ==null)
                return "Руководитель пространства "+wrkspc.get("DISPLAYNAME");
        }
    }
    if (invitedUser!=null){
        EntityDTO userObj = new EntityDTO("user", invitedUser);
        if (userObj!=null)
            return CommonUtils.nullSub(userObj.get("fullname"),"")+" ("+userObj.get("email")+")";
    }
    return "";
}
public static boolean isUserPMOfRequest(Integer userId, EntityDTO request) throws Exception{
    if (request.get("parent_entityid")!=null){
        EntityDTO project = new EntityDTO("project", request.getAsInteger("parent_entityid"));
        if ((project!=null) && (project.get("project_manager")!=null) 
        && (userId!=null) && (userId.equals(project.getAsInteger("project_manager"))))
            return true;
    }
    return false;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}