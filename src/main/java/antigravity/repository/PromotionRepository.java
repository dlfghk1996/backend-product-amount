package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;



public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

}