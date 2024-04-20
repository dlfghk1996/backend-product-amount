package antigravity.enums;



public enum DiscountType {
    WON("WON"),
    PERCENT("PERCENT");


    private final String label;

    DiscountType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
