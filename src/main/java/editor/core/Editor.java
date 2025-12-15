package editor.core;

import editor.factory.DocumentFactory;

/**
 * Document Editor that works with any document format through abstractions.
 * The editor depends only on the Document interface and DocumentFactory,
 * allowing new document formats to be added without modifying this class.
 */
public class Editor {
    
    private final DocumentFactory documentFactory;
    private Document currentDocument;
    
    /**
     * Creates an editor with the specified document factory.
     * @param documentFactory the factory used to create documents
     */
    public Editor(DocumentFactory documentFactory) {
        if (documentFactory == null) {
            throw new IllegalArgumentException("DocumentFactory cannot be null");
        }
        this.documentFactory = documentFactory;
    }
    
    /**
     * Creates a new document of the specified format.
     * @param formatKey the document format (e.g., "pdf", "word", "html")
     * @param title the document title
     * @return this editor for fluent chaining
     * @throws UnknownDocumentFormatException if the format is not supported
     */
    public Editor newDocument(String formatKey, String title) {
        this.currentDocument = documentFactory.createDocument(formatKey, title);
        return this;
    }
    
    /**
     * Sets the content of the current document.
     * @param content the content to set
     * @return this editor for fluent chaining
     * @throws IllegalStateException if no document is currently open
     */
    public Editor edit(String content) {
        ensureDocumentOpen();
        currentDocument.setContent(content);
        return this;
    }
    
    /**
     * Renders a preview of the current document.
     * @return the rendered preview as text
     * @throws IllegalStateException if no document is currently open
     */
    public String preview() {
        ensureDocumentOpen();
        return currentDocument.render();
    }
    
    /**
     * Saves the current document.
     * @return the document as a byte array
     * @throws IllegalStateException if no document is currently open
     */
    public byte[] save() {
        ensureDocumentOpen();
        return currentDocument.save();
    }
    
    /**
     * Gets the current document being edited.
     * @return the current document, or null if none is open
     */
    public Document getCurrentDocument() {
        return currentDocument;
    }
    
    /**
     * Checks if a document is currently open.
     * @return true if a document is open
     */
    public boolean hasOpenDocument() {
        return currentDocument != null;
    }
    
    /**
     * Closes the current document.
     */
    public void closeDocument() {
        this.currentDocument = null;
    }
    
    /**
     * Gets the document factory used by this editor.
     * @return the document factory
     */
    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }
    
    private void ensureDocumentOpen() {
        if (currentDocument == null) {
            throw new IllegalStateException("No document is currently open. " +
                    "Use newDocument() to create a document first.");
        }
    }
}
