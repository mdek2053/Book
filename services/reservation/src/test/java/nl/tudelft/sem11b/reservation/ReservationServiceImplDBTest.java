package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.reservation.services.ReservationServiceImpl;
import nl.tudelft.sem11b.reservation.services.ServerInteractionHelper;
import org.assertj.core.util.Lists;
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
class ReservationServiceImplDBTest {
    @Autowired
    ReservationServiceImpl reservationServiceImpl;

    // Checks if the SQL for checking conflicts is correct
    @Test
    void makeReservationTestConflicts() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
            // 15 Jan '22 10:20 so reservations aren't too much in the future
        reservationServiceImpl.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
        reservationServiceImpl.setServ(helper);

        // one reservation between 13:30 and 14:00
        reservationServiceImpl.makeReservation(1, 1, "Title",
                "2022-01-15T13:00", "2022-01-15T14:00");

        // another between 14:00 and 15:00, no conflict
        reservationServiceImpl.makeReservation(1, 1, "Title",
                "2022-01-15T14:00", "2022-01-15T15:00");

        // another between 14:59 and 15:15, this one conflicts with reservation #2
        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1, "Title",
                "2022-01-15T14:59", "2022-01-15T15:15"));

        // between 14:00 and 15:00, should conflict with #2 again
        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1, "Title",
                "2022-01-15T14:00", "2022-01-15T15:00"));

        //System.out.println(reservationService.getAll());
    }

}