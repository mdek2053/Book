package nl.tudelft.sem11b.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidData extends ServiceException{
    private String reason;

    public InvalidData(String reason) {
        super(reason);
    }

    public String getReason() {
        return reason;
    }

    @Override
    public ResponseStatusException toResponseException() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body!: " + reason);
    }
}
