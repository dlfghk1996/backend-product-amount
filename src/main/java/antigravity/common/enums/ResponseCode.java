package antigravity.common.enums;


import static javax.servlet.http.HttpServletResponse.SC_OK;

public enum ResponseCode implements BaseEnum {

    INVALID_REQUEST(1001, "잘못된 요청입니다.", SC_OK),

    // PRODUCT
    PRODUCT_PROMOTION_NOT_FOUND(2001, "해당 상품은 적용되는 프로모션이 없습니다.", SC_OK),
    INVALID_PROMOTION(2002, "유효하지 않은 프로모션 입니다.", SC_OK),
    MIN_PRODUCT_PRICE(2003, "상품 최소 가능 금액에 맞지않습니다.", SC_OK),
    MAX_PRODUCT_PRICE(2004, "상품 최대 가능 금액에 맞지않습니다.", SC_OK),

    ERROR(9001, "SERVER ERROR", SC_OK),
    ERROR_SQL(9002, "SQL ERROR", SC_OK);

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
