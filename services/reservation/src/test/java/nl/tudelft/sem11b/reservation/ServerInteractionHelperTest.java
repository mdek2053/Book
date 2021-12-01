package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exception.UnauthorizedException;
import nl.tudelft.sem11b.reservation.services.HttpHelper;
import nl.tudelft.sem11b.reservation.services.ServerInteractionHelper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
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

        assertThrows(UnauthorizedException.class, () -> serverInteractionHelper.getUserId("asd"));
    }

    @Test
    void getOpeningHoursRoomOkay() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.body()).thenReturn(Constants.jsonResponse);

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertEquals(Lists.list("07:00", "20:00"), serverInteractionHelper.getOpeningHours(1));
    }

    @Test
    void getMaintenanceNoClosure() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.body()).thenReturn(Constants.jsonResponseNoClosure);

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertNull(serverInteractionHelper.getMaintenance(1));
    }

    @Test
    void getMaintenanceNullClosure() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.body()).thenReturn(Constants.jsonResponseNullClosure);

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertNull(serverInteractionHelper.getMaintenance(1));
    }

    @Test
    void getMaintenanceHasClosure() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.body()).thenReturn(Constants.jsonResponse);

        HttpHelper helper = mock(HttpHelper.class);
        when(helper.getResponse(any(), any(), any())).thenReturn(response);

        ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();
        serverInteractionHelper.setHelper(helper);

        assertEquals("Maintenance", serverInteractionHelper.getMaintenance(1));
    }
}