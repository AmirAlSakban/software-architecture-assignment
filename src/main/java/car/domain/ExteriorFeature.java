package car.domain;

/**
 * Exterior feature options for car configuration.
 */
public enum ExteriorFeature {
    SUNROOF("Panoramic Sunroof"),
    SPORT_RIMS("Sport Alloy Rims"),
    STANDARD_RIMS("Standard Rims");
    
    private final String displayName;
    
    ExteriorFeature(String displayName) {
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
