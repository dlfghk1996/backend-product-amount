package antigravity.repository.impl;


import antigravity.domain.entity.PromotionProducts;
import antigravity.domain.entity.QProduct;
import antigravity.domain.entity.QPromotion;
import antigravity.domain.entity.QPromotionProducts;
import antigravity.repository.PromotionProductsRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public Optional<List<PromotionProducts>> getProductPromotion(int productId,
        List<Integer> promotionIdList) {
        // 문제 해석에 모호함이 있어 두 케이스 모두 작성하였습니다.
        // CASE1. 선택된 프로모션 중 한 개 이상 조회될 경우 PASS

        // CASE2. 선택된 프로모션이 모두 조회될 경우 PASS
        return
            Optional.ofNullable(queryFactory
                .selectFrom(promotionProducts)
                .innerJoin(promotion)
                .on(promotionProducts.id.eq(promotion.id))
                .innerJoin(products)
                .on(promotionProducts.product.id.eq(products.id))
                .where(promotionProducts.product.id.eq(productId)
                    .and(promotionProducts.promotion.id.in(promotionIdList)))
                .fetchJoin()
                .fetch());
    }

//    public BooleanExpression buildDynamicQuery(List<Integer> promotionIdList) {
//        BooleanBuilder builder = new BooleanBuilder();
//        for (Integer id : promotionIdList) {
//            builder.and(promotionProducts.promotion.id.eq(id));
//        }
//        return builder;
//    }
}
