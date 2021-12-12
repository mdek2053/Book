package nl.tudelft.sem11b.http;

/**
 * Represents the identity of a user authenticated by a token. Token is sent in the
 * {@code Authorization} header.
 */
public class Authenticated implements Identity {
    private final Token token;
    private final String scheme;

    /**
     * Instantiates the {@link Authenticated} class.
     *
     * @param token Token authenticating the user
     * @param scheme Authentication scheme used in the {@code Authorization} header
     */
    public Authenticated(Token token, String scheme) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null!");
        }
        if (scheme == null || scheme.isBlank()) {
            throw new IllegalArgumentException("Scheme cannot be null nor empty!");
        }

        this.token = token;
        // ensure capitalization
        this.scheme = Character.toUpperCase(scheme.charAt(0)) + scheme.substring(1);
    }

    /**
     * Instantiates the {@link Authenticated} class with the {@code Bearer} scheme.
     *
     * @param token Token authenticating the user
     */
    public Authenticated(Token token) {
        this(token, "Bearer");
    }

    /**
     * Gets the authentication token of the current user.
     *
     * @return User's authentication token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Gets the authentication scheme used in the {@code Authorization} header.
     *
     * @return Authentication scheme
     */
    public String getScheme() {
        return scheme;
    }

    @Override
    public void authenticate(ApiRequest headers) {
        headers.header("Authorization", scheme + " " + token);
    }
}
