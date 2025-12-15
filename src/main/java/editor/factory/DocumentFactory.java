package editor.factory;

import editor.core.Document;
import editor.core.UnknownDocumentFormatException;

import java.util.*;

/**
 * Factory for creating documents using a registry-based approach.
 * Document providers are registered at startup, allowing new formats
 * to be added without modifying this class or the editor.
 */
public class DocumentFactory {
    
    private final Map<String, DocumentProvider> registry;
    
    public DocumentFactory() {
        this.registry = new HashMap<>();
    }
    
    /**
     * Registers a document provider.
     * @param provider the provider to register
     * @return this factory for fluent chaining
     */
    public DocumentFactory register(DocumentProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("Provider cannot be null");
        }
        registry.put(provider.formatKey().toLowerCase(), provider);
        return this;
    }
    
    /**
     * Registers multiple document providers.
     * @param providers the providers to register
     * @return this factory for fluent chaining
     */
    public DocumentFactory registerAll(DocumentProvider... providers) {
        for (DocumentProvider provider : providers) {
            register(provider);
        }
        return this;
    }
    
    /**
     * Creates a document of the specified format.
     * @param formatKey the format key (case-insensitive)
     * @param title the document title
     * @return a new Document instance
     * @throws UnknownDocumentFormatException if the format is not registered
     */
    public Document createDocument(String formatKey, String title) {
        String key = formatKey.toLowerCase();
        DocumentProvider provider = registry.get(key);
        
        if (provider == null) {
            throw new UnknownDocumentFormatException(formatKey);
        }
        
        return provider.create(title);
    }
    
    /**
     * Checks if a format is supported.
     * @param formatKey the format key to check
     * @return true if the format is registered
     */
    public boolean supportsFormat(String formatKey) {
        return registry.containsKey(formatKey.toLowerCase());
    }
    
    /**
     * Gets all registered format keys.
     * @return unmodifiable set of format keys
     */
    public Set<String> getSupportedFormats() {
        return Collections.unmodifiableSet(registry.keySet());
    }
    
    /**
     * Creates a factory with default document providers (PDF, Word, HTML).
     * @return a pre-configured DocumentFactory
     */
    public static DocumentFactory createDefault() {
        return new DocumentFactory()
                .register(new PdfDocumentProvider())
                .register(new WordDocumentProvider())
                .register(new HtmlDocumentProvider());
    }
}
