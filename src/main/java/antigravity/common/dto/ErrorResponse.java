package antigravity.common.dto;

import antigravity.common.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Exception 발생 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    int status;
    String message;

    public ErrorResponse(ResponseCode responseCode) {
        this.status = responseCode.getCode();
        this.message = responseCode.getLabel();
    }
}
