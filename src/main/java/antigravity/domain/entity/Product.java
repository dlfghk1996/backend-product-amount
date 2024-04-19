package antigravity.domain.entity;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.AntigravityException;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    // 최소 상품가격, 최대 상품가격 검사
    public void isValidPrice(){
        if(price < 10000) {
            throw new AntigravityException(ResponseCode.MIN_PRODUCT_PRICE);
        }else if(price > 10000000) {
            throw new AntigravityException(ResponseCode.MAX_PRODUCT_PRICE);
        }
    }

    /**
     * 천단위 절삭 계산
     * @return 확정 상품 가격
     */
    public int getFinalPrice(int totalDiscountPrice){
        int finalPrice = this.price - totalDiscountPrice;
        System.out.println(finalPrice);
        if(finalPrice > 0) {
            BigDecimal conversionPrice = new BigDecimal(finalPrice);
            return conversionPrice.setScale(-3, BigDecimal.ROUND_DOWN).intValue();
        }
        return 0;
    }
}
