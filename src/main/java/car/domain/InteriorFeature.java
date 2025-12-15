package car.domain;

/**
 * Interior feature options for car configuration.
 */
public enum InteriorFeature {
    LEATHER("Leather Interior"),
    GPS("GPS Navigation System"),
    SOUND_SYSTEM("Premium Sound System");
    
    private final String displayName;
    
    InteriorFeature(String displayName) {
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
