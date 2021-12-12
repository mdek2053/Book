package nl.tudelft.sem11b.data.exceptions;

import org.springframework.web.server.ResponseStatusException;

/**
 * Base exception for all other API exceptions.
 */
public abstract class ServiceException extends Exception {
    /**
     * Instantiates the {@link ServiceException} class.
     *
     * @param message Error message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Instantiates the {@link ServiceException} class.
     *
     * @param cause Inner exception
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates the {@link ServiceException} class.
     *
     * @param message Error message
     * @param cause   Inner exception
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Converts the exception into a Spring response exception. Commonly used for forwarding of
     * remote errors.
     *
     * @return Spring response exception
     */
    public abstract ResponseStatusException toResponseException();
}
