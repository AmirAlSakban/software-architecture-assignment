package car.builder;

/**
 * Exception thrown when car configuration is invalid.
 */
public class InvalidCarConfigurationException extends RuntimeException {
    
    public InvalidCarConfigurationException(String message) {
        super(message);
    }
    
    public InvalidCarConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
