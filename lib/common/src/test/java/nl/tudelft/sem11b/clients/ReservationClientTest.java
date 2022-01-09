package nl.tudelft.sem11b.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.ApiResponse;
import nl.tudelft.sem11b.http.Authenticated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ReservationClientTest {

    @Mock
    ApiClient<Authenticated> api;

    @InjectMocks
    ReservationClient client;

    private final transient ApiDateTime since =
            new ApiDateTime(2022L, 1L, 1L, 10L, 0L);
    private final transient ApiDateTime until =
            new ApiDateTime(2022L, 1L, 1L, 11L, 0L);
    private final transient ReservationRequestModel model =
            new ReservationRequestModel(1L, "Meeting", since, until, null);
    private final transient ReservationRequestModel model2 =
            new ReservationRequestModel(1L, "Meeting", since, until, 1L);


    @Test
    void testMakeOwnReservationEmptyRoom() {
        when(api.post(eq("/reservations"), eq(model),
                any(TypeReference.class), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("reservations", null));

        assertThrows(EntityNotFound.class, () ->
                client.makeOwnReservation(1L, "Meeting", since, until));
    }

    @Test
    void testMakeOwnReservationSuccessful() throws EntityNotFound, ApiException {
        when(api.post(eq("/reservations"), eq(model),
                any(TypeReference.class), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("reservations", new IdModel(1L)));

        assertEquals(1L,
                client.makeOwnReservation(1L, "Meeting", since, until));
    }

    @Test
    void testMakeUserReservationEmptyRoom() {
        when(api.post(eq("/reservations"), eq(model2),
                any(TypeReference.class), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("reservations", null));

        assertThrows(EntityNotFound.class, () ->
                client.makeUserReservation(1L, 1L, "Meeting", since, until));
    }

    @Test
    void testMakeUserReservationSuccessful() throws EntityNotFound, ApiException {
        when(api.post(eq("/reservations"), eq(model2),
                any(TypeReference.class), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("reservations", new IdModel(1L)));

        assertEquals(1L,
                client.makeUserReservation(1L, 1L, "Meeting", since, until));
    }
}
