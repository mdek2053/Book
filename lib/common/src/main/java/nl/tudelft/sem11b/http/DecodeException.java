package nl.tudelft.sem11b.http;

public class DecodeException extends Exception {
    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
