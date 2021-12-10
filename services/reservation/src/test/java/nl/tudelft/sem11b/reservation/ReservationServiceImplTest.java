package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.reservation.services.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReservationServiceImplTest {
    @Autowired
    ReservationServiceImpl reservationServiceImpl;

    @MockBean
    ReservationRepository reservationRepository;

    // TODO: Reimplement

    @Test
    void makeReservationTestChecksRoom() throws Exception {
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "","2022-01-15T13:00", "2022-01-15T17:00"));
//
//        verify(helper, times(1)).checkRoomExists(1);
    }

    @Test
    void makeReservationTestInvalidTitle() throws Exception {
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "","2022-01-15T13:00", "2022-01-15T17:00"));
    }

    @Test
    void makeReservationTestInvalidDate() throws Exception {
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","badDate", "anotherBadDate"));
    }

    @Test
    void makeReservationTestDateParsing() throws Exception {
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L),
//                ZoneId.of("UTC")); // same day about 10ish
//        reservationServiceImpl.setClock(clock);
//
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
//        reservationServiceImpl.setServ(helper);
//
//        when(reservationRepository.saveAndFlush(any())).thenReturn(new Reservation());
//
//        long timeAtLocal = System.currentTimeMillis();
//        long offset = TimeZone.getDefault().getOffset(timeAtLocal);
//
//        Timestamp since = new Timestamp(1642248000000L - offset);
//        Timestamp until = new Timestamp(1642262400000L - offset);
//
//        reservationServiceImpl.makeReservation(1, 1, "Title",
//                "2022-01-15T12:00", "2022-01-15T16:00");
//
//        verify(reservationRepository, times(1)).saveAndFlush(new Reservation(1, 1, "Title",
//                since, until));
    }

    @Test
    void makeReservationTestDatePast() throws Exception {
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642262800000L),
//                ZoneId.of("UTC")); // same day after 16
//        reservationServiceImpl.setClock(clock);
//
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T13:00", "2022-01-15T17:00"));
    }

    @Test
    void makeReservationTestDateFarFuture() throws Exception {
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L),
//                ZoneId.of("UTC")); // 15 jan 10ish
//        reservationServiceImpl.setClock(clock);
//
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-29T13:00", "2022-01-29T17:00"));
    }

    @Test
    void makeReservationTestDateTwoDays() throws Exception {
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L),
//                ZoneId.of("UTC")); // 15 jan 10ish
//        reservationServiceImpl.setClock(clock);
//
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T13:00", "2022-01-16T17:00"));
    }

    @Test
    void makeReservationTestDateOutsideHours() throws Exception {
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L),
//                ZoneId.of("UTC")); // 15 jan 10ish
//        reservationServiceImpl.setClock(clock);
//
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
//        reservationServiceImpl.setServ(helper);
//
//        when(reservationRepository.saveAndFlush(any())).thenReturn(new Reservation());
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T06:00", "2022-01-15T06:30"));
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T06:00", "2022-01-15T10:30"));
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T19:30", "2022-01-15T20:30"));
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T21:30", "2022-01-15T23:30"));
//
//        assertDoesNotThrow(() -> reservationServiceImpl.makeReservation(1, 1, "Title",
//                "2022-01-15T16:30", "2022-01-15T16:45"));
    }

    @Test
    void makeReservationTestMaintenance() throws Exception {
//        Clock clock = Clock.fixed(Instant.ofEpochMilli(1642242000000L), ZoneId.of("UTC"));
//        reservationServiceImpl.setClock(clock);
//
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        when(helper.checkRoomExists(anyLong())).thenReturn(true);
//        when(helper.getOpeningHours(anyLong())).thenReturn(Lists.list("07:00", "20:00"));
//        when(helper.getMaintenance(anyLong())).thenReturn("Some bad reason");
//        reservationServiceImpl.setServ(helper);
//
//        assertThrows(ForbiddenException.class, () -> reservationServiceImpl.makeReservation(1, 1,
//                "Title","2022-01-15T16:30", "2022-01-15T16:45"));
    }

    @Test
    void changeNonExistingReservation() throws Exception {
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//        String token = "token123";
//        Long reservationId = 123L;
//        when(helper.getUserId(token)).thenReturn(1L);
//        when(helper.getUserRole(token)).thenReturn("admin");
//        when(reservationRepository.findReservationById(reservationId)).thenReturn(Optional.empty());
//        reservationServiceImpl.setServ(helper);
//        Long roomId = 111L;
//        String title = "Meeting";
//        String since = "2022-01-15T13:00";
//        String until = "2022-01-15T17:00";
//        ReservationModel model =
//                new ReservationModel(roomId, since, until, title);
//        assertThrows(NotFoundException.class, () ->
//                reservationServiceImpl.editReservation(token, model, reservationId));
    }

    @Test
    void successfullyEditRoomAndTitle() throws Exception {
//        ServerInteractionHelper helper = mock(ServerInteractionHelper.class);
//
//        String token = "token123";
//        String role = "admin";
//        Long roomId = 111L;
//        Long userId = 444L;
//
//        when(helper.getUserId(token)).thenReturn(userId);
//        when(helper.getUserRole(token)).thenReturn(role);
//        when(helper.checkRoomExists(roomId)).thenReturn(true);
//        when(helper.getOpeningHours(roomId)).thenReturn(Lists.list("07:00", "20:00"));
//        reservationServiceImpl.setServ(helper);
//
//        long timeAtLocal = System.currentTimeMillis();
//        long offset = TimeZone.getDefault().getOffset(timeAtLocal);
//
//        Timestamp since = new Timestamp(1642248000000L - offset);
//        Timestamp until = new Timestamp(1642262400000L - offset);
//        String title = "Meeting";
//        Long reservationId = 123L;
//        Reservation reservation =
//                new Reservation(reservationId, roomId - 1,
//                        userId, title, since, until, null);
//        when(reservationRepository.findReservationById(reservationId))
//                .thenReturn(Optional.of(reservation));
//        List<Reservation> conflictUser = new ArrayList<>();
//        conflictUser.add(reservation);
//        when(reservationRepository.hasUserConflict(userId, since, until)).thenReturn(conflictUser);
//        when(reservationRepository.hasRoomConflict(roomId, since, until)).thenReturn(null);
//
//        Reservation changedReservation = new Reservation(reservationId, roomId, userId,
//                "New title", since, until, null);
//        when(reservationRepository.save(changedReservation))
//                .thenReturn(changedReservation);
//
//        ReservationModel model = new ReservationModel(roomId, null, null, "New title");
//        Assertions.assertEquals(reservationId,
//                reservationServiceImpl.editReservation(token, model, reservationId));
//        verify(reservationRepository, times(1)).save(changedReservation);
    }
}