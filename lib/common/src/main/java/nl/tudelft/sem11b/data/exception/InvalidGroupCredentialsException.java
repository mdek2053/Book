package nl.tudelft.sem11b.data.exception;

/**
 * Provides an exception for when provided credentials for a group are invalid.
 */
public class InvalidGroupCredentialsException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidGroupCredentialsException(String s) {
        super(s);
    }
}
