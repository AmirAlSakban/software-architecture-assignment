package integration;

import car.domain.Car;
import editor.core.Document;
import editor.core.Editor;
import editor.factory.DocumentFactory;

/**
 * Car Management System that integrates car configuration with document generation.
 * Demonstrates the combination of Builder pattern (Car) with Factory pattern (Document).
 */
public class CarManagementSystem {
    
    private final Editor editor;
    private final CarReportGenerator reportGenerator;
    
    public CarManagementSystem(DocumentFactory documentFactory) {
        this.editor = new Editor(documentFactory);
        this.reportGenerator = new CarReportGenerator();
    }
    
    public CarManagementSystem(Editor editor) {
        this.editor = editor;
        this.reportGenerator = new CarReportGenerator();
    }
    
    /**
     * Generates a document for the car configuration in the specified format.
     * 
     * @param car the car to document
     * @param formatKey the document format (e.g., "pdf", "word", "html")
     * @return the generated document
     */
    public Document generateCarDocument(Car car, String formatKey) {
        String title = reportGenerator.generateTitle(car);
        String content = reportGenerator.generateReport(car);
        
        editor.newDocument(formatKey, title)
              .edit(content);
        
        return editor.getCurrentDocument();
    }
    
    /**
     * Generates and saves a document for the car configuration.
     * 
     * @param car the car to document
     * @param formatKey the document format
     * @return the saved document as bytes
     */
    public byte[] generateAndSaveCarDocument(Car car, String formatKey) {
        generateCarDocument(car, formatKey);
        return editor.save();
    }
    
    /**
     * Generates a preview of the car document.
     * 
     * @param car the car to document
     * @param formatKey the document format
     * @return the document preview
     */
    public String previewCarDocument(Car car, String formatKey) {
        generateCarDocument(car, formatKey);
        return editor.preview();
    }
    
    /**
     * Gets the underlying editor.
     * @return the editor
     */
    public Editor getEditor() {
        return editor;
    }
    
    /**
     * Gets the report generator.
     * @return the report generator
     */
    public CarReportGenerator getReportGenerator() {
        return reportGenerator;
    }
}
