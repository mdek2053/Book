package nl.tudelft.sem11b.http;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;

public class ApiResponse<S> {
    private final String service;
    private final S body;
    private final ApiException error;

    public ApiResponse(String service) {
        this.service = service;
        this.body = null;
        this.error = null;
    }

    public ApiResponse(String service, S body) {
        this.service = service;
        this.body = body;
        this.error = null;
    }

    public ApiResponse(String service, ApiException error) {
        this.service = service;
        this.body = null;
        this.error = error;
    }

    public boolean isSuccessful() {
        return error == null;
    }

    public ApiException getError() {
        return error;
    }

    public S unwrap() throws ApiException {
        var body = toOptional();
        if (body.isEmpty()) {
            throw new ApiException(service, "Body expected, but server didn't send any!");
        }

        return body.get();
    }

    public Optional<S> toOptional() throws ApiException {
        if (error != null) {
            throw error;
        }

        return Optional.ofNullable(body);
    }
}
