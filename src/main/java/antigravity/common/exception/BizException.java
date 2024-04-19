package antigravity.common.exception;

import antigravity.common.enums.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {

    @Getter
    private int errorCode;
    private ResponseCode responseCode;

    public BizException() {
        super();
    }

    public BizException(ResponseCode responseCode) {
        super(responseCode.getLabel());
        this.errorCode = responseCode.getCode();
        this.responseCode = responseCode;
    }

}
