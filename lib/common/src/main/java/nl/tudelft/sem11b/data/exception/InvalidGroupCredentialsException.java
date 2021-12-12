package nl.tudelft.sem11b.data.exception;

/**
 * Provides an exception for when provided credentials for a group are invalid.
 */
public class InvalidGroupCredentialsException extends Exception {

    public InvalidGroupCredentialsException(String s) {
        super(s);
    }
}
