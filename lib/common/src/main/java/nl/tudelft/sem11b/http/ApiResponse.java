package nl.tudelft.sem11b.http;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;

/**
 * Represents an API response. Think of this class as a try monad.
 * @param <S> Type of response body
 */
public class ApiResponse<S> {
    private final String service;
    private final S body;
    private final ApiException error;

    /**
     * Instantiates the success variant of the {@link ApiResponse} class. This variant has no
     * response data.
     *
     * @param service Name of the service that produces this response
     */
    public ApiResponse(String service) {
        this.service = service;
        this.body = null;
        this.error = null;
    }

    /**
     * Instantiates the success variant of the {@link ApiResponse} class. This variant has response
     * data given in {@code body}.
     *
     * @param service Name of the service that produces this response
     * @param body    The body of the response
     */
    public ApiResponse(String service, S body) {
        this.service = service;
        this.body = body;
        this.error = null;
    }

    /**
     * Instantiates the failure variant of the {@link ApiResponse} class. This variant has an error
     * given in {@code error}.
     *
     * @param service Name of the service that produces this response
     * @param error   The error raised during a request
     */
    public ApiResponse(String service, ApiException error) {
        this.service = service;
        this.body = null;
        this.error = error;
    }

    /**
     * Checks if this instance is the success variant of the class.
     *
     * @return {@code true} if the response is successful; {@code false} otherwise
     */
    public boolean isSuccessful() {
        return error == null;
    }

    /**
     * Gets the error raised during the associated API request (if any).
     *
     * @return API error if any; {@code null} if none
     */
    public ApiException getError() {
        return error;
    }

    /**
     * Unwraps this try value, throwing an error if this response is unsuccessful or if the server
     * did not provide any body.
     *
     * @return Unwrapped response value
     * @throws ApiException Thrown when the associated API request resulted in an error
     */
    public S unwrap() throws ApiException {
        var body = toOptional();
        if (body.isEmpty()) {
            throw new ApiException(service, "Body expected, but server didn't send any!");
        }

        return body.get();
    }

    /**
     * Unwraps this try value, throwing an error if this response is unsuccessful.
     *
     * @return Unwrapped response value if any; An empty optional if server did not return any body
     * @throws ApiException Thrown when the associated API request resulted in an error
     */
    public Optional<S> toOptional() throws ApiException {
        if (error != null) {
            throw error;
        }

        return Optional.ofNullable(body);
    }
}
