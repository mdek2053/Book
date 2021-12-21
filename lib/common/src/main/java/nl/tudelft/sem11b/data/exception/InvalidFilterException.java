package nl.tudelft.sem11b.data.exception;

public class InvalidFilterException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidFilterException(String s) {
        super(s);
    }
}
