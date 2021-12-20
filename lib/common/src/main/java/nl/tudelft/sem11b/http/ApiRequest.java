package nl.tudelft.sem11b.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Represents an API request. Note that this class is meant to be a high-level representation of the
 * request (not an exact HTTP request).
 */
public abstract class ApiRequest {
    private final transient String method;
    private final transient URI uri;
    private final transient Map<String, List<String>> headers;

    /**
     * Instantiates the {@link ApiRequest} class.
     *
     * @param method API request method
     * @param uri    URI of request
     */
    public ApiRequest(String method, URI uri) {
        this.method = method;
        this.uri = uri;
        this.headers = new HashMap<>();
    }

    /**
     * Adds a request header value.
     *
     * @param key   Key of the header to add
     * @param value Value to add
     */
    public void header(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key not given!");
        }

        var name = key.trim().toLowerCase(Locale.ENGLISH);
        if (headers.containsKey(name)) {
            headers.get(name).add(value);
        } else {
            var list = new ArrayList<String>();
            list.add(value);

            headers.put(name, list);
        }
    }

    /**
     * Constructs a Java HTTP request object from this high-level representation.
     *
     * @param coding Coding to use for body encoding
     * @return Java HTTP request object
     */
    public HttpRequest toJava(Coding coding) {
        var req = HttpRequest
            .newBuilder()
            .uri(uri)
            .method(method, createBody(coding));

        // add HTTP headers
        for (var e : headers.entrySet()) {
            var key = e.getKey();
            for (var v : e.getValue()) {
                req.header(key, v);
            }
        }

        return req.build();
    }

    /**
     * Creates a new HTTP body provider.
     *
     * @param coding Coding for data encoding
     * @return HTTP body publisher object
     */
    protected abstract HttpRequest.BodyPublisher createBody(Coding coding);

    /**
     * Creates a new {@code GET} API request.
     *
     * @param uri URI of request
     * @return New API request
     */
    public static ApiRequest get(URI uri) {
        return new PlainRequest("GET", uri);
    }

    /**
     * Creates a new {@code POST} API request.
     *
     * @param uri  URI of request
     * @param body Body of request
     * @param type The generics-preserving class mirror type
     * @param <T>  Type of request body
     * @return New API request
     */
    public static <T> ApiRequest post(URI uri, T body, TypeReference<T> type) {
        return new BodyRequest<T>("POST", uri, body, type);
    }

    /**
     * Creates a new {@code DELETE} API request.
     *
     * @param uri URI of request
     * @return New API request
     */
    public static ApiRequest delete(URI uri) {
        return new PlainRequest("DELETE", uri);
    }

    private static class PlainRequest extends ApiRequest {
        public PlainRequest(String method, URI uri) {
            super(method, uri);
        }

        @Override protected HttpRequest.BodyPublisher createBody(Coding coding) {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private static class BodyRequest<T> extends ApiRequest {
        private final transient T value;
        private final transient TypeReference<T> type;

        public BodyRequest(String method, URI uri, T value, TypeReference<T> type) {
            super(method, uri);
            this.value = value;
            this.type = type;
        }

        @Override
        protected HttpRequest.BodyPublisher createBody(Coding coding) {
            var stream = new ByteArrayOutputStream();
            var writer = new OutputStreamWriter(stream);
            try {
                // This should never fail
                coding.encode(writer, value, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return HttpRequest.BodyPublishers.ofByteArray(stream.toByteArray());
        }
    }
}
