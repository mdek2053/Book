package nl.tudelft.sem11b.http;

/**
 * Represents the identity of a user authenticated by a token. Token is sent in the
 * {@code Authorization} header, using the {@code Bearer} scheme.
 */
public class Authenticated implements Identity {
    private final Token token;

    /**
     * Instantiates the {@link Authenticated} class.
     *
     * @param token Token authenticating the user
     */
    public Authenticated(Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null!");
        }

        // TODO: Any authorization scheme

        this.token = token;
    }

    /**
     * Gets the authentication token of the current user.
     *
     * @return User's authentication token
     */
    public Token getToken() {
        return token;
    }

    @Override
    public void authenticate(ApiRequest headers) {
        headers.header("Authorization", "Bearer " + token);
    }
}
