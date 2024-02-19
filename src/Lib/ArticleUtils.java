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

public class ArticleUtils {

private static final Log log = Log.getLogger(ArticleUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

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
        ids += pm.get("km_articleid") + ",";
    }
    if (ids.length() > 0)
    ids = ids.substring(0, ids.length() - 1);
    return ids;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}