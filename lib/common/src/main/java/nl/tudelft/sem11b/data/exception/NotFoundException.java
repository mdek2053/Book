package nl.tudelft.sem11b.data.exception;

public class NotFoundException extends Exception {
    public String reason;

    public NotFoundException() {
        reason = "";
    }

    public NotFoundException(String reason) {
        this.reason = reason;
    }
}
