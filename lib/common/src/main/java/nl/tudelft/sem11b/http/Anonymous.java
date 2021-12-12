package nl.tudelft.sem11b.http;

/**
 * Represents the identity if an unauthenticated user.
 */
public class Anonymous implements Identity {
    @Override
    public void authenticate(ApiRequest headers) {
        // anonymous identity bears no authentication details
    }
}
