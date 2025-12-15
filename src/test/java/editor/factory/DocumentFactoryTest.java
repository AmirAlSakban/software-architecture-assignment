package editor.factory;

import editor.core.Document;
import editor.core.UnknownDocumentFormatException;
import editor.formats.PdfDocument;
import editor.formats.WordDocument;
import editor.formats.HtmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DocumentFactory and registry-based approach.
 */
class DocumentFactoryTest {
    
    private DocumentFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new DocumentFactory();
    }
    
    @Test
    @DisplayName("Factory should register and create PDF documents")
    void shouldCreatePdfDocument() {
        factory.register(new PdfDocumentProvider());
        
        Document doc = factory.createDocument("pdf", "Test");
        
        assertTrue(doc instanceof PdfDocument);
        assertEquals("Test", doc.getTitle());
    }
    
    @Test
    @DisplayName("Factory should register and create Word documents")
    void shouldCreateWordDocument() {
        factory.register(new WordDocumentProvider());
        
        Document doc = factory.createDocument("word", "Test");
        
        assertTrue(doc instanceof WordDocument);
        assertEquals("Test", doc.getTitle());
    }
    
    @Test
    @DisplayName("Factory should register and create HTML documents")
    void shouldCreateHtmlDocument() {
        factory.register(new HtmlDocumentProvider());
        
        Document doc = factory.createDocument("html", "Test");
        
        assertTrue(doc instanceof HtmlDocument);
        assertEquals("Test", doc.getTitle());
    }
    
    @Test
    @DisplayName("Factory should support fluent registration")
    void shouldSupportFluentRegistration() {
        factory.register(new PdfDocumentProvider())
               .register(new WordDocumentProvider())
               .register(new HtmlDocumentProvider());
        
        assertTrue(factory.supportsFormat("pdf"));
        assertTrue(factory.supportsFormat("word"));
        assertTrue(factory.supportsFormat("html"));
    }
    
    @Test
    @DisplayName("Factory should support registerAll")
    void shouldSupportRegisterAll() {
        factory.registerAll(
            new PdfDocumentProvider(),
            new WordDocumentProvider(),
            new HtmlDocumentProvider()
        );
        
        assertEquals(3, factory.getSupportedFormats().size());
    }
    
    @Test
    @DisplayName("Factory should throw exception for unknown format")
    void shouldThrowExceptionForUnknownFormat() {
        assertThrows(UnknownDocumentFormatException.class,
            () -> factory.createDocument("unknown", "Test"));
    }
    
    @Test
    @DisplayName("Factory should be case insensitive")
    void shouldBeCaseInsensitive() {
        factory.register(new PdfDocumentProvider());
        
        assertNotNull(factory.createDocument("PDF", "Test"));
        assertNotNull(factory.createDocument("Pdf", "Test"));
        assertNotNull(factory.createDocument("pdf", "Test"));
    }
    
    @Test
    @DisplayName("Factory should check supported formats")
    void shouldCheckSupportedFormats() {
        factory.register(new PdfDocumentProvider());
        
        assertTrue(factory.supportsFormat("pdf"));
        assertFalse(factory.supportsFormat("docx"));
    }
    
    @Test
    @DisplayName("Factory should return supported formats")
    void shouldReturnSupportedFormats() {
        factory = DocumentFactory.createDefault();
        
        var formats = factory.getSupportedFormats();
        assertEquals(3, formats.size());
        assertTrue(formats.contains("pdf"));
        assertTrue(formats.contains("word"));
        assertTrue(formats.contains("html"));
    }
    
    @Test
    @DisplayName("Factory should not accept null provider")
    void shouldNotAcceptNullProvider() {
        assertThrows(IllegalArgumentException.class, () -> factory.register(null));
    }
    
    @Test
    @DisplayName("createDefault should return pre-configured factory")
    void createDefaultShouldReturnPreConfiguredFactory() {
        factory = DocumentFactory.createDefault();
        
        assertNotNull(factory.createDocument("pdf", "Test"));
        assertNotNull(factory.createDocument("word", "Test"));
        assertNotNull(factory.createDocument("html", "Test"));
    }
    
    @Test
    @DisplayName("New format can be registered without modifying factory")
    void newFormatCanBeRegisteredWithoutModifyingFactory() {
        // Custom document provider for a new format
        DocumentProvider markdownProvider = new DocumentProvider() {
            @Override
            public String formatKey() {
                return "markdown";
            }
            
            @Override
            public Document create(String title) {
                return new Document() {
                    private String content = "";
                    
                    @Override
                    public String getTitle() { return title; }
                    
                    @Override
                    public void setContent(String c) { this.content = c; }
                    
                    @Override
                    public String getContent() { return content; }
                    
                    @Override
                    public byte[] save() {
                        return ("# " + title + "\n\n" + content).getBytes();
                    }
                    
                    @Override
                    public String render() {
                        return "# " + title + "\n\n" + content;
                    }
                    
                    @Override
                    public String getFormatKey() { return "markdown"; }
                };
            }
        };
        
        factory.register(markdownProvider);
        
        Document doc = factory.createDocument("markdown", "Test MD");
        assertNotNull(doc);
        assertEquals("Test MD", doc.getTitle());
        assertEquals("markdown", doc.getFormatKey());
        
        doc.setContent("Hello **world**");
        assertTrue(doc.render().contains("# Test MD"));
        assertTrue(doc.render().contains("Hello **world**"));
    }
}
