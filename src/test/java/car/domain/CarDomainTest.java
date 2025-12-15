package car.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Car domain model and CarModel compatibility.
 */
class CarDomainTest {
    
    // CarModel tests
    
    @Test
    @DisplayName("Sedan should only support V6 engine")
    void sedanShouldOnlySupportV6() {
        assertTrue(CarModel.SEDAN.supportsEngine(EngineType.V6));
        assertFalse(CarModel.SEDAN.supportsEngine(EngineType.V8));
    }
    
    @Test
    @DisplayName("Sports car should only support V8 engine")
    void sportsShouldOnlySupportV8() {
        assertFalse(CarModel.SPORTS.supportsEngine(EngineType.V6));
        assertTrue(CarModel.SPORTS.supportsEngine(EngineType.V8));
    }
    
    @Test
    @DisplayName("SUV should support all engines")
    void suvShouldSupportAllEngines() {
        assertTrue(CarModel.SUV.supportsEngine(EngineType.V6));
        assertTrue(CarModel.SUV.supportsEngine(EngineType.V8));
    }
    
    @Test
    @DisplayName("Sports car should only support manual transmission")
    void sportsShouldOnlySupportManual() {
        assertTrue(CarModel.SPORTS.supportsTransmission(TransmissionType.MANUAL));
        assertFalse(CarModel.SPORTS.supportsTransmission(TransmissionType.AUTOMATIC));
    }
    
    @Test
    @DisplayName("Compact should only support automatic transmission")
    void compactShouldOnlySupportAutomatic() {
        assertFalse(CarModel.COMPACT.supportsTransmission(TransmissionType.MANUAL));
        assertTrue(CarModel.COMPACT.supportsTransmission(TransmissionType.AUTOMATIC));
    }
    
    @Test
    @DisplayName("CarModel should return allowed options sets")
    void carModelShouldReturnAllowedOptionsSets() {
        Set<EngineType> engines = CarModel.SUV.getAllowedEngines();
        Set<TransmissionType> transmissions = CarModel.SUV.getAllowedTransmissions();
        Set<InteriorFeature> interior = CarModel.SUV.getAllowedInteriorFeatures();
        
        assertNotNull(engines);
        assertNotNull(transmissions);
        assertNotNull(interior);
        assertFalse(engines.isEmpty());
    }
    
    @Test
    @DisplayName("CarModel should have display name")
    void carModelShouldHaveDisplayName() {
        assertEquals("Sedan", CarModel.SEDAN.getDisplayName());
        assertEquals("SUV", CarModel.SUV.getDisplayName());
        assertEquals("Sports Car", CarModel.SPORTS.getDisplayName());
        assertEquals("Compact", CarModel.COMPACT.getDisplayName());
    }
    
    // Car tests
    
    @Test
    @DisplayName("Car should be immutable")
    void carShouldBeImmutable() {
        Set<InteriorFeature> interiorFeatures = EnumSet.of(InteriorFeature.LEATHER);
        Set<ExteriorFeature> exteriorFeatures = EnumSet.of(ExteriorFeature.SUNROOF);
        Set<SafetyFeature> safetyFeatures = EnumSet.of(SafetyFeature.ABS);
        
        Car car = new Car(CarModel.SUV, EngineType.V8, TransmissionType.AUTOMATIC,
                Color.BLACK, interiorFeatures, exteriorFeatures, safetyFeatures);
        
        // Returned sets should be unmodifiable
        assertThrows(UnsupportedOperationException.class,
            () -> car.getInteriorFeatures().add(InteriorFeature.GPS));
        assertThrows(UnsupportedOperationException.class,
            () -> car.getExteriorFeatures().add(ExteriorFeature.SPORT_RIMS));
        assertThrows(UnsupportedOperationException.class,
            () -> car.getSafetyFeatures().add(SafetyFeature.AIRBAGS));
    }
    
    @Test
    @DisplayName("Car should provide summary")
    void carShouldProvideSummary() {
        Car car = new Car(CarModel.SUV, EngineType.V8, TransmissionType.AUTOMATIC,
                Color.BLACK, 
                EnumSet.of(InteriorFeature.LEATHER),
                EnumSet.of(ExteriorFeature.SUNROOF),
                EnumSet.of(SafetyFeature.ABS));
        
        String summary = car.getSummary();
        
        assertTrue(summary.contains("SUV"));
        assertTrue(summary.contains("V8"));
        assertTrue(summary.contains("Automatic"));
        assertTrue(summary.contains("Black"));
        assertTrue(summary.contains("Leather"));
        assertTrue(summary.contains("Sunroof"));
        assertTrue(summary.contains("ABS"));
    }
    
    @Test
    @DisplayName("Car toString should be descriptive")
    void carToStringShouldBeDescriptive() {
        Car car = new Car(CarModel.SEDAN, EngineType.V6, TransmissionType.AUTOMATIC,
                Color.BLUE, EnumSet.noneOf(InteriorFeature.class),
                EnumSet.noneOf(ExteriorFeature.class), EnumSet.noneOf(SafetyFeature.class));
        
        String str = car.toString();
        assertTrue(str.contains("Blue"));
        assertTrue(str.contains("Sedan"));
        assertTrue(str.contains("V6"));
    }
    
    @Test
    @DisplayName("Car equals and hashCode should work correctly")
    void carEqualsAndHashCodeShouldWork() {
        Car car1 = new Car(CarModel.SEDAN, EngineType.V6, TransmissionType.AUTOMATIC,
                Color.BLACK, EnumSet.noneOf(InteriorFeature.class),
                EnumSet.noneOf(ExteriorFeature.class), EnumSet.noneOf(SafetyFeature.class));
        
        Car car2 = new Car(CarModel.SEDAN, EngineType.V6, TransmissionType.AUTOMATIC,
                Color.BLACK, EnumSet.noneOf(InteriorFeature.class),
                EnumSet.noneOf(ExteriorFeature.class), EnumSet.noneOf(SafetyFeature.class));
        
        Car car3 = new Car(CarModel.SUV, EngineType.V8, TransmissionType.AUTOMATIC,
                Color.BLACK, EnumSet.noneOf(InteriorFeature.class),
                EnumSet.noneOf(ExteriorFeature.class), EnumSet.noneOf(SafetyFeature.class));
        
        assertEquals(car1, car2);
        assertEquals(car1.hashCode(), car2.hashCode());
        assertNotEquals(car1, car3);
    }
    
    @Test
    @DisplayName("Car equals should handle null and different types")
    void carEqualsShouldHandleNullAndDifferentTypes() {
        Car car = new Car(CarModel.SEDAN, EngineType.V6, TransmissionType.AUTOMATIC,
                Color.BLACK, EnumSet.noneOf(InteriorFeature.class),
                EnumSet.noneOf(ExteriorFeature.class), EnumSet.noneOf(SafetyFeature.class));
        
        assertNotEquals(car, null);
        assertNotEquals(car, "not a car");
        assertEquals(car, car); // Same reference
    }
    
    // Enum tests
    
    @Test
    @DisplayName("EngineType should have display name and horsepower")
    void engineTypeShouldHaveProperties() {
        assertEquals("V6 Engine", EngineType.V6.getDisplayName());
        assertEquals(300, EngineType.V6.getHorsepower());
        assertEquals("V8 Engine", EngineType.V8.getDisplayName());
        assertEquals(450, EngineType.V8.getHorsepower());
    }
    
    @Test
    @DisplayName("All enums should have proper toString")
    void enumsShouldHaveProperToString() {
        assertNotNull(EngineType.V6.toString());
        assertNotNull(TransmissionType.MANUAL.toString());
        assertNotNull(InteriorFeature.LEATHER.toString());
        assertNotNull(ExteriorFeature.SUNROOF.toString());
        assertNotNull(SafetyFeature.ABS.toString());
        assertNotNull(Color.RED.toString());
    }
}
