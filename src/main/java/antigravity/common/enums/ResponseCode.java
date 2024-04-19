package antigravity.common.enums;



import static javax.servlet.http.HttpServletResponse.SC_OK;

public enum ResponseCode implements BaseEnum {

    /**
     * [ 1000 ~ 1999 ] : SUCCESS ***
     */
    OK(0, "success", SC_OK),
    SUCCESS(1000, "success", SC_OK),
    /**
     * [ 2000 ~ 2999 ] : INVALID_REQUEST ***
     */
    INVALID_REQUEST(3000, "잘 못 된 요청입니다.", SC_OK),

    // PRODUCT
    PRODUCT_PROMOTION_NOT_FOUND(4000, "해당 상품은 적용되는 프로모션이 없습니다.", SC_OK),
    PRODUCT_NOT_MATHCING_PROMOTION(4001, "잘못된 프로모션 적용입니다.", SC_OK),
    INVALID_PROMOTION(4002, "유효하지 않은 프로모션 입니다.", SC_OK),
    MIN_PRODUCT_PRICE(4003, "상품 최소 가능 금액에 맞지않습니다.", SC_OK),
    MAX_PRODUCT_PRICE(4004, "상품 최대 가능 금액에 맞지않습니다.", SC_OK),

    /**
     * [ 9000 - 9009 ] : ERROR ***
     */
    ERROR(9000, "ERROR", SC_OK),

    ERROR_SQL(9001, "SQL ERROR", SC_OK);

    private final int code;
    private final String label;
    private final int httpStatusCode;

    ResponseCode(int code, String label, int httpStatusCode) {
        this.code = code;
        this.label = label;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
