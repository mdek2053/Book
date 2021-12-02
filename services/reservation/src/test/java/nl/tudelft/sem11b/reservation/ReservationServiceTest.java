package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.exception.ForbiddenException;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.reservation.services.ReservationService;
import nl.tudelft.sem11b.reservation.services.ServerInteractionHelper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    void makeReservationTestChecksRoom() throws Exception {
        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);

        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "",
                "2022-01-15T13:00", "2022-01-15T17:00"));

        verify(helper, times(1)).checkRoomExists(1);
    }
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
        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
        reservationService.setServ(helper);

        when(reservationRepository.saveAndFlush(any())).thenReturn(new Reservation());

        Timestamp since = new Timestamp(1642248000000L);
        Timestamp until = new Timestamp(1642262400000L);

        reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T13:00", "2022-01-15T17:00");

        verify(reservationRepository, times(1)).saveAndFlush(new Reservation(1, 1, "Title",
                since, until));
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

    @Test
    void makeReservationTestDateTwoDays() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T13:00", "2022-01-16T17:00"));
    }

    @Test
    void makeReservationTestDateOutsideHours() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
        reservationService.setServ(helper);

        when(reservationRepository.saveAndFlush(any())).thenReturn(new Reservation());

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T06:00", "2022-01-15T06:30"));

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T06:00", "2022-01-15T10:30"));

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T19:30", "2022-01-15T20:30"));

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T21:30", "2022-01-15T23:30"));

        assertDoesNotThrow(() -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T16:30", "2022-01-15T16:45"));
    }

    @Test
    void makeReservationTestMaintenance() throws Exception {
        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
        reservationService.setClock(clock);

        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
        when(helper.checkRoomExists(anyLong())).thenReturn(true);
        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
        when(helper.getMaintenance(anyLong())).thenReturn("Some bad reason");
        reservationService.setServ(helper);

        assertThrows(ForbiddenException.class, () -> reservationService.makeReservation(1, 1, "Title",
                "2022-01-15T16:30", "2022-01-15T16:45"));
    }

}