package editor.factory;

import editor.formats.PdfDocument;
import editor.core.Document;

/**
 * Provider for PDF documents.
 */
public class PdfDocumentProvider implements DocumentProvider {
    
    @Override
    public String formatKey() {
        return PdfDocument.FORMAT_KEY;
    }
    
    @Override
    public Document create(String title) {
        return new PdfDocument(title);
    }
}
