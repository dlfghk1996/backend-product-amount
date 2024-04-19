package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer>,
    PromotionProductsRepositoryCustom {

//    @Query("SELECT pp FROM PromotionProducts pp" +
//        " JOIN FETCH pp.product p" +
//        " JOIN pp.promotion pm" +
//        " WHERE p.id = :productId" +
//        " AND pm.id IN :promotionIds")
//    List<PromotionProducts> findProductsWithPromotionIn(@Param("productId") int productId,
//        @Param("promotionIds") List<Integer> promotionIds);
}
