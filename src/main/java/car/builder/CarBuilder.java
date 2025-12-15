package car.builder;

import car.domain.*;

import java.util.*;

/**
 * Builder for creating valid Car instances with fluent API.
 * Validates all configurations against the selected model.
 */
public class CarBuilder {
    
    private CarModel model;
    private EngineType engine;
    private TransmissionType transmission;
    private Color color = Color.BLACK; // Default color
    private final Set<InteriorFeature> interiorFeatures = EnumSet.noneOf(InteriorFeature.class);
    private final Set<ExteriorFeature> exteriorFeatures = EnumSet.noneOf(ExteriorFeature.class);
    private final Set<SafetyFeature> safetyFeatures = EnumSet.noneOf(SafetyFeature.class);
    
    /**
     * Sets the car model (required).
     * @param model the car model
     * @return this builder
     */
    public CarBuilder withModel(CarModel model) {
        if (model == null) {
            throw new InvalidCarConfigurationException("Car model cannot be null");
        }
        this.model = model;
        return this;
    }
    
    /**
     * Sets the engine type (required).
     * @param engine the engine type
     * @return this builder
     */
    public CarBuilder withEngine(EngineType engine) {
        if (engine == null) {
            throw new InvalidCarConfigurationException("Engine type cannot be null");
        }
        this.engine = engine;
        return this;
    }
    
    /**
     * Sets the transmission type (required).
     * @param transmission the transmission type
     * @return this builder
     */
    public CarBuilder withTransmission(TransmissionType transmission) {
        if (transmission == null) {
            throw new InvalidCarConfigurationException("Transmission type cannot be null");
        }
        this.transmission = transmission;
        return this;
    }
    
    /**
     * Sets the car color (optional, defaults to BLACK).
     * @param color the color
     * @return this builder
     */
    public CarBuilder setColor(Color color) {
        if (color == null) {
            throw new InvalidCarConfigurationException("Color cannot be null");
        }
        this.color = color;
        return this;
    }
    
    /**
     * Adds an interior feature (optional).
     * @param feature the interior feature
     * @return this builder
     */
    public CarBuilder addInteriorFeature(InteriorFeature feature) {
        if (feature == null) {
            throw new InvalidCarConfigurationException("Interior feature cannot be null");
        }
        this.interiorFeatures.add(feature);
        return this;
    }
    
    /**
     * Adds multiple interior features (optional).
     * @param features the interior features
     * @return this builder
     */
    public CarBuilder addInteriorFeatures(InteriorFeature... features) {
        for (InteriorFeature feature : features) {
            addInteriorFeature(feature);
        }
        return this;
    }
    
    /**
     * Adds an exterior feature (optional).
     * @param feature the exterior feature
     * @return this builder
     */
    public CarBuilder addExteriorFeature(ExteriorFeature feature) {
        if (feature == null) {
            throw new InvalidCarConfigurationException("Exterior feature cannot be null");
        }
        this.exteriorFeatures.add(feature);
        return this;
    }
    
    /**
     * Adds a sunroof (convenience method).
     * @return this builder
     */
    public CarBuilder withSunroof() {
        return addExteriorFeature(ExteriorFeature.SUNROOF);
    }
    
    /**
     * Sets the rim type.
     * @param sportRims true for sport rims, false for standard
     * @return this builder
     */
    public CarBuilder withRims(boolean sportRims) {
        return addExteriorFeature(sportRims ? ExteriorFeature.SPORT_RIMS : ExteriorFeature.STANDARD_RIMS);
    }
    
    /**
     * Adds a safety feature (optional).
     * @param feature the safety feature
     * @return this builder
     */
    public CarBuilder addSafetyFeature(SafetyFeature feature) {
        if (feature == null) {
            throw new InvalidCarConfigurationException("Safety feature cannot be null");
        }
        this.safetyFeatures.add(feature);
        return this;
    }
    
    /**
     * Adds multiple safety features (optional).
     * @param features the safety features
     * @return this builder
     */
    public CarBuilder addSafetyFeatures(SafetyFeature... features) {
        for (SafetyFeature feature : features) {
            addSafetyFeature(feature);
        }
        return this;
    }
    
    /**
     * Builds and validates the car configuration.
     * @return a valid, immutable Car instance
     * @throws InvalidCarConfigurationException if validation fails
     */
    public Car build() {
        validateRequiredFields();
        validateCompatibility();
        
        return new Car(model, engine, transmission, color, 
                interiorFeatures, exteriorFeatures, safetyFeatures);
    }
    
    private void validateRequiredFields() {
        List<String> missingFields = new ArrayList<>();
        
        if (model == null) {
            missingFields.add("model");
        }
        if (engine == null) {
            missingFields.add("engine");
        }
        if (transmission == null) {
            missingFields.add("transmission");
        }
        
        if (!missingFields.isEmpty()) {
            throw new InvalidCarConfigurationException(
                    "Missing required fields: " + String.join(", ", missingFields) + 
                    ". Please specify: withModel(), withEngine(), and withTransmission().");
        }
    }
    
    private void validateCompatibility() {
        List<String> errors = new ArrayList<>();
        
        // Validate engine compatibility
        if (!model.supportsEngine(engine)) {
            errors.add(String.format("Engine '%s' is not supported by %s. Allowed engines: %s",
                    engine.getDisplayName(), 
                    model.getDisplayName(),
                    model.getAllowedEngines()));
        }
        
        // Validate transmission compatibility
        if (!model.supportsTransmission(transmission)) {
            errors.add(String.format("Transmission '%s' is not supported by %s. Allowed transmissions: %s",
                    transmission.getDisplayName(),
                    model.getDisplayName(),
                    model.getAllowedTransmissions()));
        }
        
        // Validate interior features compatibility
        for (InteriorFeature feature : interiorFeatures) {
            if (!model.supportsInteriorFeature(feature)) {
                errors.add(String.format("Interior feature '%s' is not supported by %s. Allowed features: %s",
                        feature.getDisplayName(),
                        model.getDisplayName(),
                        model.getAllowedInteriorFeatures()));
            }
        }
        
        // Validate exterior features compatibility
        for (ExteriorFeature feature : exteriorFeatures) {
            if (!model.supportsExteriorFeature(feature)) {
                errors.add(String.format("Exterior feature '%s' is not supported by %s. Allowed features: %s",
                        feature.getDisplayName(),
                        model.getDisplayName(),
                        model.getAllowedExteriorFeatures()));
            }
        }
        
        // Validate safety features compatibility
        for (SafetyFeature feature : safetyFeatures) {
            if (!model.supportsSafetyFeature(feature)) {
                errors.add(String.format("Safety feature '%s' is not supported by %s. Allowed features: %s",
                        feature.getDisplayName(),
                        model.getDisplayName(),
                        model.getAllowedSafetyFeatures()));
            }
        }
        
        if (!errors.isEmpty()) {
            throw new InvalidCarConfigurationException(
                    "Invalid car configuration:\n- " + String.join("\n- ", errors));
        }
    }
    
    /**
     * Resets the builder to initial state.
     * @return this builder
     */
    public CarBuilder reset() {
        this.model = null;
        this.engine = null;
        this.transmission = null;
        this.color = Color.BLACK;
        this.interiorFeatures.clear();
        this.exteriorFeatures.clear();
        this.safetyFeatures.clear();
        return this;
    }
}
