package nl.tudelft.sem11b.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

public abstract class ApiRequest {
    private final String method;
    private final URI uri;
    private final Map<String, List<String>> headers;

    public ApiRequest(String method, URI uri) {
        this.method = method;
        this.uri = uri;
        this.headers = new HashMap<>();
    }

    public void header(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key not given!");
        }

        var name = key.trim().toLowerCase();
        if (headers.containsKey(name)) {
            headers.get(name).add(value);
        } else {
            var list = new ArrayList<String>();
            list.add(value);

            headers.put(name, list);
        }
    }

    public HttpRequest toJava(Coding coding) {
        var req = HttpRequest
            .newBuilder()
            .uri(uri)
            .method(method, createBody(coding));

        for (var e : headers.entrySet()) {
            var key = e.getKey();
            for (var v : e.getValue()) {
                req.header(key, v);
            }
        }

        return req.build();
    }

    protected abstract HttpRequest.BodyPublisher createBody(Coding coding);

    public static ApiRequest get(URI uri) {
        return new PlainRequest("GET", uri);
    }

    public static <T> ApiRequest post(URI uri, T body, TypeReference<T> type) {
        return new BodyRequest<T>("POST", uri, body, type);
    }

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
        private final T value;
        private final TypeReference<T> type;

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
