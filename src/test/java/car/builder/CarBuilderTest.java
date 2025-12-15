package car.builder;

import car.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CarBuilder and car configuration validation.
 */
class CarBuilderTest {
    
    private CarBuilder builder;
    
    @BeforeEach
    void setUp() {
        builder = new CarBuilder();
    }
    
    // Valid configuration tests
    
    @Test
    @DisplayName("Should build valid Sedan with V6")
    void shouldBuildValidSedan() {
        Car car = builder
            .withModel(CarModel.SEDAN)
            .withEngine(EngineType.V6)
            .withTransmission(TransmissionType.AUTOMATIC)
            .setColor(Color.BLUE)
            .build();
        
        assertNotNull(car);
        assertEquals(CarModel.SEDAN, car.getModel());
        assertEquals(EngineType.V6, car.getEngine());
        assertEquals(TransmissionType.AUTOMATIC, car.getTransmission());
        assertEquals(Color.BLUE, car.getColor());
    }
    
    @Test
    @DisplayName("Should build valid SUV with all options")
    void shouldBuildValidSUV() {
        Car car = builder
            .withModel(CarModel.SUV)
            .withEngine(EngineType.V8)
            .withTransmission(TransmissionType.AUTOMATIC)
            .setColor(Color.BLACK)
            .addInteriorFeature(InteriorFeature.LEATHER)
            .addInteriorFeature(InteriorFeature.GPS)
            .addInteriorFeature(InteriorFeature.SOUND_SYSTEM)
            .addExteriorFeature(ExteriorFeature.SUNROOF)
            .addExteriorFeature(ExteriorFeature.SPORT_RIMS)
            .addSafetyFeature(SafetyFeature.ABS)
            .addSafetyFeature(SafetyFeature.AIRBAGS)
            .addSafetyFeature(SafetyFeature.REAR_CAMERA)
            .build();
        
        assertNotNull(car);
        assertEquals(CarModel.SUV, car.getModel());
        assertTrue(car.hasInteriorFeature(InteriorFeature.LEATHER));
        assertTrue(car.hasInteriorFeature(InteriorFeature.GPS));
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SUNROOF));
        assertTrue(car.hasSafetyFeature(SafetyFeature.ABS));
    }
    
    @Test
    @DisplayName("Should build valid Sports car")
    void shouldBuildValidSportsCar() {
        Car car = builder
            .withModel(CarModel.SPORTS)
            .withEngine(EngineType.V8)
            .withTransmission(TransmissionType.MANUAL)
            .setColor(Color.RED)
            .addInteriorFeature(InteriorFeature.LEATHER)
            .addExteriorFeature(ExteriorFeature.SPORT_RIMS)
            .build();
        
        assertNotNull(car);
        assertEquals(CarModel.SPORTS, car.getModel());
        assertEquals(EngineType.V8, car.getEngine());
        assertEquals(TransmissionType.MANUAL, car.getTransmission());
    }
    
    @Test
    @DisplayName("Should build valid Compact car")
    void shouldBuildValidCompact() {
        Car car = builder
            .withModel(CarModel.COMPACT)
            .withEngine(EngineType.V6)
            .withTransmission(TransmissionType.AUTOMATIC)
            .build();
        
        assertNotNull(car);
        assertEquals(CarModel.COMPACT, car.getModel());
    }
    
    @Test
    @DisplayName("Should use default color when not specified")
    void shouldUseDefaultColor() {
        Car car = builder
            .withModel(CarModel.SEDAN)
            .withEngine(EngineType.V6)
            .withTransmission(TransmissionType.AUTOMATIC)
            .build();
        
        assertEquals(Color.BLACK, car.getColor());
    }
    
    // Missing required fields tests
    
    @Test
    @DisplayName("Should throw exception when model is missing")
    void shouldThrowWhenModelMissing() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withEngine(EngineType.V6)
                .withTransmission(TransmissionType.AUTOMATIC)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("model"));
        assertTrue(ex.getMessage().contains("Missing required fields"));
    }
    
    @Test
    @DisplayName("Should throw exception when engine is missing")
    void shouldThrowWhenEngineMissing() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.SEDAN)
                .withTransmission(TransmissionType.AUTOMATIC)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("engine"));
    }
    
    @Test
    @DisplayName("Should throw exception when transmission is missing")
    void shouldThrowWhenTransmissionMissing() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.SEDAN)
                .withEngine(EngineType.V6)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("transmission"));
    }
    
    @Test
    @DisplayName("Should list all missing fields")
    void shouldListAllMissingFields() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder.build()
        );
        
        assertTrue(ex.getMessage().contains("model"));
        assertTrue(ex.getMessage().contains("engine"));
        assertTrue(ex.getMessage().contains("transmission"));
    }
    
    // Incompatible options tests
    
    @Test
    @DisplayName("Should throw exception for unsupported engine")
    void shouldThrowForUnsupportedEngine() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.SEDAN) // Only supports V6
                .withEngine(EngineType.V8)
                .withTransmission(TransmissionType.AUTOMATIC)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("Engine"));
        assertTrue(ex.getMessage().contains("not supported"));
        assertTrue(ex.getMessage().contains("Sedan"));
    }
    
    @Test
    @DisplayName("Should throw exception for unsupported transmission")
    void shouldThrowForUnsupportedTransmission() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.SPORTS) // Only supports MANUAL
                .withEngine(EngineType.V8)
                .withTransmission(TransmissionType.AUTOMATIC)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("Transmission"));
        assertTrue(ex.getMessage().contains("not supported"));
    }
    
    @Test
    @DisplayName("Should throw exception for unsupported interior feature")
    void shouldThrowForUnsupportedInteriorFeature() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.COMPACT) // Only supports GPS
                .withEngine(EngineType.V6)
                .withTransmission(TransmissionType.AUTOMATIC)
                .addInteriorFeature(InteriorFeature.LEATHER)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("Interior feature"));
        assertTrue(ex.getMessage().contains("Leather"));
        assertTrue(ex.getMessage().contains("not supported"));
    }
    
    @Test
    @DisplayName("Should throw exception for unsupported exterior feature")
    void shouldThrowForUnsupportedExteriorFeature() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.SEDAN) // Only supports STANDARD_RIMS
                .withEngine(EngineType.V6)
                .withTransmission(TransmissionType.AUTOMATIC)
                .addExteriorFeature(ExteriorFeature.SUNROOF)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("Exterior feature"));
        assertTrue(ex.getMessage().contains("Sunroof"));
    }
    
    @Test
    @DisplayName("Should throw exception for unsupported safety feature")
    void shouldThrowForUnsupportedSafetyFeature() {
        InvalidCarConfigurationException ex = assertThrows(
            InvalidCarConfigurationException.class,
            () -> builder
                .withModel(CarModel.SEDAN) // Does not support REAR_CAMERA
                .withEngine(EngineType.V6)
                .withTransmission(TransmissionType.AUTOMATIC)
                .addSafetyFeature(SafetyFeature.REAR_CAMERA)
                .build()
        );
        
        assertTrue(ex.getMessage().contains("Safety feature"));
        assertTrue(ex.getMessage().contains("Rear View Camera"));
    }
    
    // Null value tests
    
    @Test
    @DisplayName("Should throw exception for null model")
    void shouldThrowForNullModel() {
        assertThrows(InvalidCarConfigurationException.class,
            () -> builder.withModel(null));
    }
    
    @Test
    @DisplayName("Should throw exception for null engine")
    void shouldThrowForNullEngine() {
        assertThrows(InvalidCarConfigurationException.class,
            () -> builder.withEngine(null));
    }
    
    @Test
    @DisplayName("Should throw exception for null transmission")
    void shouldThrowForNullTransmission() {
        assertThrows(InvalidCarConfigurationException.class,
            () -> builder.withTransmission(null));
    }
    
    @Test
    @DisplayName("Should throw exception for null color")
    void shouldThrowForNullColor() {
        assertThrows(InvalidCarConfigurationException.class,
            () -> builder.setColor(null));
    }
    
    @Test
    @DisplayName("Should throw exception for null interior feature")
    void shouldThrowForNullInteriorFeature() {
        assertThrows(InvalidCarConfigurationException.class,
            () -> builder.addInteriorFeature(null));
    }
    
    @Test
    @DisplayName("Should throw exception for null safety feature")
    void shouldThrowForNullSafetyFeature() {
        assertThrows(InvalidCarConfigurationException.class,
            () -> builder.addSafetyFeature(null));
    }
    
    // Fluent API and convenience methods
    
    @Test
    @DisplayName("Should support fluent API chaining")
    void shouldSupportFluentApiChaining() {
        Car car = builder
            .withModel(CarModel.SUV)
            .withEngine(EngineType.V8)
            .withTransmission(TransmissionType.AUTOMATIC)
            .setColor(Color.WHITE)
            .withSunroof()
            .withRims(true)
            .addInteriorFeatures(InteriorFeature.LEATHER, InteriorFeature.GPS)
            .addSafetyFeatures(SafetyFeature.ABS, SafetyFeature.AIRBAGS)
            .build();
        
        assertNotNull(car);
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SUNROOF));
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SPORT_RIMS));
    }
    
    @Test
    @DisplayName("Should reset builder state")
    void shouldResetBuilderState() {
        builder.withModel(CarModel.SUV)
               .withEngine(EngineType.V8)
               .withTransmission(TransmissionType.AUTOMATIC)
               .addInteriorFeature(InteriorFeature.LEATHER);
        
        builder.reset();
        
        assertThrows(InvalidCarConfigurationException.class, () -> builder.build());
    }
    
    @Test
    @DisplayName("withRims(false) should add standard rims")
    void withRimsFalseShouldAddStandardRims() {
        Car car = builder
            .withModel(CarModel.SEDAN)
            .withEngine(EngineType.V6)
            .withTransmission(TransmissionType.AUTOMATIC)
            .withRims(false)
            .build();
        
        assertTrue(car.hasExteriorFeature(ExteriorFeature.STANDARD_RIMS));
    }
}
