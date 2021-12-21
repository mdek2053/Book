package nl.tudelft.sem11b.data.exception;

/**
 * Provides an exception which can be used when the provided credentials are invalid.
 */
public class InvalidCredentialsException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException(String s) {
        super(s);
    }
}
