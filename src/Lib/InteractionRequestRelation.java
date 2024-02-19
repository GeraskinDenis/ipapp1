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

public class InteractionRequestRelation {

private static final Log log = Log.getLogger(InteractionRequestRelation.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static EntityDTO createRequestFromInteraction(EntityDTO interaction) throws Exception{
	EntityDTO request = new EntityDTO("request");
	String ouName = "";
	if (interaction.get("CI")!=null)
		request.set("CI", interaction.getAsInteger("CI"));
	if (interaction.get("CITYPE")!=null)
	    request.set("CITYPE", interaction.getAsInteger("CITYPE"));
	if (interaction.get("displayname")!=null)
	    request.set("displayname", interaction.getAsString("displayname"));
	if (interaction.get("PRIORITY")!=null)
	    request.set("PRIORITY",interaction.getAsInteger("PRIORITY"));
	String desc = null;
	if (!CommonUtils.isEmpty(interaction.get("DESCRIPTION")))
	    desc =  CommonUtils.nullSub(interaction.getAsString("DESCRIPTION"),"-");
	if (desc!=null)
	    desc = desc.replace("null","");
	String paramDescription = buildInteractionParams(interaction);
	if (!CommonUtils.isEmpty(paramDescription) && !CommonUtils.isSame(paramDescription,""))
	    request.set("DESCRIPTION","<h2>"+"Описание Заявки"+"</h2>"+CommonUtils.nullSub(desc,"")+"<h2>"+"Параметры Заявки"+"</h2>"+paramDescription);
	if (interaction.get("WORKGROUP")!=null)
	    request.set("WORKGROUP", interaction.getAsInteger("WORKGROUP"));
	if (interaction.get("template")!=null)
	    request.set("template", interaction.getAsInteger("template"));
	if (interaction.get("service_citype")!=null)
	    request.set("service_citype", interaction.getAsString("service_citype"));
	if (interaction.get("service")!=null)
	    request.set("service", interaction.getAsInteger("service"));
	if (interaction.get("contact")!=null)
	    request.set("initiator", interaction.getAsInteger("contact"));
	if (interaction.get("location")!=null)
	    request.set("location", interaction.getAsInteger("location"));
	request.set("INTERACTION",interaction.getId());
	if (interaction.get("DEADLINE")!=null)
	    request.set("DEADLINE",interaction.getAsDateTime("DEADLINE"));
	request.doInsert();
	if (request.getId()!=null)
		return request;
	else
		return null;
}

public static Boolean checkOnlyOneRelatedObjectExist(EntityDTO record) throws Exception{
    if (CommonUtils.isSame(record.getAsString("category"),"incident")){
        List<EntityDTO> incList = Lib.ITSMIncidentUtils.getActiveInteractionIncList(record);
        if (incList.size()==1)
            return (Lib.TaskUtils.getActiveTaskList(incList.get(0)).size()==1);
    }
    if (CommonUtils.isSame(record.getAsString("category"),"request")){
        List<EntityDTO> reqList = Lib.ITSMRequestUtils.getActiveInteractionReqList(record);
        if (reqList.size()==1){
            return (Lib.ITSMRequestUtils.getActiveTaskList(reqList.get(0)).size()==1);
        }
    }
    return false;
}

public static String buildInteractionParams(EntityDTO interaction){
    String desc = "";
     List<String> requiredFieldList = new ArrayList<String>();
    //List<EntityDTO> fieldList = QueryUtils.getRecordList("entityfield","isVirtual=0 and isActive=1 and sqltablename='interaction_b1' and entitytypeid="+interaction.getEntityTypeId());
    if (!CommonUtils.isEmpty(interaction.get("template"))){
        EntityDTO interTempl = QueryUtils.getRecordById("interaction_template",interaction.getAsInteger("template"));
        if (!CommonUtils.isEmpty(interTempl))
            requiredFieldList = interTempl.getStringArray("required_fields");
    }
    //log.info("requiredFieldList="+requiredFieldList);
    for (String reqField:requiredFieldList){
        //if (!CommonUtils.isEmpty(interaction.get(field.getAsString("fieldname"))))
        EntityDTO field = QueryUtils.getRecord("entityfield","fieldname='"+reqField+"' and isActive=1 and entitytypeid="+interaction.getEntityTypeId());
        if (!CommonUtils.isEmpty(field) && !CommonUtils.isEmpty(interaction.get(field.getAsString("fieldname"))))
            desc += "<p><em>"+field.getAsString("displayname")+": "+"</em><u>"+interaction.getDisplayValue(field.getAsString("fieldname"))+"</u></p>";
    }
    desc += "<p><em>Инициатор: </em><u>"+CommonUtils.nullSub(interaction.getDisplayValue("fullname")," - ")+"</u></p>";
    String externalUser = "Нет";
    if (!interaction.getAsBoolean("internal_contact"))
        externalUser = "Да";
    desc += "<p><em>Внешний сотрудник: </em><u>"+externalUser+"</u></p>";
    desc += "<p><em>Должность: </em><u>"+CommonUtils.nullSub(interaction.getDisplayValue("contact_title")," - ")+"</u></p>";
    desc += "<p><em>Телефон: </em><u>"+CommonUtils.nullSub(interaction.getDisplayValue("contact_phone")," - ")+"</u></p>";
    desc += "<p><em>Email: </em><u>"+CommonUtils.nullSub(interaction.getDisplayValue("contact_email")," - ")+"</u></p>";
    //desc += "<p><em>Описание: </em><u>"+interaction.getDisplayValue("description")+"</u></p>";
    return desc;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}