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

/**
 * A base class for authenticated API client, Spring service proxies. Note that classes deriving
 * this class will require a Tomcat environment, so they are ill-suited for unit testing. It is thus
 * highly recommended to keep code in derived classes to an absolute minimum.
 *
 * @param <T> Type of API client
 */
public abstract class AuthenticatedServiceClient<T> {
    protected final URI uri;
    protected final String service;
    protected final Function<ApiClient<Authenticated>, T> factory;

    /**
     * Instantiates the {@link AuthenticatedServiceClient} class.
     *
     * @param uri Base URI of the API
     * @param service Name of the service
     * @param factory Factory method for the API client
     */
    public AuthenticatedServiceClient(URI uri, String service,
                                      Function<ApiClient<Authenticated>, T> factory) {
        this.uri = uri;
        this.service = service;
        this.factory = factory;
    }

    /**
     * Creates a new authenticated API client. Authentication is fetched from the `Authorization`
     * header of the current HTTP request. Note that this method requires Spring MVC environment.
     *
     * @return New API client
     */
    protected T openClient() {
        // obtain authentication token
        var token = getToken();
        if (token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Authorization header not found or is invalid!");
        }

        // create a new authenticated API client
        var api = new ApiClient<>(uri, new Authenticated(token.get()), service);
        return factory.apply(api);
    }

    /**
     * Obtains the authentication token from the current HTTP request. This method will throw an
     * exception if invoked outside a running Tomcat environment.
     *
     * @return Token if given; an empty optional otherwise
     */
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
