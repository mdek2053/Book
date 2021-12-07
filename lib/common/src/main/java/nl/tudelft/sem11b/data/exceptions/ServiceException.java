package nl.tudelft.sem11b.data.exceptions;

import org.springframework.web.server.ResponseStatusException;

public abstract class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract ResponseStatusException toResponseException();
}
