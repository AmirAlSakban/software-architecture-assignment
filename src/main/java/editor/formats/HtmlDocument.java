package editor.formats;

import editor.core.AbstractDocument;

import java.nio.charset.StandardCharsets;

/**
 * HTML document implementation.
 * Simulates HTML-specific save and render behavior.
 */
public class HtmlDocument extends AbstractDocument {
    
    public static final String FORMAT_KEY = "html";
    
    public HtmlDocument(String title) {
        super(title);
    }
    
    @Override
    public byte[] save() {
        StringBuilder body = new StringBuilder();
        if (content != null && !content.isEmpty()) {
            for (String line : content.split("\n")) {
                body.append("        <p>").append(escapeHtml(line)).append("</p>\n");
            }
        }

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>" + escapeHtml(title) + "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <main>\n" +
                "    <h1>" + escapeHtml(title) + "</h1>\n" +
                "    <div class=\"content\">\n" + body +
                "    </div>\n" +
                "  </main>\n" +
                "</body>\n" +
                "</html>";
        return htmlContent.getBytes(StandardCharsets.UTF_8);
    }
    
    @Override
    public String render() {
        return "[HTML Preview]\n" +
               "<html>\n" +
               "  <head><title>" + title + "</title></head>\n" +
               "  <body>\n" +
               "    <h1>" + title + "</h1>\n" +
               "    <p>" + content + "</p>\n" +
               "  </body>\n" +
               "</html>\n" +
               "[End of HTML Preview]";
    }
    
    @Override
    public String getFormatKey() {
        return FORMAT_KEY;
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
