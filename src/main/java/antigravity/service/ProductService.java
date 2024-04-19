package antigravity.service;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.BizException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final PromotionProductsRepository promotionProductsRepository;

    /**
     * 상품 가격 추출 서비스
     * 
     * @param request ProductInfoRequest
     * @return ProductAmountResponse
     */
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        // 1. 상품 조회
        Product product = repository.findById(request.getProductId())
            .orElseThrow(() -> new BizException(ResponseCode.INVALID_REQUEST));

        // 2. 상품 가격 유효성 검사
        product.isValidPrice();

        int originPrice = product.getPrice(); // 조회한 상품의 원가격

        // 3. 프로모션 조회
        List<PromotionProducts> promotionProductsList = promotionProductsRepository
            .getProductPromotion(product.getId(),
                IntStream.of(request.getCouponIds()).boxed().collect(Collectors.toList()))
            .orElseThrow(() -> new BizException(ResponseCode.PRODUCT_PROMOTION_NOT_FOUND));

        // 4. 프로모션 적용
        int discountPrice = 0;
        for (PromotionProducts promotionProducts : promotionProductsList) {
            Promotion promotion = promotionProducts.getPromotion();
            // 4-1. 프로모션 기간 유효성 검사
            promotion.isAvailableDate();
            // 4-2. 총 할인가격 계산
            discountPrice += promotion.getDiscountType()
                .calculate(originPrice, promotion.getDiscountValue());
        }

        // 5. 프로모션 적용 상품 정보 반환
        return ProductAmountResponse.builder()
            .name(product.getName())
            .originPrice(originPrice)
            .discountPrice(discountPrice)
            .finalPrice(product.calculateFinalPrice(discountPrice))
            .build();
    }
}
