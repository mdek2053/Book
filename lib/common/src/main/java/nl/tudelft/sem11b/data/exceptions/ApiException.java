package nl.tudelft.sem11b.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ServiceException {
    private final String service;
    private final String reason;

    public ApiException(String service, String reason) {
        super("API operation with " + service + " service failed!: " + reason);
        this.service = service;
        this.reason = reason;
    }

    public ApiException(String service, Throwable cause) {
        super("API operation with " + service + " service failed!: " + cause.getMessage(), cause);
        this.service = service;
        this.reason = null;
    }

    public String getService() {
        return service;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public ResponseStatusException toResponseException() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Call to other service failed!", this);
    }
}
