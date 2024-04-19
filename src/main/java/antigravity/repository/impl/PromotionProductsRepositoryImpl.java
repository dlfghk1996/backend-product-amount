package antigravity.repository.impl;


import antigravity.domain.entity.PromotionProducts;
import antigravity.domain.entity.QProduct;
import antigravity.domain.entity.QPromotion;
import antigravity.domain.entity.QPromotionProducts;
import antigravity.repository.PromotionProductsRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PromotionProductsRepositoryImpl implements PromotionProductsRepositoryCustom {
    static QPromotionProducts promotionProducts = QPromotionProducts.promotionProducts;
    static QPromotion promotion = QPromotion.promotion;
    static QProduct products = QProduct.product;

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<List<PromotionProducts>> getProductPromotion(int productId, List<Integer> promotionIdList) {
        return
            Optional.ofNullable(queryFactory
                .selectFrom(promotionProducts)
                .innerJoin(promotion)
                .on(promotionProducts.id.eq(promotion.id))
                .innerJoin(products)
                .on(promotionProducts.product.id.eq(products.id))
                .where(promotionProducts.product.id.eq(productId)
                    .and(promotionProducts.promotion.id.in(1,2)))
                .fetchJoin()
                .fetch());
    }
}
