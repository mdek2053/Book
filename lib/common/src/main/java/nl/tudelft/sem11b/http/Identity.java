package nl.tudelft.sem11b.http;

/**
 * Interface for API identity providers. See {@link Anonymous} class for the identity of an
 * unauthorized user or the {@link Authenticated} class for the identity of an authorized user.
 */
public interface Identity {
    /**
     * Adds authentication information to the given API request headers.
     *
     * @param headers API request headers to add authentication information to
     */
    void authenticate(ApiRequest headers);
}
