package antigravity.service;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.AntigravityException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ProductService {
    private final ProductRepository repository;
    private final PromotionProductsRepository promotionProductsRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        // 1. 상품 조회
        Product product = repository.findById(request.getProductId())
            .orElseThrow(()-> new AntigravityException(ResponseCode.INVALID_REQUEST));

        // 2. 상품 가격 유효성 검사
        product.isValidPrice();

        int originPrice = product.getPrice();

        // 2. 프로모션 조회
        List<PromotionProducts> promotionProductsList = promotionProductsRepository
            .getProductPromotion(product.getId(),
                IntStream.of(request.getCouponIds()).boxed().collect(Collectors.toList()))
            .orElseThrow(()-> new AntigravityException(ResponseCode.PRODUCT_PROMOTION_NOT_FOUND));

        System.out.println(promotionProductsList.size());

        // 3. 프로모션 적용
        int discountPrice = 0;
        for(PromotionProducts promotionProducts: promotionProductsList){
            Promotion promotion = promotionProducts.getPromotion();
            // 3-1. 프로모션 적용 유효성 검사.
            promotion.isAvailableDate();
            System.out.println(promotion.getDiscountType());
            // 3-2. 총 할인가격 계산
            discountPrice += promotion.getDiscountType().calculate(originPrice, promotion.getDiscountValue());
        }

        System.out.println(discountPrice);

        // 5. 최종가격 구하기
        return ProductAmountResponse.builder()
            .name(product.getName())
            .originPrice(originPrice)
            .discountPrice(discountPrice)
            .finalPrice(product.getFinalPrice(discountPrice))
            .build();
    }
}
