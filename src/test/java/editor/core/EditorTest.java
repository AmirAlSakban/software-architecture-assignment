package editor.core;

import editor.factory.DocumentFactory;
import editor.formats.PdfDocument;
import editor.formats.WordDocument;
import editor.formats.HtmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Document Editor (Part 1).
 */
class EditorTest {
    
    private DocumentFactory factory;
    private Editor editor;
    
    @BeforeEach
    void setUp() {
        factory = DocumentFactory.createDefault();
        editor = new Editor(factory);
    }
    
    @Test
    @DisplayName("Editor should create PDF document via factory")
    void shouldCreatePdfDocument() {
        editor.newDocument("pdf", "Test PDF");
        
        Document doc = editor.getCurrentDocument();
        assertNotNull(doc);
        assertTrue(doc instanceof PdfDocument);
        assertEquals("Test PDF", doc.getTitle());
        assertEquals("pdf", doc.getFormatKey());
    }
    
    @Test
    @DisplayName("Editor should create Word document via factory")
    void shouldCreateWordDocument() {
        editor.newDocument("word", "Test Word");
        
        Document doc = editor.getCurrentDocument();
        assertNotNull(doc);
        assertTrue(doc instanceof WordDocument);
        assertEquals("Test Word", doc.getTitle());
        assertEquals("word", doc.getFormatKey());
    }
    
    @Test
    @DisplayName("Editor should create HTML document via factory")
    void shouldCreateHtmlDocument() {
        editor.newDocument("html", "Test HTML");
        
        Document doc = editor.getCurrentDocument();
        assertNotNull(doc);
        assertTrue(doc instanceof HtmlDocument);
        assertEquals("Test HTML", doc.getTitle());
        assertEquals("html", doc.getFormatKey());
    }
    
    @Test
    @DisplayName("Editor should work with case-insensitive format keys")
    void shouldHandleCaseInsensitiveFormatKeys() {
        editor.newDocument("PDF", "Test");
        assertTrue(editor.getCurrentDocument() instanceof PdfDocument);
        
        editor.newDocument("Word", "Test");
        assertTrue(editor.getCurrentDocument() instanceof WordDocument);
        
        editor.newDocument("HTML", "Test");
        assertTrue(editor.getCurrentDocument() instanceof HtmlDocument);
    }
    
    @Test
    @DisplayName("Editor should throw exception for unknown format")
    void shouldThrowExceptionForUnknownFormat() {
        UnknownDocumentFormatException exception = assertThrows(
            UnknownDocumentFormatException.class,
            () -> editor.newDocument("xlsx", "Test")
        );
        
        assertEquals("xlsx", exception.getFormatKey());
        assertTrue(exception.getMessage().contains("Unknown document format"));
        assertTrue(exception.getMessage().contains("xlsx"));
    }
    
    @Test
    @DisplayName("Editor should edit document content")
    void shouldEditDocumentContent() {
        editor.newDocument("pdf", "Test")
              .edit("Hello World");
        
        assertEquals("Hello World", editor.getCurrentDocument().getContent());
    }
    
    @Test
    @DisplayName("Editor should preview document")
    void shouldPreviewDocument() {
        editor.newDocument("pdf", "Test")
              .edit("Content here");
        
        String preview = editor.preview();
        assertNotNull(preview);
        assertTrue(preview.contains("Test"));
        assertTrue(preview.contains("Content here"));
    }
    
    @Test
    @DisplayName("Editor should save document")
    void shouldSaveDocument() {
        editor.newDocument("pdf", "Test")
              .edit("Content");
        
        byte[] saved = editor.save();
        assertNotNull(saved);
        assertTrue(saved.length > 0);
    }
    
    @Test
    @DisplayName("Editor should throw exception when editing without document")
    void shouldThrowExceptionWhenEditingWithoutDocument() {
        assertThrows(IllegalStateException.class, () -> editor.edit("Content"));
    }
    
    @Test
    @DisplayName("Editor should throw exception when previewing without document")
    void shouldThrowExceptionWhenPreviewingWithoutDocument() {
        assertThrows(IllegalStateException.class, () -> editor.preview());
    }
    
    @Test
    @DisplayName("Editor should throw exception when saving without document")
    void shouldThrowExceptionWhenSavingWithoutDocument() {
        assertThrows(IllegalStateException.class, () -> editor.save());
    }
    
    @Test
    @DisplayName("Editor should close document")
    void shouldCloseDocument() {
        editor.newDocument("pdf", "Test");
        assertTrue(editor.hasOpenDocument());
        
        editor.closeDocument();
        assertFalse(editor.hasOpenDocument());
        assertNull(editor.getCurrentDocument());
    }
    
    @Test
    @DisplayName("Editor should not accept null factory")
    void shouldNotAcceptNullFactory() {
        assertThrows(IllegalArgumentException.class, () -> new Editor(null));
    }
    
    @Test
    @DisplayName("Editor should support fluent API")
    void shouldSupportFluentApi() {
        byte[] result = editor.newDocument("pdf", "Test")
                              .edit("Content")
                              .save();
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
