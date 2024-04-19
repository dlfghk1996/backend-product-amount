package antigravity.enums;

import antigravity.common.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum DiscountType implements BaseEnum {
    WON("WON", (price, discountPrice) -> discountPrice),
    PERCENT("PERCENT", (price, discountPrice) -> (int) (price * (discountPrice * 0.01)));

    @JsonValue
    private final String code;
    private BiFunction<Integer,Integer, Integer> expression;

    DiscountType(String code, BiFunction<Integer, Integer, Integer> expression) {
        this.code = code;
        this.expression = expression;
    }

    public int calculate(int price, int discountPrice){
        System.out.println("calculate 진입");
        return expression.apply(price, discountPrice);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }
}
