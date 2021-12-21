package nl.tudelft.sem11b.http;

import java.util.Objects;

/**
 * Represents an authentication token.
 */
public class Token {
    private final transient String token; //NOPMD

    /**
     * Instantiates the {@link Token} class.
     *
     * @param token String representation of the authentication token
     */
    public Token(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty!");
        }

        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }

    @Override public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Token that = (Token) other;
        return token.equals(that.token);
    }

    @Override public int hashCode() {
        return Objects.hash(token);
    }
}
