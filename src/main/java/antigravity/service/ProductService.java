package antigravity.service;

import antigravity.common.enums.ResponseCode;
import antigravity.common.exception.AntigravityException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.enums.PromotionType;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;

import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

// TODO
/**
 * 1. 최소 상품 가격, 최대 상품가격
 * 2. 최종 상품 금액 천단위 절삭
 * 3. 쿠폰 유효기간
 * 4. 매핑 확인
 * 쿠폰이 적용되는지 검증 로직이 있어야 합니다.
 * 촘촘한 검증 코드가 요구됩니다.
 * 최소 상품가격은 ₩ 10,000 입니다.
 * 최대 상품가격은 ₩ 10,000,000 입니다.
 * 최종 상품 금액은 천단위 절삭합니다.
 * */
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        // 1. 유효성 검사 : 상품의 존재유무
        Product product = repository.findById(request.getProductId())
            .orElseThrow(()-> new AntigravityException(ResponseCode.INVALID_REQUEST));

        List<PromotionProducts> promotionProductList = product.getPromotionProductsList();

        // 2. 유효성 검사 (요청값에 해당하는 데이터 조회)
        if(ObjectUtils.isEmpty(promotionProductList)){
            throw new AntigravityException(ResponseCode.PRODUCT_PROMOTION_NOT_FOUND);
        }

        // 3. 프로모션 적용 가능 여부 검사
        for(PromotionProducts promotionProducts: promotionProductList){
            Promotion promotion = promotionProducts.getPromotion();
            if(!IntStream.of(request.getCouponIds())
                .allMatch(x -> x == promotion.getId())){
                throw new AntigravityException(ResponseCode.PRODUCT_NOT_MATHCING_PROMOTION);
            }
            if(promotion.getUseStartedAt().after(new Date())
                && promotion.getUseEndedAt().before(new Date())){
                throw new AntigravityException(ResponseCode.INVALID_PROMOTION);
            }
        }

        // 4. 할인율 구하기
        int originPrice = product.getPrice();
        int discountPrice = promotionProductList
            .stream()
            .map(promotionProducts->{
                Promotion promotion = promotionProducts.getPromotion();
                String promotionType = promotion.getPromotionType();
                int discountValue = promotion.getDiscountValue();
                return calculateDiscountPrice(originPrice, discountValue, promotionType);
            }).reduce(0, Integer::sum);

        // 5. 최종가격 구하기
        int finalPrice = originPrice - discountPrice;
        finalPrice = finalPrice< 0? 0: getFinalPrice(finalPrice);

        return ProductAmountResponse.builder()
            .name(product.getName())
            .originPrice(originPrice)
            .discountPrice(discountPrice)
            .finalPrice(finalPrice)
            .build();
    }

    public int calculateDiscountPrice(int price, int discountValue, String promotionType){
        int result = 0;
        if (PromotionType.CODE.equals(promotionType)) {
            result= discountValue;
        }else if (PromotionType.COUPON.equals(promotionType)) {
            double M3=discountValue*0.01; // M3는 %를 소수점으로 변환한 값이다 즉 20%를 0.2로 변환한다
            double discountPrice=price*M3; // 할인되는 가격
            result = (int)discountPrice;
        }
        return result;
    }


    /**
     * 천단위 절삭 계산
     * @param finalPrice 상품 금액
     * @return 확정 상품 가격
     */
    private int getFinalPrice(int finalPrice) {
        int downUnit = finalPrice % 1000;
        return finalPrice - downUnit;
    }
}






// 천단위 절삭
//(1000으로 나눴다가 반올림하고 * 1000 해보세요)
//double value = 0;
//value = Math.round(x / 1000) * 1000;

//        int y = x%1000;
//        x = (y>=500)? x+ (1000-y) :x-y;
//x가 정수이면 x/1000도 정수가 됩니다.
//    즉. x=15600일 때 x/1000하면 15.6이 되는 게 아니고, 16이 됩니다. Round 필요 없어요.
//
//그러나 제 예전 직장이 임베디드 환경의 C여서,
//상사가 가능한 나눗셈/float 타입 쓰지 말라고 했던가 뭔가 해서 가능한 피할 수 있으면 피하는 버릇도 생겼고, 꼭 C가 아니더라도 소수와 정수 섞어서 연산(특히 나눗셈)하다 보면 소위 gotcha라고 하는 보기에 멀쩡했는데
//나중에 보면 예상치 않은 결과가 나오는 경우도 있고 해서 가능한 피합니다.

