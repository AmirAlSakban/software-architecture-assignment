package integration;

import car.builder.CarBuilder;
import car.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CarReportGenerator.
 */
class CarReportGeneratorTest {
    
    private CarReportGenerator generator;
    private Car testCar;
    
    @BeforeEach
    void setUp() {
        generator = new CarReportGenerator();
        
        testCar = new CarBuilder()
            .withModel(CarModel.SUV)
            .withEngine(EngineType.V8)
            .withTransmission(TransmissionType.AUTOMATIC)
            .setColor(Color.BLACK)
            .addInteriorFeature(InteriorFeature.LEATHER)
            .addExteriorFeature(ExteriorFeature.SUNROOF)
            .addSafetyFeature(SafetyFeature.ABS)
            .build();
    }
    
    @Test
    @DisplayName("Should generate report with model info")
    void shouldGenerateReportWithModelInfo() {
        String report = generator.generateReport(testCar);
        
        assertTrue(report.contains("SUV"));
        assertTrue(report.contains("Model:"));
    }
    
    @Test
    @DisplayName("Should generate report with engine and transmission")
    void shouldGenerateReportWithPowertrain() {
        String report = generator.generateReport(testCar);
        
        assertTrue(report.contains("V8"));
        assertTrue(report.contains("Automatic"));
        assertTrue(report.contains("POWERTRAIN"));
    }
    
    @Test
    @DisplayName("Should generate report with color")
    void shouldGenerateReportWithColor() {
        String report = generator.generateReport(testCar);
        
        assertTrue(report.contains("Black"));
        assertTrue(report.contains("Color:"));
    }
    
    @Test
    @DisplayName("Should generate report with interior features")
    void shouldGenerateReportWithInteriorFeatures() {
        String report = generator.generateReport(testCar);
        
        assertTrue(report.contains("INTERIOR FEATURES"));
        assertTrue(report.contains("Leather"));
    }
    
    @Test
    @DisplayName("Should generate report with exterior features")
    void shouldGenerateReportWithExteriorFeatures() {
        String report = generator.generateReport(testCar);
        
        assertTrue(report.contains("EXTERIOR FEATURES"));
        assertTrue(report.contains("Sunroof"));
    }
    
    @Test
    @DisplayName("Should generate report with safety features")
    void shouldGenerateReportWithSafetyFeatures() {
        String report = generator.generateReport(testCar);
        
        assertTrue(report.contains("SAFETY FEATURES"));
        assertTrue(report.contains("ABS"));
    }
    
    @Test
    @DisplayName("Should generate appropriate title")
    void shouldGenerateAppropriateTitle() {
        String title = generator.generateTitle(testCar);
        
        assertTrue(title.contains("SUV"));
        assertTrue(title.contains("Configuration Report"));
    }
    
    @Test
    @DisplayName("Report should not include empty sections")
    void reportShouldNotIncludeEmptySections() {
        Car minimalCar = new CarBuilder()
            .withModel(CarModel.SEDAN)
            .withEngine(EngineType.V6)
            .withTransmission(TransmissionType.AUTOMATIC)
            .build();
        
        String report = generator.generateReport(minimalCar);
        
        // These sections should not appear when empty
        assertFalse(report.contains("INTERIOR FEATURES"));
        assertFalse(report.contains("EXTERIOR FEATURES"));
        assertFalse(report.contains("SAFETY FEATURES"));
    }
}
