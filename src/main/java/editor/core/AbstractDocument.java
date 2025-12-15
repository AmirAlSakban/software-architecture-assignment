package editor.core;

/**
 * Abstract base class for documents providing common functionality.
 * Concrete document implementations should extend this class.
 */
public abstract class AbstractDocument implements Document {
    
    protected final String title;
    protected String content;
    
    protected AbstractDocument(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Document title cannot be null or blank");
        }
        this.title = title;
        this.content = "";
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public void setContent(String content) {
        this.content = content != null ? content : "";
    }
    
    @Override
    public String getContent() {
        return content;
    }
}
