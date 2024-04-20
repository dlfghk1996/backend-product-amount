package antigravity.enums;


import java.util.function.BiFunction;

public enum PromotionType {
  COUPON("COUPON", (price, discountPrice) -> discountPrice),
  CODE("CODE", (price, discountPrice) -> (int) (price * (discountPrice * 0.01)));

  private final String label;

  private BiFunction<Integer, Integer, Integer> expression;

  PromotionType(String label, BiFunction<Integer, Integer, Integer> expression) {
    this.label = label;
    this.expression = expression;
  }

  public int calculate(int price, int discountPrice){
    return expression.apply(price, discountPrice);
  }

  public String getLabel() {
    return this.label;
  }
}
