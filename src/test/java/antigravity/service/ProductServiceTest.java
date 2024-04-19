package antigravity.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.AntigravityException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.enums.DiscountType;
import antigravity.enums.PromotionType;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito.Then;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    //@Mock
    @Spy
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionProductsRepository promotionProductsRepository;

    private Product product;

    private List<PromotionProducts> promotionProducts;

    private ProductInfoRequest productInfoRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
            .name("피팅노드상품")
            .id(1)
            .price(215000)
            .build();

        productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[] {1, 2})
            .build();

        promotionProducts = Arrays.asList(
            new PromotionProducts(1, new Promotion(), product),
            new PromotionProducts(2, new Promotion(), product)
        );
    }

    @Nested
    @DisplayName("요청값 유효성 검사")
    class request_value_invalid_test {
        @Test
        @DisplayName("존재하지 않는 상품일 경우 InvalidRequestException 발생.")
        void product_not_exists_exception_test() {
            when(productRepository.findById(anyInt()))
                .thenThrow(NullPointerException.class);

            // then
            AntigravityException e = assertThrows(
                AntigravityException.class, () -> productRepository.findById(0));

            assertThat(e.getErrorCode()).isEqualTo(ResponseCode.INVALID_REQUEST.getLabel());
        }

        @Test
        @DisplayName("해당 상품에 적용된 프로모션이 없을 경우 ProductPromotionNotFountException 발생.")
        void product_not_exists_promotion_test() {
            // given
            doReturn(promotionProducts).when(promotionProductsRepository).getProductPromotion(anyInt(), anyList());

            // when
            List<Promotion> promotionList = promotionProductsRepository
                .getProductPromotion(0,null)
                .get()
                .stream()
                .map(PromotionProducts::getPromotion)
                .collect(Collectors.toList());

            boolean noneMatchFlag1 = promotionList.containsAll(
                IntStream.of(productInfoRequest.getCouponIds())
                .boxed()
                .collect(Collectors.toList()));

            boolean noneMatchFlag2 = promotionProducts.stream()
                .filter(pp-> Arrays.stream(productInfoRequest.getCouponIds()).noneMatch(req ->pp.getPromotion().getId() == req))
                .count() > 0;

            // then
            AntigravityException e = assertThrows(
                AntigravityException.class, () ->  assertThat(noneMatchFlag2).isTrue());

            assertThat(e.getErrorCode()).isEqualTo(ResponseCode.PRODUCT_PROMOTION_NOT_FOUND.getLabel());
        }
    }


    @Nested
    @DisplayName("쿠폰 적용 가능 여부 검사")
    class promotion_invalid_test {

        // 상품의 최소 금액보다 작은 경우 에러반환 (product)
        @Test
        @DisplayName("상품 금액 검사")
        void product_not_exists_exception_test() {

            AntigravityException exception = assertThrows(AntigravityException.class, ()-> product.isValidPrice());

            assertEquals(ResponseCode.MIN_PRODUCT_PRICE, exception.getErrorCode()); // 상품 최소 금액 미달
            assertEquals(ResponseCode.MAX_PRODUCT_PRICE, exception.getErrorCode()); // 상품 최대 금액 초과
        }

        @Test
        @DisplayName("쿠폰 사용 기한 검사")
        void promotion_test() {
            promotionProducts.forEach(p-> {
                AntigravityException exception = assertThrows(AntigravityException.class,
                    () -> p.getPromotion().isAvailableDate());
                assertEquals(ResponseCode.INVALID_PROMOTION, exception.getErrorCode());
            });
        }
    }


    // 할인율 구하기 (DiscountType)
    @Test
    @DisplayName("최종 상품 금액 테스트")
    void product_promotion_price_test() {
        int discountPriceByPercent = DiscountType.PERCENT.calculate(product.getPrice(),30);
        int discountPriceByWon = DiscountType.WON.calculate(product.getPrice(),30000);

        int totalDiscountPrice = promotionProducts.stream()
            .mapToInt(pp-> pp.getPromotion().getDiscountType().calculate(product.getPrice(), pp.getPromotion().getDiscountValue()))
            .sum();

        // 최종 할인 금액
        assertEquals(discountPriceByPercent+discountPriceByWon, discountPriceByWon);

        assertNotEquals(product.getPrice(), product.getFinalPrice(totalDiscountPrice));
    }



    // 상품 조회값 설정
    private Product product() {
        return Product.builder()
            .id(1)
            .name("")
            .price(1000).build();
    }

    private Promotion createPromotion() {
        return Promotion.builder()
            .id(1)
            .promotionType(PromotionType.COUPON)
            .discountType(DiscountType.PERCENT)
            .discountValue(1000)
            .useStartedAt(new Date())
            .useEndedAt(new Date())
            .name("a")
            .build();
    }

//    @Test
//    void getProductAmount() {
//        // Given : 어떠한 데이터가 주어질 때.
//        // When : 어떠한 기능을 실행하면.
//        // Then : 어떠한 결과를 기대한다.
//        System.out.println("상품 가격 추출 테스트");
//
//        // 제어할 수 없는 값이 비즈니스 로직에 사용되기 떄문에 테스트하기 어렵다.
//        it('일요일에는 주문 금액이 10% 할인된다', () => {
//    const sut = Order.of(10_000, OrderStatus.APPROVAL);
//
//        sut.discount();
//
//        expect(sut.amount).toBe(9_000);
//})
//    }

// 데이터베이스를 사용하는 테스트들은 느린 테스트의 주범이다.

}

//@ParameterizedTest
//@Nested
//@ParameterizedTest
//@ValueSource(ints = {1, 10, 100})
//void generate_with_user_id(int userId) {
//    final String expected = String.format("FOO230101%05d00004", userId);
//
//    final String actual = purchaseOrderIdGenerator.generateId(
//        userId,
//        0,
//        3
//    );
//    assertThat(actual).isEqualTo(expected);
//}
//
//assertThat(splitArray).containsExactly("1");
//@Test
//@DisplayName("쿠폰 사용 기한이 맞지 않거나 상품에 사용할 수 없는 쿠폰일 경우 에러반환")
//void test_05() {
//    int[] list = {5};
//
//    ProductInfoRequest request = new ProductInfoRequest(1, list);
//    PromotionDTO promotionDTO = new PromotionDTO(1, "COUPON", "30000원 할인쿠폰", "WON", 30000);
//    Date today = new Date();
//    String StrDate = "20220202";
//    String EndDate = "20230505";
//    Promotion promotion;
//    SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd");
//    try {
//        Date SDate = from.parse(StrDate);
//        Date EDate = from.parse(EndDate);
//
//        promotion = new Promotion(1, "COUPON", "30000원 할인쿠폰", "WON", 30000, SDate,EDate);
//
//    } catch (ParseException e) {
//        throw new RuntimeException(e);
//    }
//
//    when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(new Product(1, "피팅노드상품", 215000)));
//    when(promotionProductRepository.findServiceAblePromotion(request.getProductId(),1,today)).thenReturn(Optional.of(promotionDTO));
//    AntiGravityApplicationException exception = Assertions.assertThrows(AntiGravityApplicationException.class
//        , () -> productService.getProductAmount(request));
//
//    Assertions.assertEquals(ErrorCode.COUPON_IS_NOT_AVAILABLE, exception.getErrorCode());
//
//}
//@ParameterizedTest
//@ValueSource(ints = {1, 3, 5, -3, 15, Integer.MAX_VALUE}) // six numbers
//void isOdd_ShouldReturnTrueForOddNumbers(int number) {
//    assertTrue(Numbers.isOdd(number));
//}