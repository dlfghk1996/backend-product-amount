package antigravity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.BizException;
import antigravity.common.util.MathUtil;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.enums.DiscountType;
import antigravity.enums.PromotionType;
import antigravity.model.request.ProductInfoRequest;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("상품 프로모션 적용 가격 테스트")
@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Spy
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionProductsRepository promotionProductsRepository;

    private Product product;

    private List<PromotionProducts> promotionProductsList;

    private ProductInfoRequest productInfoRequest;

    // Test Value
    private LocalDate useStartedAt = LocalDate.now().minusDays(3);
    private LocalDate useEndedAt = LocalDate.now().plusDays(3);

    // 값 초기화
    @BeforeEach
    void setUp() {
        product = Product.builder()
            .name("피팅노드상품")
            .id(1)
            .price(215000)
            .build();

        productInfoRequest = ProductInfoRequest.builder()
            .productId(2)
            .couponIds(new int[]{1, 2})
            .build();

        promotionProductsList = Arrays.asList(
            new PromotionProducts(1, createPromotionForCode(useStartedAt, useEndedAt), product),
            new PromotionProducts(2, createPromotionForCoupon(useStartedAt, useEndedAt), product)
        );
    }

    @Nested
    @DisplayName("사용자 요청값 유효성 검사")
    class request_value_invalid_test {

        @Test
        @DisplayName("존재하지 않는 상품일 경우 INVALID_REQUEST ResponseCode 반환.")
        void product_not_exists_exception_test() {

            when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));

            Optional<Product> optProduct = productRepository.findById(productInfoRequest.getProductId());
            BizException e = assertThrows(BizException.class
                , () -> optProduct.orElseThrow(()->new BizException(ResponseCode.INVALID_REQUEST))
                , "요청한 상품이 존재합니다.");

            assertThat(e.getResponseCode()).isEqualTo(ResponseCode.INVALID_REQUEST);
        }

        @Test
        @DisplayName("해당 상품에 적용된 프로모션이 없을 경우 PRODUCT_PROMOTION_NOT_FOUND ResponseCode 반환.")
        void product_not_exists_promotion_test() {

            List<Integer> reqList = IntStream.of(productInfoRequest.getCouponIds())
                .boxed()
                .collect(Collectors.toList());

            when(promotionProductsRepository.getProductPromotion(
                product.getId(),
                promotionProductsList.stream().map(pp->pp.getPromotion().getId()).collect(Collectors.toList())))
                .thenReturn(Optional.ofNullable(promotionProductsList));

            Optional<List<PromotionProducts>> optProduct = promotionProductsRepository.getProductPromotion(product.getId(), reqList);

            BizException e = assertThrows(BizException.class
                , () -> optProduct.orElseThrow(()->new BizException(ResponseCode.PRODUCT_PROMOTION_NOT_FOUND)));

            assertThat(e.getResponseCode()).isEqualTo(ResponseCode.PRODUCT_PROMOTION_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("쿠폰 적용 가능 여부 검사")
    class promotion_invalid_test {

        // 상품의 최소 금액보다 작은 경우 에러반환 (product)
        @Nested
        @DisplayName("isValidPrice 메소드는 ")
        class isValidPrice {

            @Test
            @DisplayName("상품 금액이 최소 금액 미만이면 MIN_PRODUCT_PRICE ResponseCode 반환")
            void product_price_min_text() {
                BizException exception = assertThrows(BizException.class,
                    () -> product.isValidPrice());
                assertEquals(ResponseCode.MIN_PRODUCT_PRICE, exception.getResponseCode());
            }

            @Test
            @DisplayName("상품 금액이 최대 금액 이상이면 MAX_PRODUCT_PRICE ResponseCode 반환")
            void product_price_max_text() {
                BizException exception = assertThrows(BizException.class,
                    () -> product.isValidPrice());
                assertEquals(ResponseCode.MAX_PRODUCT_PRICE, exception.getResponseCode());
            }
        }

        @Test
        @DisplayName("쿠폰 사용 기간이 유효하지 않으면 INVALID_PROMOTION ResponseCode 반환")
        void promotion_test() {
            BizException exception = assertThrows(BizException.class,
                () ->  promotionProductsList.forEach(pp-> pp.getPromotion().isAvailableDate()));

            assertEquals(ResponseCode.INVALID_PROMOTION_DATE, exception.getResponseCode());
        }
    }
    @Nested
    @DisplayName("프로모션 적용 가격 계산")
    class final_product_price{
        @Test
        @DisplayName("PROMOTION TYPE 별 할인율 계산 테스트")
        void product_promotion_price_test() {
            int discountPriceByPercent = PromotionType.CODE.calculate(product.getPrice(),30);
            assertEquals(discountPriceByPercent, 150500);

            int discountPriceByWon = PromotionType.COUPON.calculate(product.getPrice(),30000);
            assertEquals(discountPriceByWon, 30000);

            int totalDiscountPrice = promotionProductsList.stream()
                .mapToInt(pp-> pp.getPromotion().getPromotionType()
                    .calculate(product.getPrice(), pp.getPromotion().getDiscountValue()))
                .sum();

            // 최종 할인 금액
            assertEquals((discountPriceByPercent+discountPriceByWon), totalDiscountPrice);
        }

        @Test
        @DisplayName("단위 기준 절삭 테스트")
        void price_round_test() {
            assertThat(MathUtil.roundNumber(185200, 1000)).isEqualTo(185000);
        }

        @Test
        @DisplayName("최종가격을 반환한다.")
        void when_priceInformationIsValid_then_returnFinalPrice() {
            int discountPrice = 18000;
            assertThat(product.calculateFinalPrice(discountPrice))
                .isEqualTo(MathUtil.roundNumber(product.getPrice()-discountPrice,1000));
        }
    }

    private Promotion createPromotionForCode(LocalDate useStartedAt, LocalDate useEndedAt) {
        return Promotion.builder()
            .id(1)
            .promotionType(PromotionType.COUPON)
            .discountType(DiscountType.PERCENT)
            .discountValue(3000)
            .useStartedAt(useStartedAt)
            .useEndedAt(useEndedAt)
            .name("30000원 할인쿠폰")
            .build();
    }

     Promotion createPromotionForCoupon(LocalDate useStartedAt, LocalDate useEndedAt) {
        return Promotion.builder()
            .id(2)
            .promotionType(PromotionType.COUPON)
            .discountType(DiscountType.WON)
            .discountValue(15)
            .useStartedAt(useStartedAt)
            .useEndedAt(useEndedAt)
            .name("15% 할인코드")
            .build();
    }
}
