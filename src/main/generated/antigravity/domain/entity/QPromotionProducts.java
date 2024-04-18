package antigravity.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPromotionProducts is a Querydsl query type for PromotionProducts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPromotionProducts extends EntityPathBase<PromotionProducts> {

    private static final long serialVersionUID = 44236388L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPromotionProducts promotionProducts = new QPromotionProducts("promotionProducts");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QProduct product;

    public final QPromotion promotion;

    public QPromotionProducts(String variable) {
        this(PromotionProducts.class, forVariable(variable), INITS);
    }

    public QPromotionProducts(Path<? extends PromotionProducts> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPromotionProducts(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPromotionProducts(PathMetadata metadata, PathInits inits) {
        this(PromotionProducts.class, metadata, inits);
    }

    public QPromotionProducts(Class<? extends PromotionProducts> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
        this.promotion = inits.isInitialized("promotion") ? new QPromotion(forProperty("promotion")) : null;
    }

}

