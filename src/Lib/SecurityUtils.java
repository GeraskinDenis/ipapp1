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

public class SecurityUtils {

private static final Log log = Log.getLogger(SecurityUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void createOUAccessGroup(EntityDTO ou, List<String> roles) throws Exception{
	for (int i=0;i<roles.size();i++){
		EntityDTO group = new EntityDTO("ACCESSGROUP");
		EntityDTO role = QueryUtils.getRecord("ROLE","SHORTNAME='"+ roles.get(i)+"'"); 
		if (role!=null){
			group.set("ROLE", role.getKeyValue());
			group.set("DISPLAYNAME", role.getAsString("DISPLAYNAME")+"_"+ou.getAsString("DISPLAYNAME"));
			if ("group".equals(ou.getAsString("ORGUNITTYPE"))){
				EntityDTO parentOU = new EntityDTO("R_ORGUNIT",ou.getAsInteger("PARENT_ORGUNIT"));
				if (parentOU!=null){
					group.set("DISPLAYNAME", role.getAsString("DISPLAYNAME")+"_"+ou.getAsString("DISPLAYNAME")+"_"+parentOU.getAsString("DISPLAYNAME"));
				}
			}
			group.set("GROUPTYPE", "QUERY");
			String query = "select user as userId from v_r_orgunituser ou inner join v_user u on (u.USERID = ou.user) inner join iprole r on (u.roleid = r.role_id)"; 
			query+=" where ou.orgunit = "+ou.getAsInteger("R_ORGUNITID")+" and r.short_name = '"+roles.get(i)+"'";
			group.set("GROUPQUERY", query);
			group.set("ORGUNIT", ou.getAsInteger("R_ORGUNITID"));
			if (!QueryUtils.isExist("ACCESSGROUP","ORGUNIT="+ou.getAsInteger("R_ORGUNITID")+" AND ROLE="+role.getKeyValue()))
				group.doInsert();
		}
	}
}

public static void deleteOUAccessGroup(EntityDTO ou) throws Exception{
	List<EntityDTO> groupList = QueryUtils.getRecordList("ACCESSGROUP","ORGUNIT="+ou.getAsInteger("R_ORGUNITID"));
	for (EntityDTO group:groupList)
		group.doDelete();
}

public static Integer getWorkspaceUserFromUser(Integer userId){
    EntityDTO wpUser = QueryUtils.getRecord("workspace_user","userid="+userId);
    if (!CommonUtils.isEmpty(wpUser)){
        log.info("getWorkspaceUserFromUser wpUserId ="+wpUser.getId());
        return wpUser.getId();
    }
    return null;
}

public static Integer getCurrentWorkspaceUser(){
   return getWorkspaceUserFromUser(ContextUtils.getCurrentUserId());
}

public static Integer getUserFromWorkSpaceUser(Integer workspaceUserId){
    if (CommonUtils.isEmpty(workspaceUserId))
        return null;
    EntityDTO wpUser = QueryUtils.getRecordById("workspace_user",workspaceUserId);
    if (!CommonUtils.isEmpty(wpUser) && !CommonUtils.isEmpty(wpUser.get("userid")))
        return wpUser.getAsInteger("userid");
    return null;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}