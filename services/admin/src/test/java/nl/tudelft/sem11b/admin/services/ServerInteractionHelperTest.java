package nl.tudelft.sem11b.admin.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpResponse;

import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

class ServerInteractionHelperTest {

    @Test
    void verifyUserAdmin() throws CommunicationException {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\n"
                + "  \"id\": 42,\n"
                + "  \"login\": \"jsmith\",\n"
                + "  \"roles\": [\n"
                + "    \"admin\"\n"
                + "  ]\n"
                + "}");

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertDoesNotThrow(() -> serverInteractionHelper.verifyUserAdmin("asd"));

    }

    @Test
    void verifyUserNotAdmin() throws CommunicationException {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\n"
                + "  \"id\": 42,\n"
                + "  \"login\": \"jsmith\",\n"
                + "  \"roles\": [\n"
                + "    \"\"\n"
                + "  ]\n"
                + "}");

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertThrows(UnauthorizedException.class,
                () -> serverInteractionHelper.verifyUserAdmin("asd"));

    }
}