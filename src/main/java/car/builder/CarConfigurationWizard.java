package car.builder;

import car.domain.*;

/**
 * Director class that guides step-by-step car configuration.
 * Ensures configuration follows the correct order and provides
 * pre-configured templates.
 */
public class CarConfigurationWizard {
    
    private final CarBuilder builder;
    
    public CarConfigurationWizard() {
        this.builder = new CarBuilder();
    }
    
    public CarConfigurationWizard(CarBuilder builder) {
        this.builder = builder;
    }
    
    /**
     * Step 1: Select the car model (required first step).
     * @param model the car model
     * @return step 2 interface
     */
    public EngineStep selectModel(CarModel model) {
        builder.withModel(model);
        return new EngineStepImpl();
    }
    
    // Step interfaces for enforcing build order
    
    public interface EngineStep {
        TransmissionStep selectEngine(EngineType engine);
    }
    
    public interface TransmissionStep {
        OptionsStep selectTransmission(TransmissionType transmission);
    }
    
    public interface OptionsStep {
        OptionsStep setColor(Color color);
        OptionsStep addInteriorFeature(InteriorFeature feature);
        OptionsStep addExteriorFeature(ExteriorFeature feature);
        OptionsStep addSafetyFeature(SafetyFeature feature);
        OptionsStep withSunroof();
        Car build();
    }
    
    // Step implementations
    
    private class EngineStepImpl implements EngineStep {
        @Override
        public TransmissionStep selectEngine(EngineType engine) {
            builder.withEngine(engine);
            return new TransmissionStepImpl();
        }
    }
    
    private class TransmissionStepImpl implements TransmissionStep {
        @Override
        public OptionsStep selectTransmission(TransmissionType transmission) {
            builder.withTransmission(transmission);
            return new OptionsStepImpl();
        }
    }
    
    private class OptionsStepImpl implements OptionsStep {
        @Override
        public OptionsStep setColor(Color color) {
            builder.setColor(color);
            return this;
        }
        
        @Override
        public OptionsStep addInteriorFeature(InteriorFeature feature) {
            builder.addInteriorFeature(feature);
            return this;
        }
        
        @Override
        public OptionsStep addExteriorFeature(ExteriorFeature feature) {
            builder.addExteriorFeature(feature);
            return this;
        }
        
        @Override
        public OptionsStep addSafetyFeature(SafetyFeature feature) {
            builder.addSafetyFeature(feature);
            return this;
        }
        
        @Override
        public OptionsStep withSunroof() {
            builder.withSunroof();
            return this;
        }
        
        @Override
        public Car build() {
            return builder.build();
        }
    }
    
    // Pre-configured templates
    
    /**
     * Creates a basic sedan with minimal options.
     * @param color the car color
     * @return configured Car
     */
    public Car buildBasicSedan(Color color) {
        return builder.reset()
                .withModel(CarModel.SEDAN)
                .withEngine(EngineType.V6)
                .withTransmission(TransmissionType.AUTOMATIC)
                .setColor(color)
                .addSafetyFeature(SafetyFeature.ABS)
                .build();
    }
    
    /**
     * Creates a fully-loaded SUV with all options.
     * @param color the car color
     * @return configured Car
     */
    public Car buildLuxurySUV(Color color) {
        return builder.reset()
                .withModel(CarModel.SUV)
                .withEngine(EngineType.V8)
                .withTransmission(TransmissionType.AUTOMATIC)
                .setColor(color)
                .addInteriorFeatures(InteriorFeature.LEATHER, InteriorFeature.GPS, InteriorFeature.SOUND_SYSTEM)
                .addExteriorFeature(ExteriorFeature.SUNROOF)
                .addExteriorFeature(ExteriorFeature.SPORT_RIMS)
                .addSafetyFeatures(SafetyFeature.ABS, SafetyFeature.AIRBAGS, SafetyFeature.REAR_CAMERA)
                .build();
    }
    
    /**
     * Creates a sports car with performance options.
     * @param color the car color
     * @return configured Car
     */
    public Car buildSportsCar(Color color) {
        return builder.reset()
                .withModel(CarModel.SPORTS)
                .withEngine(EngineType.V8)
                .withTransmission(TransmissionType.MANUAL)
                .setColor(color)
                .addInteriorFeatures(InteriorFeature.LEATHER, InteriorFeature.SOUND_SYSTEM)
                .addExteriorFeature(ExteriorFeature.SPORT_RIMS)
                .addSafetyFeatures(SafetyFeature.ABS, SafetyFeature.AIRBAGS)
                .build();
    }
}
