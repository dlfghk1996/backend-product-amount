package antigravity.domain.entity;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.BizException;
import antigravity.common.util.MathUtil;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;

    /**
     * 최소 상품가격, 최대 상품가격 검사
     */
    public void isValidPrice() {
        if (price < 10000) {
            throw new BizException(ResponseCode.MIN_PRODUCT_PRICE);
        } else if (price > 10000000) {
            throw new BizException(ResponseCode.MAX_PRODUCT_PRICE);
        }
    }

    /**
     * 최종 가격 계산
     *
     * @param totalDiscountPrice int
     * @return int
     */
    public int calculateFinalPrice(int totalDiscountPrice) {
        int finalPrice = this.price - totalDiscountPrice;
        return finalPrice > 0 ? MathUtil.roundNumber(finalPrice, 1000) : 0;
    }
}
