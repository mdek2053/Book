package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exception.ForbiddenException;
import nl.tudelft.sem11b.reservation.services.ReservationService;
import nl.tudelft.sem11b.reservation.services.ServerInteractionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
class ReservationServiceDBTest {
    @Autowired
    ReservationService reservationService;

    @Test
    void makeReservationTestConflicts() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        reservationService.setServ(helper);

        reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T13:00", "2022-01-15T14:00");

        reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T14:00", "2022-01-15T15:00");

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T14:59", "2022-01-15T15:15"));

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T14:00", "2022-01-15T15:00"));

        //System.out.println(reservationService.getAll());
    }

}