package antigravity.common.util;

import java.time.LocalDate;

public class DateUtil {

    /**
     * 오늘 일자가 startDate 와 endDate 의 범위 내에 있는지 검사
     *
     * @param startDate LocalDate
     * @param endDate LocalDate
     * @return boolean
     */
    public static boolean isBetweenCurrentDate(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        return startDate.compareTo(today) <= 0 && !(endDate.compareTo(today) < 0);
    }
}
