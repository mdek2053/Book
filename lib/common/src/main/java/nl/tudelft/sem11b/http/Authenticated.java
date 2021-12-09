package nl.tudelft.sem11b.http;

public class Authenticated implements Identity {
    private final Token token;

    public Authenticated(Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null!");
        }

        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public void authenticate(ApiRequest headers) {
        headers.header("Authorization", "Bearer " + token);
    }
}
