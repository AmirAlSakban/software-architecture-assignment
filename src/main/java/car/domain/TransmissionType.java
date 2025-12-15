package car.domain;

/**
 * Transmission type options for car configuration.
 */
public enum TransmissionType {
    MANUAL("Manual Transmission"),
    AUTOMATIC("Automatic Transmission");
    
    private final String displayName;
    
    TransmissionType(String displayName) {
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
