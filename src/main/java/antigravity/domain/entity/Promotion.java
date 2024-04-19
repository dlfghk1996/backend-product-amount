package antigravity.domain.entity;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.BizException;
import antigravity.common.util.DateUtil;
import antigravity.enums.DiscountType;
import antigravity.enums.PromotionType;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('COUPON','CODE')", nullable = false)
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('WON','PERCENT')", nullable = false)
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인

    private int discountValue; // 할인 금액 or 할인 %

    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간

    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

    /**
     * 프로모션 유효기간 검사
     */
    public void isAvailableDate() {
        if (DateUtil.isBetweenCurrentDate(this.useStartedAt, this.useEndedAt)) {
            throw new BizException(ResponseCode.INVALID_PROMOTION);
        }
    }
}
