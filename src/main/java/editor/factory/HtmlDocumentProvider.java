package editor.factory;

import editor.formats.HtmlDocument;
import editor.core.Document;

/**
 * Provider for HTML documents.
 */
public class HtmlDocumentProvider implements DocumentProvider {
    
    @Override
    public String formatKey() {
        return HtmlDocument.FORMAT_KEY;
    }
    
    @Override
    public Document create(String title) {
        return new HtmlDocument(title);
    }
}
