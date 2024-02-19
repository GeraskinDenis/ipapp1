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

public class RegistrationUtils {

private static final Log log = Log.getLogger(RegistrationUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void inviteUserBeforeInsert(EntityDTO record) throws Exception{
	//не системный и не админ
	//if (ContextUtils.getCurrentUser() != null && !User.getSystemUser(null).getLoginName().equals(ContextUtils.getCurrentUserLogin())){
    	String inpEmail = record.getAsString("email");
    	if (CommonUtils.isEmpty(inpEmail) || !ValidationUtils.validateEmailAddress(inpEmail)){
    		throw new Exception("Адрес электронной почты указан в некорректном формате.");
    	}
    	if (QueryUtils.isExist("USER", "email='" + inpEmail + "' and status not in ('UNREGISTERED')")){
    		throw new Exception("Пользователь с указанным адресом электронной почты уже существует.");
    	}
    	//Проверяем что нет незарегистрированного пользователя с таким email, если есть, то удаляем
    	if (QueryUtils.isExist("USER", "email='" + inpEmail + "' and status = 'UNREGISTERED'")){
    		EntityDTO oldUser = QueryUtils.getRecord("USER","email = '" + inpEmail + "'");
    		oldUser.doDelete();
    	}
    // 	record.set("status", "UNREGISTERED");
    	record.set("emailStatus", "UNCONFIRMED");
    	record.set("workspaceId", ContextUtils.getCurrentUser().getWorkspaceId());
    	record.set("confirmationCode",  CommonUtils.randString(128));
    	record.set("fullname", record.getAsString("email"));
    	record.set("loginname", record.getAsString("email"));
	//}
}
/*	
	public static void inviteUserAfterInsert(EntityDTO record) throws Exception{
		if(ContextUtils.getCurrentUser() != null && !User.getSystemUser(null).getLoginName().equals(ContextUtils.getCurrentUserLogin())){
			//не системный и не админ
			NotificationUtils.sendNotification(record.getKeyValue(), UserRestService.USER_INVITATION_TEMPLATE_ID, record, record);
		}
	}
*/	

// Возвращает кол-во лицензируемых пользователей в пространстве workspaceId
public static Integer getLicensedUserCount(Integer workspaceId){
    return QueryUtils.select().from("User").where("status in ('REGISTERED', 'INVITED', 'PREREGISTERED') and workspaceId=:_ws_id").bind("_ws_id", workspaceId).getCount();
}

// Проверяет есть ли возможность зарегистрировать еще одного пользователя в пространстве workspaceDTO
public static Boolean checkInvitationAvailability(EntityDTO workspaceDTO){
    
    Integer allowedCount = workspaceDTO.getAsInteger("user_count");
    Integer licensedCount = getLicensedUserCount(workspaceDTO.getId());
    
    return allowedCount>licensedCount;
    
}

public static void inviteUser(EntityDTO workspaceDTO) throws Exception{
    
    if (CommonUtils.isEmpty(workspaceDTO)){
        return;
    }
    
    if (workspaceDTO.isFieldEmpty("invite_user_email")){
        return;
    }
    
    if (!checkInvitationAvailability(workspaceDTO)){
        throw new ValidationException("Для приглашения новых пользователей измените количество пользователей на вкладке 'Управление подпиской' или деактивируйте текущих пользователей");
    }
    
	EntityDTO user = new EntityDTO("user");
    user.set("email", workspaceDTO.getAsString("invite_user_email"));
    user.set("fullname", workspaceDTO.getAsString("invite_user_email"));
    user.set("workspaceId", workspaceDTO.getId());
    user.doInsert();
    
}

public static void makeWorkspaceDataReadOnly(EntityDTO workspace) throws Exception{
    log.info("Начало RegistrationUtils.makeWorkspaceDataReadOnly");
    List<EntityDTO> tableList = QueryUtils.getRecordList("entitytype","workspaceid="+workspace.getKeyValue()+" and isactive = 1 and dstype='table'");
    log.info("RegistrationUtils, кол-во таблиц = "+tableList.size());
    String tables = "";
    for (EntityDTO tbl:tableList)
    	tables += tbl.getKeyValue()+",";
    if ((tables!="") && (tables.length()>1)){
    	log.info("список таблиц = "+tables);
    	tables = tables.substring(0, tables.length()-1);
    	List<EntityDTO> accessMapList = QueryUtils.getRecordList("entityaccessmap","entitytypeid in ("+tables+")");
    	log.info("найдено количество accessmap ="+accessMapList.size());
    	for (EntityDTO accessMap:accessMapList){
    		accessMap.set("cancreate",false);
    		accessMap.set("canupdate",false);
    		accessMap.doUpdate();
    	}
    }
}  

public static void deactivateWorkspaceUsers(EntityDTO workspace) throws Exception{
    List<EntityDTO> wrkspUsers = QueryUtils.getRecordList("user","workspaceid="+workspace.getKeyValue()+" and status = 'REGISTERED'");
    for (EntityDTO wrkspUser:wrkspUsers){
        if (workspace.getAsInteger("manager")!=wrkspUser.getKeyValue()){
            wrkspUser.set("status","DEACTIVATED");
            wrkspUser.doUpdate();
        }
    }
}

public static void inviteUserSendEmail(EntityDTO record) {
    // 39 - ID шаблона оповещения
	ru.ip.server.utils.NotificationUtils.sendNotification(record.getKeyValue(), (Integer) 39, record, record);
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}