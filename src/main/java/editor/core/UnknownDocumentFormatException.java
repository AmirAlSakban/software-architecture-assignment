package editor.core;

import java.util.Set;
import java.util.TreeSet;

/**
 * Exception thrown when an unknown document format is requested.
 */
public class UnknownDocumentFormatException extends RuntimeException {
    
    private final String formatKey;
    private final Set<String> supportedFormats;
    
    public UnknownDocumentFormatException(String formatKey, Set<String> supportedFormats) {
        super("Unknown document format: '" + formatKey + "'. Supported formats: " + String.join(", ", new TreeSet<>(supportedFormats)));
        this.formatKey = formatKey;
        this.supportedFormats = supportedFormats == null ? Set.of() : Set.copyOf(supportedFormats);
    }
    
    public String getFormatKey() {
        return formatKey;
    }

    public Set<String> getSupportedFormats() {
        return supportedFormats;
    }
}
