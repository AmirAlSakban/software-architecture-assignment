package car.domain;

import java.util.*;

/**
 * Car model enum defining allowed options for each model.
 * Not all models support all options.
 */
public enum CarModel {
    SEDAN("Sedan", 
          EnumSet.of(EngineType.V6),
          EnumSet.allOf(TransmissionType.class),
          EnumSet.allOf(InteriorFeature.class),
          EnumSet.of(ExteriorFeature.STANDARD_RIMS),
          EnumSet.of(SafetyFeature.ABS, SafetyFeature.AIRBAGS)),
    
    SUV("SUV",
        EnumSet.allOf(EngineType.class),
        EnumSet.allOf(TransmissionType.class),
        EnumSet.allOf(InteriorFeature.class),
        EnumSet.allOf(ExteriorFeature.class),
        EnumSet.allOf(SafetyFeature.class)),
    
    SPORTS("Sports Car",
           EnumSet.of(EngineType.V8),
           EnumSet.of(TransmissionType.MANUAL),
           EnumSet.of(InteriorFeature.LEATHER, InteriorFeature.SOUND_SYSTEM),
           EnumSet.of(ExteriorFeature.SPORT_RIMS, ExteriorFeature.SUNROOF),
           EnumSet.allOf(SafetyFeature.class)),
    
    COMPACT("Compact",
            EnumSet.of(EngineType.V6),
            EnumSet.of(TransmissionType.AUTOMATIC),
            EnumSet.of(InteriorFeature.GPS),
            EnumSet.of(ExteriorFeature.STANDARD_RIMS),
            EnumSet.of(SafetyFeature.ABS, SafetyFeature.REAR_CAMERA));
    
    private final String displayName;
    private final Set<EngineType> allowedEngines;
    private final Set<TransmissionType> allowedTransmissions;
    private final Set<InteriorFeature> allowedInteriorFeatures;
    private final Set<ExteriorFeature> allowedExteriorFeatures;
    private final Set<SafetyFeature> allowedSafetyFeatures;
    
    CarModel(String displayName,
             Set<EngineType> allowedEngines,
             Set<TransmissionType> allowedTransmissions,
             Set<InteriorFeature> allowedInteriorFeatures,
             Set<ExteriorFeature> allowedExteriorFeatures,
             Set<SafetyFeature> allowedSafetyFeatures) {
        this.displayName = displayName;
        this.allowedEngines = Collections.unmodifiableSet(allowedEngines);
        this.allowedTransmissions = Collections.unmodifiableSet(allowedTransmissions);
        this.allowedInteriorFeatures = Collections.unmodifiableSet(allowedInteriorFeatures);
        this.allowedExteriorFeatures = Collections.unmodifiableSet(allowedExteriorFeatures);
        this.allowedSafetyFeatures = Collections.unmodifiableSet(allowedSafetyFeatures);
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean supportsEngine(EngineType engine) {
        return allowedEngines.contains(engine);
    }
    
    public boolean supportsTransmission(TransmissionType transmission) {
        return allowedTransmissions.contains(transmission);
    }
    
    public boolean supportsInteriorFeature(InteriorFeature feature) {
        return allowedInteriorFeatures.contains(feature);
    }
    
    public boolean supportsExteriorFeature(ExteriorFeature feature) {
        return allowedExteriorFeatures.contains(feature);
    }
    
    public boolean supportsSafetyFeature(SafetyFeature feature) {
        return allowedSafetyFeatures.contains(feature);
    }
    
    public Set<EngineType> getAllowedEngines() {
        return allowedEngines;
    }
    
    public Set<TransmissionType> getAllowedTransmissions() {
        return allowedTransmissions;
    }
    
    public Set<InteriorFeature> getAllowedInteriorFeatures() {
        return allowedInteriorFeatures;
    }
    
    public Set<ExteriorFeature> getAllowedExteriorFeatures() {
        return allowedExteriorFeatures;
    }
    
    public Set<SafetyFeature> getAllowedSafetyFeatures() {
        return allowedSafetyFeatures;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
