package antigravity.enums;

import antigravity.common.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PromotionType implements BaseEnum {
  CODE("CODE", "코드"),
  COUPON("COUPON", "쿠폰");

  @JsonValue
  private final String code;
  private final String label;

  PromotionType(String code, String label) {
    this.code = code;
    this.label = label;
  }

  public String getCode() {
    return this.code;
  }

  @Override
  public String getName() {
    return this.name();
  }

  @Override
  public String getLabel() {
    return this.label;
  }
}
