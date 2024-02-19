package ide;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.ip.server.cache.CacheManager;
import ru.ip.server.database.HibernateUtil;
import ru.ip.server.datasource.EntityDescription;
import ru.ip.server.entity.EntityDTO;
import ru.ip.server.integration.mdm.XmlConverter;
import ru.ip.server.integration.v2.elements.Element;
import ru.ip.server.integration.v2.elements.ObjectElement;
import ru.ip.server.logging.Log;
import ru.ip.server.module.cms.RSMUtils;
import ru.ip.server.security.SessionContextHolder;
import ru.ip.server.support.TableUtils;
import ru.ip.server.utils.*;
import ru.ip.server.utils.QueryUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RunAnyCode {

    private static final Log log = Log.getLogger(RunAnyCode.class.getName());

    public static void main(String[] args) throws Exception {

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.WARN);
        HibernateUtil.startSystemFactory();
        SessionContextHolder.setSystemContext(89);

    }

}
