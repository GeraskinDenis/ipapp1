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

public class PaymentUtils {

private static final Log log = Log.getLogger(PaymentUtils.class); // Logger

// START_CLASS_LIBRARY_CODE. Не удаляйте эту строку!

public static Integer calcPaymentAmountOnUserAdded(EntityDTO workspace, Integer addedUserCount){
    Integer amount = null;
    DateTime currentEndDate = workspace.getDateTime("subscription_end_date");
    if (CommonUtils.isEmpty(currentEndDate) || currentEndDate.isBefore(new DateTime()))
        return null;
    DateTime startDate = new DateTime().withTimeAtStartOfDay();
    Integer days  = Days.daysBetween(startDate,currentEndDate.withTimeAtStartOfDay()).getDays();
    
    Months m = Months.monthsBetween(startDate, currentEndDate);
    int numberOfMonth = m.getMonths();
    DateTime startDayinMonth = startDate.plusMonths(numberOfMonth);
    Integer numberOfDays  = Days.daysBetween(startDayinMonth,currentEndDate.withTimeAtStartOfDay()).getDays();
    Integer daysInMonth = currentEndDate.dayOfMonth().getMaximumValue();
    if (daysInMonth<startDayinMonth.dayOfMonth().getMaximumValue())
        daysInMonth = startDayinMonth.dayOfMonth().getMaximumValue();
    amount = (int)Math.floor(CommonUtils.nullSub(workspace.getAsInteger("price"),0)*(numberOfDays*1.0/daysInMonth+numberOfMonth)*addedUserCount);
    return amount;
}

public static Integer calcPaymentAmountOnSubscriptionRenew(EntityDTO workspace, Integer numberOfMonthes){
    Integer amount = null;
    if (CommonUtils.isEmpty(workspace) || CommonUtils.isEmpty(workspace.get("user_count")))
        return null;
    amount = (int)Math.floor(CommonUtils.nullSub(workspace.getAsInteger("price"),0) * numberOfMonthes * workspace.getAsInteger("user_count"));
    log.info("amount="+amount);
    return amount;
}

// END_CLASS_LIBRARY_CODE. Не удаляйте эту строку!
}