package ide;

import org.joda.time.DateTime;
import ru.ip.server.utils.CastUtils;
import ru.ip.server.utils.CommonUtils;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        DateTime dateTime = CastUtils.getDateTime(CommonUtils.getCurrentTimestamp());
        System.out.println(dateTime);
    }
}
