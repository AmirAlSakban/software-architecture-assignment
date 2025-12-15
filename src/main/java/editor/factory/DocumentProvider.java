package editor.factory;

import editor.core.Document;

/**
 * Provider interface for document creation.
 * Implementations provide document instances for specific formats.
 * This follows the Service Provider Interface (SPI) pattern.
 */
public interface DocumentProvider {
    
    /**
     * Returns the format key this provider handles.
     * @return the format key (e.g., "pdf", "word", "html")
     */
    String formatKey();
    
    /**
     * Creates a new document with the given title.
     * @param title the document title
     * @return a new Document instance
     */
    Document create(String title);
}
