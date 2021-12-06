package nl.tudelft.sem11b.data.exception;

public class UnauthorizedException extends Exception {
    public String reason;

    public UnauthorizedException() {
        reason = "";
    }

    public UnauthorizedException(String reason) {
        this.reason = reason;
    }
}
