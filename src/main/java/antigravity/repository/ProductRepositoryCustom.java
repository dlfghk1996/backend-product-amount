package antigravity.repository;

import antigravity.domain.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {

    Optional<Product> findProduct(int productId);
}
