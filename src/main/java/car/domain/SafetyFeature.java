package car.domain;

/**
 * Safety feature options for car configuration.
 */
public enum SafetyFeature {
    ABS("Anti-lock Braking System (ABS)"),
    AIRBAGS("Full Airbag System"),
    REAR_CAMERA("Rear View Camera");
    
    private final String displayName;
    
    SafetyFeature(String displayName) {
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
