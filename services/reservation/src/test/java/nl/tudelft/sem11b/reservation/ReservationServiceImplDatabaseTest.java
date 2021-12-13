package nl.tudelft.sem11b.reservation;

import static nl.tudelft.sem11b.reservation.Constants.ROOM_A;
import static nl.tudelft.sem11b.reservation.Constants.ROOM_B;
import static nl.tudelft.sem11b.reservation.Constants.USER_A;
import static nl.tudelft.sem11b.reservation.Constants.USER_B;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.reservation.services.ReservationServiceImpl;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ReservationServiceImplDatabaseTest {
    @Autowired
    ReservationServiceImpl service;

    @MockBean
    RoomsService rooms;

    @MockBean
    UserService users;

    // Checks if the SQL for checking conflicts is correct
    @Test
    void makeReservationTestUserConflicts() throws Exception {
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(rooms.getRoom(ROOM_B.getId())).thenReturn(Optional.of(ROOM_B));
        when(users.currentUser()).thenReturn(USER_A);

        // one reservation between 13:30 and 14:00
        service.makeOwnReservation(ROOM_A.getId(), "Reservation # 1",
            ApiDate.tomorrow().at(13, 30), ApiDate.tomorrow().at(14, 0));

        // another between 14:00 and 15:00, no conflict
        service.makeOwnReservation(ROOM_B.getId(), "Reservation # 2",
            ApiDate.tomorrow().at(14, 0), ApiDate.tomorrow().at(15, 0));

        // another between 14:59 and 15:15, this one conflicts with reservation #2
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(ROOM_B.getId(),
            "Conflict", ApiDate.tomorrow().at(14, 59), ApiDate.tomorrow().at(15, 15)));

        // between 14:00 and 15:00, should conflict with #2 again
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(ROOM_A.getId(),
            "Conflict", ApiDate.tomorrow().at(14, 0), ApiDate.tomorrow().at(15, 0)));
    }

    @Test
    void makeReservationTestRoomConflicts() throws Exception {
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(rooms.getRoom(ROOM_B.getId())).thenReturn(Optional.of(ROOM_B));

        // arrange
        when(users.currentUser()).thenReturn(USER_A);
        service.makeOwnReservation(ROOM_A.getId(), "Reservation # 1",
            ApiDate.tomorrow().at(13, 0), ApiDate.tomorrow().at(15, 0));

        when(users.currentUser()).thenReturn(USER_B);
        service.makeOwnReservation(ROOM_B.getId(), "Reservation # 2",
            ApiDate.tomorrow().at(14, 0), ApiDate.tomorrow().at(16, 0));


        // action + assert
        when(users.currentUser()).thenReturn(USER_A);
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(ROOM_B.getId(),
            "Conflict B # 1", ApiDate.tomorrow().at(15, 0), ApiDate.tomorrow().at(16, 0)));

        when(users.currentUser()).thenReturn(USER_B);
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(ROOM_A.getId(),
            "Conflict B # 1", ApiDate.tomorrow().at(13, 30), ApiDate.tomorrow().at(14, 30)));
    }

}