package antigravity.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private Date useStartedAt; // 쿠폰 사용가능 시작 기간
    private Date useEndedAt; // 쿠폰 사용가능 종료 기간
}
