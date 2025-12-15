package car.builder;

import car.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CarConfigurationWizard (Director pattern).
 */
class CarConfigurationWizardTest {
    
    private CarConfigurationWizard wizard;
    
    @BeforeEach
    void setUp() {
        wizard = new CarConfigurationWizard();
    }
    
    @Test
    @DisplayName("Wizard should enforce step order through interfaces")
    void shouldEnforceStepOrder() {
        Car car = wizard.selectModel(CarModel.SUV)
                        .selectEngine(EngineType.V8)
                        .selectTransmission(TransmissionType.AUTOMATIC)
                        .setColor(Color.BLACK)
                        .addInteriorFeature(InteriorFeature.LEATHER)
                        .addSafetyFeature(SafetyFeature.ABS)
                        .build();
        
        assertNotNull(car);
        assertEquals(CarModel.SUV, car.getModel());
        assertEquals(EngineType.V8, car.getEngine());
        assertEquals(TransmissionType.AUTOMATIC, car.getTransmission());
    }
    
    @Test
    @DisplayName("Wizard should build car with minimal options")
    void shouldBuildWithMinimalOptions() {
        Car car = wizard.selectModel(CarModel.SEDAN)
                        .selectEngine(EngineType.V6)
                        .selectTransmission(TransmissionType.MANUAL)
                        .build();
        
        assertNotNull(car);
    }
    
    @Test
    @DisplayName("Wizard should allow optional steps to be chained")
    void shouldAllowOptionalStepsChaining() {
        Car car = wizard.selectModel(CarModel.SUV)
                        .selectEngine(EngineType.V8)
                        .selectTransmission(TransmissionType.AUTOMATIC)
                        .setColor(Color.RED)
                        .withSunroof()
                        .addInteriorFeature(InteriorFeature.GPS)
                        .addExteriorFeature(ExteriorFeature.SPORT_RIMS)
                        .addSafetyFeature(SafetyFeature.REAR_CAMERA)
                        .build();
        
        assertEquals(Color.RED, car.getColor());
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SUNROOF));
    }
    
    // Template methods tests
    
    @Test
    @DisplayName("buildBasicSedan should create valid sedan")
    void buildBasicSedanShouldCreateValidSedan() {
        Car car = wizard.buildBasicSedan(Color.SILVER);
        
        assertNotNull(car);
        assertEquals(CarModel.SEDAN, car.getModel());
        assertEquals(EngineType.V6, car.getEngine());
        assertEquals(TransmissionType.AUTOMATIC, car.getTransmission());
        assertEquals(Color.SILVER, car.getColor());
        assertTrue(car.hasSafetyFeature(SafetyFeature.ABS));
    }
    
    @Test
    @DisplayName("buildLuxurySUV should create fully loaded SUV")
    void buildLuxurySUVShouldCreateFullyLoadedSUV() {
        Car car = wizard.buildLuxurySUV(Color.BLACK);
        
        assertNotNull(car);
        assertEquals(CarModel.SUV, car.getModel());
        assertEquals(EngineType.V8, car.getEngine());
        
        assertTrue(car.hasInteriorFeature(InteriorFeature.LEATHER));
        assertTrue(car.hasInteriorFeature(InteriorFeature.GPS));
        assertTrue(car.hasInteriorFeature(InteriorFeature.SOUND_SYSTEM));
        
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SUNROOF));
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SPORT_RIMS));
        
        assertTrue(car.hasSafetyFeature(SafetyFeature.ABS));
        assertTrue(car.hasSafetyFeature(SafetyFeature.AIRBAGS));
        assertTrue(car.hasSafetyFeature(SafetyFeature.REAR_CAMERA));
    }
    
    @Test
    @DisplayName("buildSportsCar should create performance car")
    void buildSportsCarShouldCreatePerformanceCar() {
        Car car = wizard.buildSportsCar(Color.RED);
        
        assertNotNull(car);
        assertEquals(CarModel.SPORTS, car.getModel());
        assertEquals(EngineType.V8, car.getEngine());
        assertEquals(TransmissionType.MANUAL, car.getTransmission());
        assertEquals(Color.RED, car.getColor());
        
        assertTrue(car.hasInteriorFeature(InteriorFeature.LEATHER));
        assertTrue(car.hasExteriorFeature(ExteriorFeature.SPORT_RIMS));
    }
    
    @Test
    @DisplayName("Wizard with custom builder should work")
    void wizardWithCustomBuilderShouldWork() {
        CarBuilder customBuilder = new CarBuilder();
        CarConfigurationWizard customWizard = new CarConfigurationWizard(customBuilder);
        
        Car car = customWizard.selectModel(CarModel.SEDAN)
                              .selectEngine(EngineType.V6)
                              .selectTransmission(TransmissionType.AUTOMATIC)
                              .build();
        
        assertNotNull(car);
    }
}
