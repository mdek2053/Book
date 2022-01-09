package nl.tudelft.sem11b.http;

import java.net.URI;
import java.net.http.HttpRequest;

public class ApiRequestTestImpl extends ApiRequest {
    /**
     * Instantiates the {@link ApiRequest} class.
     *
     * @param method API request method
     * @param uri    URI of request
     */
    public ApiRequestTestImpl(String method, URI uri) {
        super(method, uri);
    }

    @Override
    protected HttpRequest.BodyPublisher createBody(Coding coding) {
        return null;
    }
}
