# 안티그래비티 백엔드 기술과제

## 환경
- 개발 언어 : Java 17

## 변경사항
1. 패키지
   - config -> common.config 패키지 이동: 공통 코드를 한 곳에서 관리
2. 도메인
   - discountType, promotionType 은 enum 타입으로 관리
   - 변수 명명 규칙(Snake -> Camel) 변경
   - PromotionProduct Entity 와 Product, Promotion Entity 간 연관 관계 매핑
3. DB
   - JDBC Statement -> Spring Data JPA 변경

## 추가사항
1. <b>라이브러리</b> : QueryDSL
2. 클래스:
   - Enum
      - DiscountType
      - PromotionType
   - 예외 처리
      - ControllerAdvice
      - BizException
      - ErrorResponse
      - ResponseCode(enum): 에러 코드 관리
   - 설정(config)
      - QueryDslConfig
   - 레포지토리
      - ProductRepository
      - PromotionProductsRepository
      - PromotionProductsRepositoryCustom
      - PromotionProductsRepositoryImpl
   - 유틸
      - DateUtil: 날짜 관련 기능 집합
      - MathUtil: 연산 관련 기능 집합

## 정책
1. [상품] 상품 가격은 최소 10,000원, 최대 10,000,000 원이어야 한다.
2. [상품] 최종 상품 가격이 총 할인 금액보다 작을 경우 최종 상품 가격은 0원이다.
3. [상품] COUPON 은 금액 할인, CODE 는 % 할인으로 계산한다.
4. [상품] 최종 상품 금액은 천(1,000) 단위로 절삭 한다.
   - [상품] 최종 상품 금액 단위가 천 단위 이하일 경우 절삭 하지 않는다.
5. [프로모션] 쿠폰은 사용 가능 기간 내에서만 적용이 가능하다.
6. [프로모션]  적용 요청 받은 promotion 이  해당 상품에 적용 가능한지 여부 검사
   - 사용 요청 받은 promotion 중 적용 불가능한 promotion 이 한 개라도 존재한다면 할인을 진행 하지 않는다.

## Response Error
- 1000번대: 사용자 요청 값 예외
- 2000번대: 상품 예외
- 3000번대: 프로모션 예외
- 9000번대: Server Error

| Http Status | Code | 에러 메세지                               | 설명                                             |
|:-----------:|:----:|--------------------------------------|------------------------------------------------|
|     200     | 1001 | 잘못된 요청입니다.                           | 필수 요청값이 없는 경우 발생한다. (ex. 유효하지 않은 상품ID를 조회한 경우) |
|     200     | 2001 | 적용 가능한 프로모션이 없습니다.                   | 적용 가능한 프로모션이 없을 경우 발생한다.                       |
|     200     | 2002 | 적용 불가한 프로모션이 1개 이상 포함되어 있습니다.        | 요청한 프로모션이 1개 이상 조회되지 않는 경우 발생한다.               |
|     200     | 2003 | 사용 가능한 기간이 아닌 프로모션이 1개이상 포함 되어있습니다.  | 프로모션의 사용 가능 기간 범위를 벗어난 경우 발생한다.                |
|     200     | 2004 | 최소 상품 금액보다 작습니다.                     | 상품 금액이 10,000 미만일 시 발생한다.                      |
|     200     | 2005 | 최대 상품 금액보다 큽니다.                      | 상품 금액이  10,000,000 초과일 시 발생한다.                 |
|     200     | 9001 | SERVER ERROR                         | 핸들링 하고 있는 예외를 제외한 나머지 예외 처리 시 발생한다.            |
|     200     | 9002 | SQL ERROR                            | DB 관련 오류 발생 시 발생한다. (ex. 단건 조회 시 복수값 응답 등)     |

## 테스트 시나리오
| No. | 시나리오                               | 결과                       |  예외 처리 Y/N |
|:---:|------------------------------------|--------------------------|:------------:|
|  1  | 유효한 상품 ID & 유효한 프로모션 코드 전체(2개) 조회  | 선택한 프로모션 할인 적용된 절삭 가격 반환 |      N       |
|  2  | 유효한 상품 ID & 유효한 프로모션 코드 1개 조회      | 2002 에러 발생               |      Y       |
|  3  | 유효한 상품 ID & 유효하지 않은 프로모션 코드 조회     | 2001 에러 발생               |      Y       |
|  4  | 유효하지 않은 상품ID 로 조회                  | 1001 에러 발생               |      Y       |
|  5  | 1개 이상의 프로모션 유효기간이 만료 또는 시작이 안된 경우   | 2002 에러 발생               |      Y       |
|  6  | 조회된 상품 가격이 프로모션 할인 값보다 작은 경우       | FinalPrice 0 반환          |      N       |
|  7  | 할인 적용된 금액이 절삭 기준 단위(1,000)보다 작은 경우 | 절삭되지 않은 금액 반환            |      N       |
