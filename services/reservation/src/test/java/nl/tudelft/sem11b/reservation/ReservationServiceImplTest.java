package nl.tudelft.sem11b.reservation;

import static nl.tudelft.sem11b.reservation.Constants.ROOM_A;
import static nl.tudelft.sem11b.reservation.Constants.USER_A;
import static nl.tudelft.sem11b.reservation.Constants.USER_B;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Optional;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.reservation.services.ReservationServiceImpl;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReservationServiceImplTest {
    private final ReservationModel reservationModel = new ReservationModel(
        ROOM_A.getId(),
        ApiDate.tomorrow().at(new ApiTime(14, 0)),
        ApiDate.tomorrow().at(new ApiTime(18, 0)),
        "Meeting"
    );

    private final ReservationRequestModel requestModel = new ReservationRequestModel(
            ROOM_A.getId(),
            "Meeting2",
            ApiDate.tomorrow().at(new ApiTime(13, 30)),
            ApiDate.tomorrow().at(new ApiTime(15, 30)),
            USER_A.getId()
    );

    @Autowired
    ReservationServiceImpl service;

    @MockBean
    ReservationRepository reservations;

    @MockBean
    RoomsService rooms;

    @MockBean
    UserService users;

    @Test
    void nonexistentRoom() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.empty());
        when(users.currentUser()).thenReturn(USER_A);

        // action
        assertThrows(EntityNotFound.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getSince(), reservationModel.getUntil()));

        // assert
        verify(rooms, atLeastOnce()).getRoom(ROOM_A.getId());
    }

    @Test
    void invalidTitle() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), null,
            reservationModel.getSince(), reservationModel.getUntil()));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), "",
            reservationModel.getSince(), reservationModel.getUntil()));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), " ",
            reservationModel.getSince(), reservationModel.getUntil()));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), "\t",
            reservationModel.getSince(), reservationModel.getUntil()));
    }

    @Test
    void flippedTimestamps() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getUntil(), reservationModel.getSince()));
    }

    @Test
    void pastDate() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            ApiDate.yesterday().at(reservationModel.getSince().getTime()),
            ApiDate.yesterday().at(reservationModel.getUntil().getTime())));
    }

    @Test
    void farFuture() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            ApiDate.tomorrow().after(14).at(reservationModel.getSince().getTime()),
            ApiDate.tomorrow().after(14).at(reservationModel.getUntil().getTime())));
    }

    @Test
    void reservationSpanningTwoDays() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            ApiDate.tomorrow().at(reservationModel.getSince().getTime()),
            ApiDate.tomorrow().after().at(reservationModel.getUntil().getTime())));
    }

    @Test
    void outsideHours() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getSince().getDate().at(ApiTime.MINIMUM),
            reservationModel.getUntil().getDate().at(ApiTime.MAXIMUM)));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getSince().getDate().at(ApiTime.MINIMUM),
            reservationModel.getUntil().getDate().at(ROOM_A.getBuilding().getOpen())));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getSince().getDate().at(ROOM_A.getBuilding().getClose()),
            reservationModel.getUntil().getDate().at(ApiTime.MAXIMUM)));
    }

    @Test
    void roomUnderClosure() throws Exception {
        var closed =
            new RoomModel(ROOM_A.getId(), ROOM_A.getSuffix(), ROOM_A.getName(),
                ROOM_A.getCapacity(), ROOM_A.getBuilding(),
                ROOM_A.getEquipment().toArray(EquipmentModel[]::new),
                new ClosureModel("Maintenance", ApiDate.yesterday(),
                    reservationModel.getSince().getDate()));

        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(closed));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
            reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getSince(), reservationModel.getUntil()));
    }

    @Test
    void editNonexistentReservation() throws Exception {
        final var id = 2L;

        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);
        when(reservations.findById(id)).thenReturn(Optional.empty());

        // action + assert
        assertThrows(EntityNotFound.class, () -> service.editReservation(
            id, reservationModel.getTitle(),
            reservationModel.getSince(), reservationModel.getUntil()));
    }

    @Test
    void insertTest() throws Exception {
        final var captor = ArgumentCaptor.forClass(Reservation.class);

        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);
        when(reservations.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.makeOwnReservation(reservationModel.getRoomId(), reservationModel.getTitle(),
            reservationModel.getSince(), reservationModel.getUntil());

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(reservationModel.getTitle(), entity.getTitle());
        assertEquals(reservationModel.getRoomId(), entity.getRoomId());
        assertEquals(reservationModel.getSince().toLocal(), entity.getSince().toLocalDateTime());
        assertEquals(reservationModel.getUntil().toLocal(), entity.getUntil().toLocalDateTime());
        assertEquals(USER_A.getId(), entity.getUserId());
    }

    @Test
    void updateTest() throws Exception {
        final var captor = ArgumentCaptor.forClass(Reservation.class);
        final var reservation = new Reservation(
            8, reservationModel.getRoomId(), USER_A.getId(), reservationModel.getTitle(),
            Timestamp.valueOf(reservationModel.getSince().toLocal()),
            Timestamp.valueOf(reservationModel.getUntil().toLocal()),
            null
        );

        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservations.save(captor.capture())).thenAnswer(i -> i.getArgument(0));


        // action
        service.editReservation(reservation.getId(), reservationModel.getTitle() + "!", null, null);

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(reservationModel.getTitle() + "!", entity.getTitle());
        assertEquals(reservationModel.getRoomId(), entity.getRoomId());
        assertEquals(reservationModel.getSince().toLocal(), entity.getSince().toLocalDateTime());
        assertEquals(reservationModel.getUntil().toLocal(), entity.getUntil().toLocalDateTime());
        assertEquals(USER_A.getId(), entity.getUserId());
    }

    @Test
    void deleteNonExistentReservationTest() throws ApiException {
        final var id = 2L;

        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);
        when(reservations.findById(id)).thenReturn(Optional.empty());

        // action + assert
        assertThrows(EntityNotFound.class, () -> service.deleteReservation(id));
    }

    @Test
    void deleteReservationWithoutPermissions() throws ApiException {
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_A.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );

        // arrange
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_B);

        // action + assert
        assertThrows(ApiException.class, () -> service.deleteReservation(reservation.getId()));
    }

    @Test
    void deleteReservationSuccessfully() throws ApiException, EntityNotFound {
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_A.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );

        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_A);

        // action
        service.deleteReservation(reservation.getId());

        // verify
        verify(reservations).delete(reservation);
    }

    @Test
    void checkValidAvailability() throws ApiException {
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);
        assertTrue(service.checkAvailability(ROOM_A.getId(), requestModel));
    }

    @Test
    void checkInvalidTimeAvailability() throws ApiException {
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);
        assertThrows(ResponseStatusException.class, () -> service.checkAvailability(ROOM_A.getId(),
                new ReservationRequestModel(requestModel.getRoomId(), requestModel.getTitle(),
                        ApiDate.yesterday().at(requestModel.getSince().getTime()),
                        ApiDate.yesterday().at(requestModel.getUntil().getTime()),
                        requestModel.getForUser()
                )));
    }
}