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

public class AdminUtils {

private static final Log log = Log.getLogger(AdminUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static void deleteReports(String query)  throws Exception{
	List<EntityDTO> reportList = QueryUtils.getRecordList("REPORTTYPE", query); 
	for (EntityDTO report:reportList){
		List<EntityDTO> relationList = QueryUtils.getRecordList("REPORTRELATION", "SOURCEREPORTTYPEID="+report.getKeyValue()+" OR DESTINATIONREPORTTYPEID ="+report.getKeyValue());
		for (EntityDTO relation:relationList){
			List<EntityDTO> relationMappingList = QueryUtils.getRecordList("REPORTRELATIONENTRY","REPORTRELATIONID ="+ relation.getKeyValue());
			for (EntityDTO relationMapping:relationMappingList){
				relationMapping.doDelete();
			}
			relation.doDelete();
		}
		List<EntityDTO> dashReportList = QueryUtils.getRecordList("DASHBOARDREPORT", "REPORTTYPEID="+report.getKeyValue());
		for (EntityDTO dashReport:dashReportList)
			dashReport.doDelete();
		List<EntityDTO> fieldReportList = QueryUtils.getRecordList("REPORTFIELD", "REPORTTYPEID="+report.getKeyValue());
		for (EntityDTO fieldReport:fieldReportList)
			fieldReport.doDelete();
		ru.ip.server.database.DBFactory.getInstance().getCompositeTableDAO().deleteRecordsByQuery(11, "FORMTYPE IN ('REPORT_RESULT', 'REPORT_PARAM') AND  ENTITYID ="+report.getKeyValue());
		report.doDelete();
	}
}

public static void deleteMenu(String menuName) throws Exception{
	String menuIds = "";
EntityDTO menu = QueryUtils.getRecord("MENU", "SHORTNAME = '"+menuName+"'");
	if (menu!=null){
		menuIds= getChildMenusList(menu);
		menuIds+= menu.getKeyValue().toString(); 
		log.info(menuIds);
		ru.ip.server.database.DBFactory.getInstance().getCompositeTableDAO().deleteRecordsByQuery(21, "MENUID IN ("+menuIds+")");
	}
}

public static String getChildMenusList(EntityDTO menu) throws Exception{
	String ids = "";
	List<EntityDTO> menuList = QueryUtils.getRecordList("MENU", "PARENTMENUID ="+menu.getKeyValue());
	for (EntityDTO pm:menuList){
		ids+=getChildMenusList(pm);
		ids+=pm.get("MENUID")+",";
	}
	return ids;
}


public static void generateReportDefinitionFromEntityType(Integer entityId) throws Exception{
		System.out.println("start work");
		EntityDTO ent = QueryUtils.getRecord(1,"entitytypeid ="+entityId);
		System.out.println("start work2");
	    if (ent!=null){
	        EntityDTO report = new EntityDTO("REPORTTYPE");    
	        report.set("displayname", ent.get("displayname"));
	        report.set("reportstyle","simpleTable");
	        String query = "select ";
	        report.doInsert();
	        List<EntityDTO> fieldList = QueryUtils.getRecordList("entityfield", "isactive =1 and entitytypeid ="+entityId);
	        int i = 0;
	        for (EntityDTO field:fieldList){
	            System.out.println("поле найдено -"+field.get("displayname"));
	            if (!Arrays.asList("relatedhistorylist", "attachments","workflowid","shortname").contains(field.get("fieldname")) &&
	            	!"VIRTUAL".equals(field.getAsString("fieldtype"))){ 

		            i++;
		        	EntityDTO repField = new EntityDTO("REPORTFIELD");
		            repField.set("isresultfield", true);
		            repField.set("fieldname", field.get("fieldname"));
		            repField.set("displayname", field.get("displayname"));
		            if (field.get("fieldformatid")!=null)
		                repField.set("fieldformatid", field.getAsInteger("fieldformatid"));
		            repField.set("reporttypeid", report.getKeyValue());
		            /*if (field.get("isvisible")!=null)
		                repField.set("isvisible", field.getAsBoolean("isvisible"));
		            else
		                repField.set("isvisible", false);
		            */
		            repField.set("sortorder", i);
		            repField.doInsert();
		            query+=repField.get("fieldname")+", ";
	            }
	        }
	        query = query.substring(0, query.length()-2)+" from v_"+ent.get("tablename");
	        System.out.println("запрос -"+query);
	        report.set("sql_query",query);
	        report.doUpdate();
	    }
	}
	
public static void deleteRunningSpeed(String reqTempl){
	List<EntityDTO> rsList = QueryUtils.getRecordList("","tech_template in (select ttt.task_templateid from fkv39o1z.v_request_template rt inner join fkv39o1z.v_techtask_template tt on (tt.request_template = rt.request_templateid) inner join fkv39o1z.v_task_template ttt on (ttt.techtask_template = tt.techtask_templateid) where rt.parent_request_template = "+reqTempl+")");
	log.info("кол-во коэф-ов ="+rsList.size());
    
}

public static void deleteKmArticle(String articleName) throws Exception{
	String articleIds = "";
    EntityDTO article = QueryUtils.getRecord("km_article", "SHORTNAME = '"+articleName+"'");
	if (article!=null){
		articleIds= getChildKmArticleList(article);
		articleIds+= article.getKeyValue().toString(); 
		log.info(articleIds);
		ContextUtils.addMessage("article ids:"+articleIds);
		ru.ip.server.database.DBFactory.getInstance().getCompositeTableDAO().deleteRecordsByQuery("km_article", " km_articleid IN ("+articleIds+")");
	}
}

public static String getChildKmArticleList(EntityDTO article) throws Exception{
	String ids = "";
	List<EntityDTO> articleList = QueryUtils.getRecordList("km_article", "parent_article ="+article.getKeyValue());
	for (EntityDTO pm:articleList){
		ids+=getChildKmArticleList(pm);
		ids+=pm.get("km_articleid")+",";
	}
	return ids;
}

public static String getChildKmArticleListByQuery(Integer parentArticleId) throws Exception {
    String ids = "";
    String query = "WITH RECURSIVE r AS (" +
                " SELECT km_articleid, parent_article" +
                " FROM system.v_km_article" +
                " WHERE parent_article =:_parent_article_id" +
                " " +
                " UNION" +
                " " +
                " SELECT article.km_articleid, article.parent_article" +
                " FROM system.v_km_article article " +
                "  JOIN r" +
                "   ON article.parent_article = r.km_articleid" +
                ")" +
                " SELECT * FROM r";
                
    List<EntityDTO> articleList = QueryUtils.selectSql(query).bind("_parent_article_id", parentArticleId).getRecordList();
    for (EntityDTO pm : articleList) {
        //ids += getChildKmArticleListByQuery(pm.getId());
        ids += pm.get("km_articleid") + ",";
    }
    if (ids.length() > 0)
    ids = ids.substring(0, ids.length() - 1);
    return ids;
}

public static Boolean isMyWorkspace(EntityDTO record){
    log.info("curr workspaceid = "+ContextUtils.getCurrentUser().getWorkspaceId());
    log.info("record workspaceid = "+record.getWorkspaceId());
    return ContextUtils.getCurrentUser().getWorkspaceId().equals(record.getWorkspaceId());
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}