package nl.tudelft.sem11b.clients;

import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticatedServiceClientTest {

    //honestly no clue what the T should do here, I just made it an EquipmentModel
    @Mock
    Function<ApiClient<Authenticated>, EquipmentModel> factory;

    @Mock
    HttpServletRequest mockHttpRequest;

    ServletWebRequest servletWebRequest;

    AuthenticatedServiceClient client;

    @BeforeEach
    void setup() {
        servletWebRequest = new ServletWebRequest(mockHttpRequest);
        client = new AuthenticatedServiceClient(URI.create("http://localhost:8080"), "rooms", factory) {
            @Override
            protected Object openClient() {
                return super.openClient();
            }
        };
    }

    @Test
    void openClientAttNullTest() {
        RequestContextHolder.setRequestAttributes(null);

        assertThrows(RuntimeException.class, () -> client.openClient());
    }

    @Test
    void openClientNullAuthorizationTest() {
        RequestContextHolder.setRequestAttributes(servletWebRequest);

        when(mockHttpRequest.getHeader("Authorization"))
                .thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> client.openClient());
    }

    @Test
    void openClientBlankAuthorizationTest() {
        RequestContextHolder.setRequestAttributes(servletWebRequest);

        when(mockHttpRequest.getHeader("Authorization"))
                .thenReturn(" ");

        assertThrows(ResponseStatusException.class, () -> client.openClient());
    }

    @Test
    void openClientInvalidAuthorizationTest() {
        RequestContextHolder.setRequestAttributes(servletWebRequest);

        when(mockHttpRequest.getHeader("Authorization"))
                .thenReturn(" jasdlfjasoijeoji");

        assertThrows(ResponseStatusException.class, () -> client.openClient());
    }

    @Test
    void openClientSuccessFulTest() {
        RequestContextHolder.setRequestAttributes(servletWebRequest);

        EquipmentModel model = new EquipmentModel("yeet");

        when(mockHttpRequest.getHeader("Authorization"))
                .thenReturn("bearer E");

        when(factory.apply(any(ApiClient.class))).thenReturn(model);

        assertEquals(model, client.openClient());
    }

}
