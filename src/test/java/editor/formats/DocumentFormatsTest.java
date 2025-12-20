package editor.formats;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for document format implementations.
 */
class DocumentFormatsTest {
    
    // PDF Document Tests
    
    @Test
    @DisplayName("PdfDocument should have correct format key")
    void pdfShouldHaveCorrectFormatKey() {
        PdfDocument pdf = new PdfDocument("Test");
        assertEquals("pdf", pdf.getFormatKey());
    }
    
    @Test
    @DisplayName("PdfDocument should render with PDF markers")
    void pdfShouldRenderWithMarkers() {
        PdfDocument pdf = new PdfDocument("Test Title");
        pdf.setContent("Test content");
        
        String rendered = pdf.render();
        assertTrue(rendered.contains("[PDF Preview]"));
        assertTrue(rendered.contains("Test Title"));
        assertTrue(rendered.contains("Test content"));
        assertTrue(rendered.contains("[End of PDF Preview]"));
    }
    
    @Test
    @DisplayName("PdfDocument should save with PDF header")
    void pdfShouldSaveWithHeader() {
        PdfDocument pdf = new PdfDocument("Test");
        pdf.setContent("Content");
        
        byte[] saved = pdf.save();

        try (PDDocument doc = PDDocument.load(saved)) {
            String text = new PDFTextStripper().getText(doc);
            assertTrue(text.contains("Test"));
            assertTrue(text.contains("Content"));
        } catch (Exception ex) {
            fail("PDF should be readable: " + ex.getMessage());
        }
    }
    
    // Word Document Tests
    
    @Test
    @DisplayName("WordDocument should have correct format key")
    void wordShouldHaveCorrectFormatKey() {
        WordDocument word = new WordDocument("Test");
        assertEquals("word", word.getFormatKey());
    }
    
    @Test
    @DisplayName("WordDocument should render with Word markers")
    void wordShouldRenderWithMarkers() {
        WordDocument word = new WordDocument("Test Title");
        word.setContent("Test content");
        
        String rendered = word.render();
        assertTrue(rendered.contains("[Word Document Preview]"));
        assertTrue(rendered.contains("Test Title"));
        assertTrue(rendered.contains("Test content"));
        assertTrue(rendered.contains("[End of Word Preview]"));
    }
    
    @Test
    @DisplayName("WordDocument should save as XML")
    void wordShouldSaveAsXml() {
        WordDocument word = new WordDocument("Test");
        word.setContent("Content");
        
        byte[] saved = word.save();

        try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(saved))) {
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            String allText = paragraphs.stream().map(XWPFParagraph::getText).reduce("", (a, b) -> a + " " + b);
            assertTrue(allText.contains("Test"));
            assertTrue(allText.contains("Content"));
        } catch (Exception ex) {
            fail("DOCX should be readable: " + ex.getMessage());
        }
    }
    
    @Test
    @DisplayName("WordDocument should escape XML characters")
    void wordShouldEscapeXmlCharacters() {
        WordDocument word = new WordDocument("Test <>&\"'");
        word.setContent("<script>alert('xss')</script>");

        byte[] saved = word.save();

        try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(saved))) {
            String allText = doc.getParagraphs().stream().map(XWPFParagraph::getText).reduce("", (a, b) -> a + " " + b);
            assertTrue(allText.contains("Test <>"));
            assertTrue(allText.contains("alert('xss')"));
        } catch (Exception ex) {
            fail("DOCX should be readable: " + ex.getMessage());
        }
    }
    
    // HTML Document Tests
    
    @Test
    @DisplayName("HtmlDocument should have correct format key")
    void htmlShouldHaveCorrectFormatKey() {
        HtmlDocument html = new HtmlDocument("Test");
        assertEquals("html", html.getFormatKey());
    }

    @Test
    @DisplayName("HtmlDocument should escape content and use template")
    void htmlShouldEscapeContent() {
        HtmlDocument html = new HtmlDocument("Title & <tag>");
        html.setContent("Hello <World> & 'all'");

        byte[] saved = html.save();
        String content = new String(saved, StandardCharsets.UTF_8);

        assertTrue(content.contains("&lt;tag&gt;"));
        assertTrue(content.contains("Hello &lt;World&gt; &amp; &#39;all&#39;"));
        assertTrue(content.contains("<main>"));
    }
    
    @Test
    @DisplayName("HtmlDocument should render with HTML markers")
    void htmlShouldRenderWithMarkers() {
        HtmlDocument html = new HtmlDocument("Test Title");
        html.setContent("Test content");
        
        String rendered = html.render();
        assertTrue(rendered.contains("[HTML Preview]"));
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("<title>Test Title</title>"));
        assertTrue(rendered.contains("Test content"));
        assertTrue(rendered.contains("[End of HTML Preview]"));
    }
    
    @Test
    @DisplayName("HtmlDocument should save as full HTML")
    void htmlShouldSaveAsFullHtml() {
        HtmlDocument html = new HtmlDocument("Test");
        html.setContent("Content");
        
        byte[] saved = html.save();
        String content = new String(saved, StandardCharsets.UTF_8);
        
        assertTrue(content.contains("<!DOCTYPE html>"));
        assertTrue(content.contains("<html lang=\"en\">"));
        assertTrue(content.contains("<title>Test</title>"));
        assertTrue(content.contains("<h1>Test</h1>"));
        assertTrue(content.contains("Content"));
    }
    
    @Test
    @DisplayName("HtmlDocument should escape HTML characters")
    void htmlShouldEscapeHtmlCharacters() {
        HtmlDocument html = new HtmlDocument("Test <script>");
        html.setContent("<img onerror='alert(1)'>");
        
        byte[] saved = html.save();
        String content = new String(saved, StandardCharsets.UTF_8);
        
        assertTrue(content.contains("&lt;"));
        assertTrue(content.contains("&gt;"));
    }
    
    // Common tests for all documents
    
    @Test
    @DisplayName("Documents should handle null content")
    void documentsShouldHandleNullContent() {
        PdfDocument pdf = new PdfDocument("Test");
        pdf.setContent(null);
        assertEquals("", pdf.getContent());
        
        WordDocument word = new WordDocument("Test");
        word.setContent(null);
        assertEquals("", word.getContent());
        
        HtmlDocument html = new HtmlDocument("Test");
        html.setContent(null);
        assertEquals("", html.getContent());
    }
    
    @Test
    @DisplayName("Documents should reject null or blank title")
    void documentsShouldRejectInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> new PdfDocument(null));
        assertThrows(IllegalArgumentException.class, () -> new PdfDocument(""));
        assertThrows(IllegalArgumentException.class, () -> new PdfDocument("   "));
        
        assertThrows(IllegalArgumentException.class, () -> new WordDocument(null));
        assertThrows(IllegalArgumentException.class, () -> new HtmlDocument(null));
    }
    
    @Test
    @DisplayName("Each format should produce different output")
    void eachFormatShouldProduceDifferentOutput() {
        String title = "Same Title";
        String content = "Same Content";
        
        PdfDocument pdf = new PdfDocument(title);
        pdf.setContent(content);
        
        WordDocument word = new WordDocument(title);
        word.setContent(content);
        
        HtmlDocument html = new HtmlDocument(title);
        html.setContent(content);
        
        String pdfRender = pdf.render();
        String wordRender = word.render();
        String htmlRender = html.render();
        
        // All contain the content
        assertTrue(pdfRender.contains(content));
        assertTrue(wordRender.contains(content));
        assertTrue(htmlRender.contains(content));
        
        // But format differently
        assertNotEquals(pdfRender, wordRender);
        assertNotEquals(wordRender, htmlRender);
        assertNotEquals(pdfRender, htmlRender);
        
        // Save outputs also differ
        String pdfSaved = new String(pdf.save(), StandardCharsets.UTF_8);
        String wordSaved = new String(word.save(), StandardCharsets.UTF_8);
        String htmlSaved = new String(html.save(), StandardCharsets.UTF_8);
        
        assertNotEquals(pdfSaved, wordSaved);
        assertNotEquals(wordSaved, htmlSaved);
    }
}
