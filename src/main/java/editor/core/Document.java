package editor.core;

/**
 * Document abstraction defining the contract for all document types.
 * This interface allows the editor to work with any document format
 * without knowing the concrete implementation details.
 */
public interface Document {
    
    /**
     * Gets the document title.
     * @return the title of the document
     */
    String getTitle();
    
    /**
     * Sets the content of the document.
     * @param content the content to set
     */
    void setContent(String content);
    
    /**
     * Gets the current content of the document.
     * @return the document content
     */
    String getContent();
    
    /**
     * Saves the document and returns the binary representation.
     * @return byte array representing the saved document
     */
    byte[] save();
    
    /**
     * Renders the document as a text preview.
     * @return text representation of the document
     */
    String render();
    
    /**
     * Gets the format key identifying this document type.
     * @return the format key (e.g., "pdf", "word", "html")
     */
    String getFormatKey();
}
