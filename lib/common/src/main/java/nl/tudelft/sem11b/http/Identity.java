package nl.tudelft.sem11b.http;

public interface Identity {
    void authenticate(ApiRequest headers);
}
