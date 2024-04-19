package antigravity.model.request;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoRequest {
    @NotNull
    private int productId;

    @NotNull
    private int[] couponIds;
}
