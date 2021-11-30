package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.HttpHelper;
import nl.tudelft.sem11b.reservation.ServerInteractionHelper;
import nl.tudelft.sem11b.reservation.exceptions.BadTokenException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")

@SpringBootTest
class ServerInteractionHelperTest {
    @Test
    void getUserIdTestOkay() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{id: 50}");

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertEquals(50, serverInteractionHelper.getUserId("asd"));
    }

    @Test
    void getUserIdTestBadToken() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(403);

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertThrows(BadTokenException.class, () -> serverInteractionHelper.getUserId("asd"));
    }
}