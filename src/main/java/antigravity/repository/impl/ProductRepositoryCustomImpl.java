package antigravity.repository.impl;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.QProduct;
import antigravity.repository.ProductRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    static QProduct product = QProduct.product;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Product> findProduct(int productId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(product)
            .where(product.id.eq(productId)).fetchOne());
    }
}
