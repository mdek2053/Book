package nl.tudelft.sem11b.authentication.exceptions;

/**
 * Provides an exception which can be used when the provided credentials are invalid.
 */
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String s) {
        super(s);
    }
}
