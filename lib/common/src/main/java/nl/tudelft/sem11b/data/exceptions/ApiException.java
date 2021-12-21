package nl.tudelft.sem11b.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * A generic API exception. Most commonly thrown by {@link nl.tudelft.sem11b.http.ApiClient}.
 */
public class ApiException extends ServiceException {
    private static final long serialVersionUID = 1L;

    private final String service;
    private final String reason;

    /**
     * Instantiates the {@link ApiException} class.
     *
     * @param service Name of service that raised the error
     * @param reason  Description of error
     */
    public ApiException(String service, String reason) {
        super("API operation with " + service + " service failed!: " + reason);
        this.service = service;
        this.reason = reason;
    }

    /**
     * Instantiates the {@link ApiException} class.
     *
     * @param service Name of service that raised the error
     * @param cause   Inner exception
     */
    public ApiException(String service, Throwable cause) {
        super("API operation with " + service + " service failed!: " + cause.getMessage(), cause);
        this.service = service;
        this.reason = null; //NOPMD
    }

    /**
     * Gets name of the service that raised the error.
     *
     * @return Name of originating service
     */
    public String getService() {
        return service;
    }

    /**
     * Gets the description of error (if any).
     *
     * @return Description of error; {@code null} if none
     */
    public String getReason() {
        return reason;
    }

    @Override
    public ResponseStatusException toResponseException() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Call to other service failed!",
            this);
    }
}
