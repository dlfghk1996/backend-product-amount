package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import java.util.List;
import java.util.Optional;

public interface PromotionProductsRepositoryCustom {
    Optional<List<PromotionProducts>> getProductPromotion(int productId, List<Integer> promotionIdList);
}
