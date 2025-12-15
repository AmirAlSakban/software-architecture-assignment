package car.domain;

import java.util.*;

/**
 * Immutable Car domain object.
 * Instances are created only through the CarBuilder to ensure validity.
 */
public final class Car {
    
    private final CarModel model;
    private final EngineType engine;
    private final TransmissionType transmission;
    private final Color color;
    private final Set<InteriorFeature> interiorFeatures;
    private final Set<ExteriorFeature> exteriorFeatures;
    private final Set<SafetyFeature> safetyFeatures;
    
    /**
     * Creates a new Car instance.
     * Prefer using CarBuilder for validation of configurations.
     */
    public Car(CarModel model, 
        EngineType engine, 
        TransmissionType transmission,
        Color color,
        Set<InteriorFeature> interiorFeatures,
        Set<ExteriorFeature> exteriorFeatures,
        Set<SafetyFeature> safetyFeatures) {
        this.model = model;
        this.engine = engine;
        this.transmission = transmission;
        this.color = color;
        this.interiorFeatures = Collections.unmodifiableSet(interiorFeatures.isEmpty() 
                ? EnumSet.noneOf(InteriorFeature.class) : EnumSet.copyOf(interiorFeatures));
        this.exteriorFeatures = Collections.unmodifiableSet(exteriorFeatures.isEmpty() 
                ? EnumSet.noneOf(ExteriorFeature.class) : EnumSet.copyOf(exteriorFeatures));
        this.safetyFeatures = Collections.unmodifiableSet(safetyFeatures.isEmpty() 
                ? EnumSet.noneOf(SafetyFeature.class) : EnumSet.copyOf(safetyFeatures));
    }
    
    public CarModel getModel() {
        return model;
    }
    
    public EngineType getEngine() {
        return engine;
    }
    
    public TransmissionType getTransmission() {
        return transmission;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Set<InteriorFeature> getInteriorFeatures() {
        return interiorFeatures;
    }
    
    public Set<ExteriorFeature> getExteriorFeatures() {
        return exteriorFeatures;
    }
    
    public Set<SafetyFeature> getSafetyFeatures() {
        return safetyFeatures;
    }
    
    public boolean hasInteriorFeature(InteriorFeature feature) {
        return interiorFeatures.contains(feature);
    }
    
    public boolean hasExteriorFeature(ExteriorFeature feature) {
        return exteriorFeatures.contains(feature);
    }
    
    public boolean hasSafetyFeature(SafetyFeature feature) {
        return safetyFeatures.contains(feature);
    }
    
    /**
     * Gets a formatted summary of this car configuration.
     * @return multi-line summary string
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Car Configuration Summary\n");
        sb.append("========================\n");
        sb.append("Model: ").append(model.getDisplayName()).append("\n");
        sb.append("Color: ").append(color.getDisplayName()).append("\n");
        sb.append("Engine: ").append(engine).append("\n");
        sb.append("Transmission: ").append(transmission).append("\n");
        
        if (!interiorFeatures.isEmpty()) {
            sb.append("\nInterior Features:\n");
            interiorFeatures.forEach(f -> sb.append("  - ").append(f.getDisplayName()).append("\n"));
        }
        
        if (!exteriorFeatures.isEmpty()) {
            sb.append("\nExterior Features:\n");
            exteriorFeatures.forEach(f -> sb.append("  - ").append(f.getDisplayName()).append("\n"));
        }
        
        if (!safetyFeatures.isEmpty()) {
            sb.append("\nSafety Features:\n");
            safetyFeatures.forEach(f -> sb.append("  - ").append(f.getDisplayName()).append("\n"));
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("%s %s with %s and %s", 
                color.getDisplayName(), 
                model.getDisplayName(), 
                engine.getDisplayName(), 
                transmission.getDisplayName());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return model == car.model &&
               engine == car.engine &&
               transmission == car.transmission &&
               color == car.color &&
               Objects.equals(interiorFeatures, car.interiorFeatures) &&
               Objects.equals(exteriorFeatures, car.exteriorFeatures) &&
               Objects.equals(safetyFeatures, car.safetyFeatures);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(model, engine, transmission, color, 
                interiorFeatures, exteriorFeatures, safetyFeatures);
    }
}
