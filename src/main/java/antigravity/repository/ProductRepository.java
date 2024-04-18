package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.PromotionProducts;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {

}
