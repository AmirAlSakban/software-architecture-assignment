package editor.formats;

import editor.core.AbstractDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            writeContent(document, page, title, content);

            document.save(out);
            return out.toByteArray();
        } catch (IOException ex) {
            String fallback = "PDF generation failed: " + ex.getMessage();
            return fallback.getBytes(StandardCharsets.UTF_8);
        }
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

    private static void writeContent(PDDocument document, PDPage page, String title, String content) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(title);
        for (String line : (content == null ? "" : content).split("\n")) {
            lines.add(line);
        }

        try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
            stream.setLeading(16f);
            stream.beginText();
            stream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            stream.newLineAtOffset(72, page.getMediaBox().getHeight() - 72);
            stream.showText(title);
            stream.newLine();

            stream.setFont(PDType1Font.HELVETICA, 12);

            int maxLines = 60;
            int written = 0;
            for (String line : lines.subList(1, lines.size())) {
                if (written >= maxLines) {
                    break;
                }
                for (String wrapped : wrap(line, 90)) {
                    if (written >= maxLines) break;
                    stream.showText(wrapped);
                    stream.newLine();
                    written++;
                }
            }

            stream.endText();
        }
    }

    private static List<String> wrap(String text, int width) {
        List<String> out = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            out.add("");
            return out;
        }
        int idx = 0;
        while (idx < text.length()) {
            int end = Math.min(idx + width, text.length());
            out.add(text.substring(idx, end));
            idx = end;
        }
        return out;
    }
}
