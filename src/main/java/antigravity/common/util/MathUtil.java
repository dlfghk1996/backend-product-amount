package antigravity.common.util;

import java.math.BigDecimal;

public class MathUtil {

    /**
     * 단위 절삭
     *
     * @param number int
     * @param scale  int
     * @return int
     */
    public static int roundNumber(int number, int scale) {
        // 절삭 기준값이 number 보다 작을 경우 절삭
        if (scale > number) {
            return number;
        }
        // 절삭 기준값의 자리수 계산
        int count = 0;
        while (scale > 0) {
            scale = scale / 10;
            count++;
        }
        BigDecimal conversionPrice = new BigDecimal(number);
        return conversionPrice.setScale(count * -1, BigDecimal.ROUND_DOWN).intValue();
    }
}
