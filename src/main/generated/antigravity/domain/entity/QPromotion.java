package antigravity.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPromotion is a Querydsl query type for Promotion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPromotion extends EntityPathBase<Promotion> {

    private static final long serialVersionUID = 686008544L;

    public static final QPromotion promotion = new QPromotion("promotion");

    public final EnumPath<antigravity.enums.DiscountType> discountType = createEnum("discountType", antigravity.enums.DiscountType.class);

    public final NumberPath<Integer> discountValue = createNumber("discountValue", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final EnumPath<antigravity.enums.PromotionType> promotionType = createEnum("promotionType", antigravity.enums.PromotionType.class);

    public final DateTimePath<java.util.Date> useEndedAt = createDateTime("useEndedAt", java.util.Date.class);

    public final DateTimePath<java.util.Date> useStartedAt = createDateTime("useStartedAt", java.util.Date.class);

    public QPromotion(String variable) {
        super(Promotion.class, forVariable(variable));
    }

    public QPromotion(Path<? extends Promotion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPromotion(PathMetadata metadata) {
        super(Promotion.class, metadata);
    }

}

