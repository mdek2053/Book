package nl.tudelft.sem11b.http;

public class Anonymous implements Identity {
    @Override
    public void authenticate(ApiRequest headers) {
        // anonymous identity bears no authentication details
    }
}
