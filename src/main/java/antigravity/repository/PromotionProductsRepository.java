package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;



public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {

    List<PromotionProducts> findByProductIdAndPromotionIdIn(int productId, int[] couponIds);
}
