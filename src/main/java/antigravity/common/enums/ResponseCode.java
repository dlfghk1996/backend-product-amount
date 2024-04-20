package antigravity.common.enums;


import static javax.servlet.http.HttpServletResponse.SC_OK;

public enum ResponseCode {

    INVALID_REQUEST(1001, "잘못된 요청입니다.", SC_OK),

    // PRODUCT
    PRODUCT_PROMOTION_NOT_FOUND(2001, "적용 가능한 프로모션이 없습니다.", SC_OK),
    INVALID_REQUEST_PROMOTION(2002, "적용 불가한 프로모션이 1개 이상 포함되어 있습니다. |", SC_OK),
    INVALID_PROMOTION_DATE(2003, "사용 가능한 기간이 아닌 프로모션이 1개이상 포함 되어있습니다.", SC_OK),
    MIN_PRODUCT_PRICE(2004, "최소 상품 금액보다 작습니다.", SC_OK),
    MAX_PRODUCT_PRICE(2005, "최대 상품 금액보다 큽니다.", SC_OK),

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

    public int getCode() {
        return this.code;
    }


    public String getLabel() {
        return this.label;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
