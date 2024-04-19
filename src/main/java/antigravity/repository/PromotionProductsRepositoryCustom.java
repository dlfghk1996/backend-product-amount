package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import java.util.List;
import java.util.Optional;

public interface PromotionProductsRepositoryCustom {

    /**
     * 요청 상품과 프로모션에 일치하는 프로모션 목록 조회
     *
     * @param productId int
     * @param promotionIdList List
     * @return Optional
     */
    Optional<List<PromotionProducts>> getProductPromotion(int productId, List<Integer> promotionIdList);
}
