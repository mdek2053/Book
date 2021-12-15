package nl.tudelft.sem11b.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when request data is invalid.
 */
public class InvalidData extends ServiceException {
    private String reason;

    /**
     * Instantiates the {@link InvalidData} class.
     *
     * @param reason Error message
     */
    public InvalidData(String reason) {
        super(reason);
    }

    /**
     * Gets the error message.
     *
     * @return Error message
     */
    public String getReason() {
        return reason;
    }

    @Override
    public ResponseStatusException toResponseException() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Invalid request body!: " + reason);
    }
}
