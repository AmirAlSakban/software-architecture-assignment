package editor.factory;

import editor.formats.WordDocument;
import editor.core.Document;

/**
 * Provider for Word documents.
 */
public class WordDocumentProvider implements DocumentProvider {
    
    @Override
    public String formatKey() {
        return WordDocument.FORMAT_KEY;
    }
    
    @Override
    public Document create(String title) {
        return new WordDocument(title);
    }
}
