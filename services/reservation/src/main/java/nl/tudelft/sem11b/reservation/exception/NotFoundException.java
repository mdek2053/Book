package nl.tudelft.sem11b.reservation.exception;

public class NotFoundException extends Exception {
    public String reason;

    public NotFoundException() {
        reason = "";
    }

    public NotFoundException(String reason) {
        this.reason = reason;
    }
}
