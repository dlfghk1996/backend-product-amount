package antigravity.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 현재시간이 주어진 날짜 사이인지 비교
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isBetweenCurrentDate(Date startDate, Date endDate) {
        return startDate.after(new Date())
            && endDate.before(new Date());
    }
}
