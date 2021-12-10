package nl.tudelft.sem11b.clients;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.http.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

public abstract class AuthenticatedServiceClient<T> {
    protected final URI uri;
    protected final String service;
    protected final Function<ApiClient<Authenticated>, T> factory;

    public AuthenticatedServiceClient(URI uri, String service,
                                      Function<ApiClient<Authenticated>, T> factory) {
        this.uri = uri;
        this.service = service;
        this.factory = factory;
    }

    protected T openClient() {
        var token = getToken();
        if (token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Authorization header not found or is invalid!");
        }

        var api = new ApiClient<>(uri, new Authenticated(token.get()), service);
        return factory.apply(api);
    }

    private static Optional<Token> getToken() {
        var att = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (att == null) {
            throw new RuntimeException("Unable to access current HTTP request!");
        }

        var req = att.getRequest();
        var header = req.getHeader("Authorization");
        if (header == null || header.isBlank() || !header.toLowerCase().startsWith("bearer ")) {
            return Optional.empty();
        }

        return Optional.of(new Token(header.substring(7).trim()));
    }
}
