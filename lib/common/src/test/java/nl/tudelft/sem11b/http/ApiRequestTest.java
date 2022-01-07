package nl.tudelft.sem11b.http;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApiRequestTest {

    ApiRequest request = new ApiRequestTestImpl("test", URI.create("http://localhost:8080/"));

    public ApiRequestTest() throws URISyntaxException {

    }

    @Test
    void headerTestNullKey() {
        assertThrows(IllegalArgumentException.class, () -> request.header(null, "yeet"));
    }

    @Test
    void headerTestEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> request.header(" ", "yeet"));
    }

    @Test
    void headerTestSuccessful() {
        // I don't know how to verify this, I just test this and check whether it breaks
        request.header("Content", "chili");
    }

    @Test
    void headerTestSameHeader() {
        request.header("Content", "chili");
        request.header("Content", "chili con carne");
    }

}
