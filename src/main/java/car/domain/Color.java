package car.domain;

/**
 * Color options for car exterior.
 */
public enum Color {
    BLACK("Black"),
    WHITE("White"),
    SILVER("Silver"),
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green");
    
    private final String displayName;
    
    Color(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
