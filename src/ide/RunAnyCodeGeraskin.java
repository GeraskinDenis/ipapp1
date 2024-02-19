package ide;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.ip.server.cache.CacheManager;
import ru.ip.server.database.HibernateUtil;
import ru.ip.server.entity.EntityDTO;
import ru.ip.server.logging.Log;
import ru.ip.server.logic.notification.Notification;
import ru.ip.server.security.SessionContextHolder;
import ru.ip.server.utils.ContextUtils;
import ru.ip.server.utils.QueryUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RunAnyCodeGeraskin {

    private static final Log log = Log.getLogger(RunAnyCodeGeraskin.class.getName());

    public static void main(String[] args) throws Exception {

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.WARN);
        HibernateUtil.startSystemFactory();
        SessionContextHolder.setSystemContext(89);

        EntityDTO record = new EntityDTO("4309:1");
        System.out.println(record.getAsString("displayname"));
//        record.getAsDateTime("???"); // TODO
//        EntityDTO record = new EntityDTO("998:8478");
//        System.out.println(record);
//        System.out.println(record.getAsString(""));
//        record.getFieldList().forEach((k, v)-> System.out.println(k + ": " + v));

//        WF_113_TransitionRules.be_agreed2done_agreed_for_application_metrology_experts_afterTranstion(record, record);
//        "chain_level_ctech_mo_specialist".equals(ContextUtils.getCurrentUserRoleShortName());

//        EntityDTO notification = QueryUtils.getRecordByShortName("notification", "NotificationAboutAddingToExperts");
//        EntityDTO notification = QueryUtils.getRecord("notification", "systemname = 'NotificationAboutAddingToExperts'");
//        System.out.println(Objects.isNull(notification));
//        System.out.println(notification.getId());
//        System.out.println(notification.getAsString("systemname"));

//
//        EntityDTO record = new EntityDTO("19:68");
//        System.out.println(record.getId());
//        System.out.println(record.getAsString("notificationname"));
//        System.out.println(record.getAsString("systemname"));

//        Notification notification = CacheManager.getInstance().getNotificationByIdOrName(68);
//        System.out.println(notification.getBodyTemplate());
//
//        EntityDTO dto = new EntityDTO("19:68");
//        System.out.println(dto.getAsString("shortname"));

//        List<EntityDTO> dtoList = QueryUtils.getRecordList("FKV39O1Z.R_ORGUNITATTR", "string_0 = 'ТЧР-2'");
//        List<EntityDTO> dtoList = QueryUtils.getRecordList("r_orgunit", "officialname = 'ТЧР-2'");
//        dtoList.forEach(System.out::println);

    }

}
