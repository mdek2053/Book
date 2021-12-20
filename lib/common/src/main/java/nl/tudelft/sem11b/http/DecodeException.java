package nl.tudelft.sem11b.http;

/**
 * Exception thrown when data cannot be successfully deserialized, because it is in invalid or
 * unreadable format.
 */
public class DecodeException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates the {@link DecodeException} class.
     *
     * @param message Error message
     */
    public DecodeException(String message) {
        super(message);
    }

    /**
     * Instantiates the {@link DecodeException} class.
     *
     * @param cause Inner exception
     */
    public DecodeException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates the {@link DecodeException} class.
     *
     * @param message Error message
     * @param cause   Inner exception
     */
    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
