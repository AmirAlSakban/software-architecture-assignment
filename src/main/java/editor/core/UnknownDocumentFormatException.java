package editor.core;

/**
 * Exception thrown when an unknown document format is requested.
 */
public class UnknownDocumentFormatException extends RuntimeException {
    
    private final String formatKey;
    
    public UnknownDocumentFormatException(String formatKey) {
        super("Unknown document format: '" + formatKey + "'. " +
              "Please register a DocumentProvider for this format.");
        this.formatKey = formatKey;
    }
    
    public String getFormatKey() {
        return formatKey;
    }
}
