package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exception.ForbiddenException;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.reservation.services.ReservationService;
import nl.tudelft.sem11b.reservation.services.ServerInteractionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {
    @Autowired
    ReservationService reservationService;

    @MockBean
    ReservationRepository reservationRepository;

    @Test
    void makeReservationTestInvalidTitle() throws Exception {
        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);

        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "",
                "2022-01-15T13:00", "2022-01-15T17:00"));
    }

    @Test
    void makeReservationTestInvalidDate() throws Exception {
        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);

        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "badDate", "anotherBadDate"));
    }

    @Test
    void makeReservationTestDateParsing() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC")); // same day about 11ish
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        reservationService.setServ(helper);

        Timestamp since = new Timestamp(1642248000000L);
        Timestamp until = new Timestamp(1642262400000L);

        reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T13:00", "2022-01-15T17:00");

        verify(reservationRepository, times(1)).makeReservation(1, 1, "Title",
                since, until, null);
    }

    @Test
    void makeReservationTestDatePast() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642262800000L), ZoneId.of("UTC")); // same day after 17
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T13:00", "2022-01-15T17:00"));
    }

    @Test
    void makeReservationTestDateFarFuture() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-29T13:00", "2022-01-29T17:00"));
    }

}