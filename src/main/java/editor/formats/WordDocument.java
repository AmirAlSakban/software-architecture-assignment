package editor.formats;

import editor.core.AbstractDocument;

import java.nio.charset.StandardCharsets;

/**
 * Word document implementation.
 * Simulates Word-specific save and render behavior.
 */
public class WordDocument extends AbstractDocument {
    
    public static final String FORMAT_KEY = "word";
    
    public WordDocument(String title) {
        super(title);
    }
    
    @Override
    public byte[] save() {
        // Simulate Word XML format (simplified DOCX structure)
        String wordContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n" +
                "  <w:body>\n" +
                "    <w:title>" + escapeXml(title) + "</w:title>\n" +
                "    <w:p>\n" +
                "      <w:r>\n" +
                "        <w:t>" + escapeXml(content) + "</w:t>\n" +
                "      </w:r>\n" +
                "    </w:p>\n" +
                "  </w:body>\n" +
                "</w:document>";
        return wordContent.getBytes(StandardCharsets.UTF_8);
    }
    
    @Override
    public String render() {
        return "[Word Document Preview]\n" +
               "┌─────────────────────────────────┐\n" +
               "│ " + title + "\n" +
               "├─────────────────────────────────┤\n" +
               "│ " + content.replace("\n", "\n│ ") + "\n" +
               "└─────────────────────────────────┘\n" +
               "[End of Word Preview]";
    }
    
    @Override
    public String getFormatKey() {
        return FORMAT_KEY;
    }
    
    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
