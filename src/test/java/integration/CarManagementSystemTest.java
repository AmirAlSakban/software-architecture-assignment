package integration;

import car.builder.CarBuilder;
import car.domain.*;
import editor.core.Document;
import editor.factory.DocumentFactory;
import editor.formats.PdfDocument;
import editor.formats.WordDocument;
import editor.formats.HtmlDocument;
import integration.order.Order;
import integration.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Car Management System (Bonus).
 */
class CarManagementSystemTest {
    
    private DocumentFactory documentFactory;
    private CarManagementSystem carSystem;
    private Car testCar;
    
    @BeforeEach
    void setUp() {
        documentFactory = DocumentFactory.createDefault();
        carSystem = new CarManagementSystem(documentFactory);
        
        // Create a test car with various features
        testCar = new CarBuilder()
            .withModel(CarModel.SUV)
            .withEngine(EngineType.V8)
            .withTransmission(TransmissionType.AUTOMATIC)
            .setColor(Color.BLACK)
            .addInteriorFeature(InteriorFeature.LEATHER)
            .addInteriorFeature(InteriorFeature.GPS)
            .addExteriorFeature(ExteriorFeature.SUNROOF)
            .addSafetyFeature(SafetyFeature.ABS)
            .addSafetyFeature(SafetyFeature.AIRBAGS)
            .build();
    }
    
    @Test
    @DisplayName("Should generate PDF document for car")
    void shouldGeneratePdfDocumentForCar() {
        Document doc = carSystem.generateCarDocument(testCar, "pdf");
        
        assertNotNull(doc);
        assertTrue(doc instanceof PdfDocument);
        assertTrue(doc.getTitle().contains("SUV"));
        assertTrue(doc.getContent().contains("V8"));
        assertTrue(doc.getContent().contains("Automatic"));
    }
    
    @Test
    @DisplayName("Should generate Word document for car")
    void shouldGenerateWordDocumentForCar() {
        Document doc = carSystem.generateCarDocument(testCar, "word");
        
        assertNotNull(doc);
        assertTrue(doc instanceof WordDocument);
        assertTrue(doc.getTitle().contains("SUV"));
        assertTrue(doc.getContent().contains("V8"));
    }
    
    @Test
    @DisplayName("Should generate HTML document for car")
    void shouldGenerateHtmlDocumentForCar() {
        Document doc = carSystem.generateCarDocument(testCar, "html");
        
        assertNotNull(doc);
        assertTrue(doc instanceof HtmlDocument);
        assertTrue(doc.getTitle().contains("SUV"));
        assertTrue(doc.getContent().contains("V8"));
    }
    
    @Test
    @DisplayName("Document title should contain car model")
    void documentTitleShouldContainCarModel() {
        Document pdfDoc = carSystem.generateCarDocument(testCar, "pdf");
        Document wordDoc = carSystem.generateCarDocument(testCar, "word");
        Document htmlDoc = carSystem.generateCarDocument(testCar, "html");
        
        assertTrue(pdfDoc.getTitle().contains("SUV"));
        assertTrue(wordDoc.getTitle().contains("SUV"));
        assertTrue(htmlDoc.getTitle().contains("SUV"));
    }
    
    @Test
    @DisplayName("Document content should include engine info")
    void documentContentShouldIncludeEngineInfo() {
        Document doc = carSystem.generateCarDocument(testCar, "pdf");
        
        String content = doc.getContent();
        assertTrue(content.contains("V8"));
        assertTrue(content.contains("Engine"));
    }
    
    @Test
    @DisplayName("Document content should include transmission info")
    void documentContentShouldIncludeTransmissionInfo() {
        Document doc = carSystem.generateCarDocument(testCar, "pdf");
        
        String content = doc.getContent();
        assertTrue(content.contains("Automatic"));
        assertTrue(content.contains("Transmission"));
    }
    
    @Test
    @DisplayName("Document content should include features")
    void documentContentShouldIncludeFeatures() {
        Document doc = carSystem.generateCarDocument(testCar, "pdf");
        
        String content = doc.getContent();
        assertTrue(content.contains("Leather"));
        assertTrue(content.contains("GPS"));
        assertTrue(content.contains("Sunroof"));
        assertTrue(content.contains("ABS"));
    }
    
    @Test
    @DisplayName("Should preview car document")
    void shouldPreviewCarDocument() {
        String preview = carSystem.previewCarDocument(testCar, "pdf");
        
        assertNotNull(preview);
        assertTrue(preview.contains("[PDF Preview]"));
        assertTrue(preview.contains("SUV"));
    }
    
    @Test
    @DisplayName("Should save car document")
    void shouldSaveCarDocument() {
        byte[] saved = carSystem.generateAndSaveCarDocument(testCar, "pdf");
        
        assertNotNull(saved);
        assertTrue(saved.length > 0);
        
        String content = new String(saved);
        assertTrue(content.contains("%PDF"));
    }
    
    @Test
    @DisplayName("Should generate documents in all formats for same car")
    void shouldGenerateDocumentsInAllFormatsForSameCar() {
        Document pdfDoc = carSystem.generateCarDocument(testCar, "pdf");
        Document wordDoc = carSystem.generateCarDocument(testCar, "word");
        Document htmlDoc = carSystem.generateCarDocument(testCar, "html");
        
        // All should contain the same car information
        String pdfContent = pdfDoc.getContent();
        String wordContent = wordDoc.getContent();
        String htmlContent = htmlDoc.getContent();
        
        assertTrue(pdfContent.contains("SUV"));
        assertTrue(wordContent.contains("SUV"));
        assertTrue(htmlContent.contains("SUV"));
        
        // But rendered differently
        String pdfRender = pdfDoc.render();
        String wordRender = wordDoc.render();
        String htmlRender = htmlDoc.render();
        
        assertTrue(pdfRender.contains("[PDF Preview]"));
        assertTrue(wordRender.contains("[Word Document Preview]"));
        assertTrue(htmlRender.contains("[HTML Preview]"));
    }

    @Test
    @DisplayName("Should include order metadata when provided")
    void shouldIncludeOrderMetadataWhenProvided() {
        OrderService orderService = carSystem.getOrderService();
        Order order = orderService.placeOrder(testCar);

        Document doc = carSystem.generateCarDocument(testCar, "pdf", order);
        assertTrue(doc.getContent().contains(order.getId().toString()));
    }
    
    @Test
    @DisplayName("Correct document implementation should be used via factory")
    void correctDocumentImplementationShouldBeUsedViaFactory() {
        Document pdf = carSystem.generateCarDocument(testCar, "pdf");
        Document word = carSystem.generateCarDocument(testCar, "word");
        Document html = carSystem.generateCarDocument(testCar, "html");
        
        assertEquals("pdf", pdf.getFormatKey());
        assertEquals("word", word.getFormatKey());
        assertEquals("html", html.getFormatKey());
    }
    
    @Test
    @DisplayName("Should get underlying editor")
    void shouldGetUnderlyingEditor() {
        assertNotNull(carSystem.getEditor());
    }
    
    @Test
    @DisplayName("Should get report generator")
    void shouldGetReportGenerator() {
        assertNotNull(carSystem.getReportGenerator());
    }
}
