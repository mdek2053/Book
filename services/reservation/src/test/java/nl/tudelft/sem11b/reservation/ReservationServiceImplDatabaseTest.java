package nl.tudelft.sem11b.reservation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.reservation.services.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
class ReservationServiceImplDatabaseTest {
    @Autowired
    ReservationServiceImpl reservationServiceImpl;

    // Checks if the SQL for checking conflicts is correct
    @Test
    void makeReservationTestUserConflicts() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        // 15 Jan '22 10:20 so reservations aren't too much in the future
        reservationServiceImpl.setClock(clock);

        // TODO: Uncomment once API clients are in place
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
//        reservationServiceImpl.setServ(helper);

        // one reservation between 13:30 and 14:00
        reservationServiceImpl.makeReservation(1, 1, "Title",
            new ApiDateTime(2022, 1, 15, 13, 0), new ApiDateTime(2022, 1, 15, 14, 0));

        // another between 14:00 and 15:00, no conflict
        reservationServiceImpl.makeReservation(1, 1, "Title",
            new ApiDateTime(2022, 1, 15, 14, 0), new ApiDateTime(2022, 1, 15, 15, 0));

        // another between 14:59 and 15:15, this one conflicts with reservation #2
        assertThrows(InvalidData.class, () -> reservationServiceImpl.makeReservation(1, 1,
            "Title", new ApiDateTime(2022, 1, 15, 14, 59), new ApiDateTime(2022, 1, 15, 15, 15)));

        // between 14:00 and 15:00, should conflict with #2 again
        assertThrows(InvalidData.class, () -> reservationServiceImpl.makeReservation(1, 1,
            "Title", new ApiDateTime(2022, 1, 15, 14, 0), new ApiDateTime(2022, 1, 15, 15, 0)));

        //System.out.println(reservationService.getAll());
    }

    @Test
    void makeReservationTestRoomConflicts() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        // 15 Jan '22 10:20 so reservations aren't too much in the future
        reservationServiceImpl.setClock(clock);

        // TODO: Uncomment once API clients are in place
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
//        reservationServiceImpl.setServ(helper);

        // one reservation between 13:30 and 14:00
        reservationServiceImpl.makeReservation(3, 2, "Title",
            new ApiDateTime(2022, 1, 15, 13, 0), new ApiDateTime(2022, 1, 15, 14, 0));

        // another between 14:00 and 15:00, another room
        reservationServiceImpl.makeReservation(4, 3, "Title",
            new ApiDateTime(2022, 1, 15, 14, 0), new ApiDateTime(2022, 1, 15, 15, 0));

        // one between 14:59 and 15:15 in room 3 no conflict
        reservationServiceImpl.makeReservation(3, 4, "Title",
            new ApiDateTime(2022, 1, 15, 14, 59), new ApiDateTime(2022, 1, 15, 15, 15));

        // between 15:15 and 15:30 in room 3, no conflict
        reservationServiceImpl.makeReservation(3, 5, "Title",
            new ApiDateTime(2022, 1, 15, 15, 15), new ApiDateTime(2022, 1, 15, 15, 30));

        // between 15:29 and 15:30 in room 3, conflict
        assertThrows(InvalidData.class, () -> reservationServiceImpl.makeReservation(3, 6,
            "Title", new ApiDateTime(2022, 1, 15, 15, 29), new ApiDateTime(2022, 1, 15, 15, 30)));

        // between 14:15 and 15:00 in room 4, should conflict
        assertThrows(InvalidData.class, () -> reservationServiceImpl.makeReservation(4, 7,
            "Title", new ApiDateTime(2022, 1, 15, 14, 15), new ApiDateTime(2022, 1, 15, 15, 0)));
    }

}