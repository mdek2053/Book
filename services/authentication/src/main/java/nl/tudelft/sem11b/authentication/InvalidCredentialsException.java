package nl.tudelft.sem11b.authentication;

public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String s) {
        super(s);
    }
}
