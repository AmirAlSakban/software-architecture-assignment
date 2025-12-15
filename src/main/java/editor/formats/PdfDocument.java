package editor.formats;

import editor.core.AbstractDocument;

import java.nio.charset.StandardCharsets;

/**
 * PDF document implementation.
 * Simulates PDF-specific save and render behavior.
 */
public class PdfDocument extends AbstractDocument {
    
    public static final String FORMAT_KEY = "pdf";
    
    public PdfDocument(String title) {
        super(title);
    }
    
    @Override
    public byte[] save() {
        // Simulate PDF binary format with header
        String pdfContent = "%PDF-1.4\n" +
                "% " + title + "\n" +
                "1 0 obj\n" +
                "<< /Type /Catalog /Pages 2 0 R >>\n" +
                "endobj\n" +
                "2 0 obj\n" +
                "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n" +
                "endobj\n" +
                "3 0 obj\n" +
                "<< /Type /Page /Parent 2 0 R /Contents 4 0 R >>\n" +
                "endobj\n" +
                "4 0 obj\n" +
                "<< /Length " + content.length() + " >>\n" +
                "stream\n" +
                content + "\n" +
                "endstream\n" +
                "endobj\n" +
                "%%EOF";
        return pdfContent.getBytes(StandardCharsets.UTF_8);
    }
    
    @Override
    public String render() {
        return "[PDF Preview]\n" +
               "═══════════════════════════════════\n" +
               "Title: " + title + "\n" +
               "───────────────────────────────────\n" +
               content + "\n" +
               "═══════════════════════════════════\n" +
               "[End of PDF Preview]";
    }
    
    @Override
    public String getFormatKey() {
        return FORMAT_KEY;
    }
}
