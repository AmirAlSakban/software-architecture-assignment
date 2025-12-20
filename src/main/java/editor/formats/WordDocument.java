package editor.formats;

import editor.core.AbstractDocument;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFParagraph titlePara = doc.createParagraph();
            titlePara.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun titleRun = titlePara.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setText(title);

            if (content != null && !content.isEmpty()) {
                for (String line : content.split("\n")) {
                    XWPFParagraph bodyPara = doc.createParagraph();
                    bodyPara.setAlignment(ParagraphAlignment.LEFT);
                    XWPFRun bodyRun = bodyPara.createRun();
                    bodyRun.setFontSize(12);
                    bodyRun.setText(line);
                }
            }

            doc.write(out);
            return out.toByteArray();
        } catch (IOException ex) {
            return ("DOCX generation failed: " + ex.getMessage()).getBytes();
        }
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
}
