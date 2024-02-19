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

public class TelegramIntegration {

private static final Log log = Log.getLogger(TelegramIntegration.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

// public static void getMessageList() throws Exception{
//     // Инициализируем клиент
//     ru.ip.server.integration.messenger.TelegramBot tBot = new ru.ip.server.integration.messenger.TelegramBot("607505927:AAF7f-AuvZwXdfo2XOodogvwvO7qpXD4SmM");
//     List<Map<String, Object>>  results = tBot.readMessages();
//     System.out.println("res: " + results);
//     for (Map<String, Object> inc:results){
//         if (!QueryUtils.isExist("interaction","externalid='"+inc.get("message_id")+"' and source='telegram'")){
//             log.info(inc.get("message_id") +" - такого обращения еще нет");
//             createInteraction(inc);
//         }
//     }
// }

// public static EntityDTO createInteraction(Map fieldMap) throws Exception{
// 	EntityDTO inter = new EntityDTO("interaction");
//     if (fieldMap.get("message_id")!=null)
//         inter.set("externalid", fieldMap.get("message_id"));
// 	inter.set("source", "telegram");
// 	inter.set("treatment", "telegram");
// 	inter.set("category","complaint");
// 	if (fieldMap.get("text")!=null)
// 	    inter.set("DISPLAYNAME",  fieldMap.get("text"));
// 	inter.set("priority", 3);

// 	if (fieldMap.get("chat_id")!=null){
// 	    inter.set("telegram_chatid", fieldMap.get("chat_id"));
// 	    EntityDTO user = QueryUtils.getRecord("user","workphone='"+fieldMap.get("chat_id")+"'");
// 	    if (user!=null){
// 	        inter.set("contact",user.getKeyValue());
// 	    }
// 	}
// 	inter.set("internal_contact",false);
//     if ((fieldMap.get("last_name")!=null) && (fieldMap.get("first_name")!=null))
//         inter.set("fullname", fieldMap.get("last_name")+" "+fieldMap.get("first_name"));

// 	inter.set("DEADLINE", new DateTime().plusDays(5));
// 	inter.set("service", 4);
// 	inter.doInsert();
// 	if (inter.getKeyValue()!=null)
// 		return inter;
// 	else
// 		return null;
// }


// public static void sendInteractionResolution(EntityDTO inter) throws Exception{
//     // Инициализируем клиент
//     ru.ip.server.integration.messenger.TelegramBot tBot = new ru.ip.server.integration.messenger.TelegramBot("607505927:AAF7f-AuvZwXdfo2XOodogvwvO7qpXD4SmM");
//     // описание полей для закрытия
//     Integer chatId = null;
//     if (inter.get("telegram_chatid")!=null){
//         chatId = Integer.parseInt(inter.getAsString("telegram_chatid"));
//         EntityDTO comment = QueryUtils.getRecord("comment","entitytypeid = 2200 and entityid="+inter.getKeyValue());
//         if (comment!=null)
//             tBot.sendMessage(chatId, comment.getAsString("comment"));
//     }
// }

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}