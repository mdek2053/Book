package nl.tudelft.sem11b.data.exception;

/**
 * Provides an exception which can be used when the provided credentials are invalid.
 */
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String s) {
        super(s);
    }
}
