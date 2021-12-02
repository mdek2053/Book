package nl.tudelft.sem11b.data.exception;

public class ForbiddenException extends Exception {
    public String reason;

    public ForbiddenException() {
        reason = "";
    }

    public ForbiddenException(String reason) {
        this.reason = reason;
    }
}
