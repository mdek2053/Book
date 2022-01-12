package nl.tudelft.sem11b.reservation;

import static nl.tudelft.sem11b.reservation.Constants.GROUPS;
import static nl.tudelft.sem11b.reservation.Constants.GROUP_A;
import static nl.tudelft.sem11b.reservation.Constants.GROUP_B;
import static nl.tudelft.sem11b.reservation.Constants.ROOM_A;
import static nl.tudelft.sem11b.reservation.Constants.USER_A;
import static nl.tudelft.sem11b.reservation.Constants.USER_B;
import static nl.tudelft.sem11b.reservation.Constants.USER_C;
import static nl.tudelft.sem11b.reservation.Constants.USER_D;
import static nl.tudelft.sem11b.reservation.Constants.groupModelList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiDateUtils;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.reservation.services.ReservationServiceImpl;
import nl.tudelft.sem11b.services.GroupService;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReservationServiceImplTest {
    private final ReservationModel reservationModel = new ReservationModel(
            ROOM_A.getId(),
            ApiDateUtils.at(ApiDateUtils.tomorrow(), 14, 0),
            ApiDateUtils.at(ApiDateUtils.tomorrow(), 18, 0),
            "Meeting"
    );

    private final ReservationRequestModel requestModel = new ReservationRequestModel(
            ROOM_A.getId(),
            "Meeting2",
            ApiDateUtils.at(ApiDateUtils.tomorrow(), 13, 30),
            ApiDateUtils.at(ApiDateUtils.tomorrow(), 15, 30),
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

    @MockBean
    GroupService groups;

    @BeforeEach
    void setup() {
        groupModelList = new ArrayList<>();
        List<Long> array = new ArrayList<>();
        array.add(USER_B.getId());
        GROUP_A.addToGroupMembers(array);
        GROUPS.add(GROUP_A);
        GROUPS.add(GROUP_B);
    }

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
                new ApiDateTime(ApiDateUtils.yesterday(), reservationModel.getSince().getTime()),
                new ApiDateTime(ApiDateUtils.yesterday(), reservationModel.getUntil().getTime())));
    }

    @Test
    void farFuture() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
                reservationModel.getRoomId(), reservationModel.getTitle(),
                new ApiDateTime(ApiDateUtils.after(ApiDateUtils.tomorrow(), 14),
                        reservationModel.getSince().getTime()),
                new ApiDateTime(ApiDateUtils.after(ApiDateUtils.tomorrow(), 14),
                        reservationModel.getUntil().getTime())));
    }

    @Test
    void reservationSpanningTwoDays() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
                reservationModel.getRoomId(), reservationModel.getTitle(),
                new ApiDateTime(ApiDateUtils.yesterday(),
                        reservationModel.getSince().getTime()),
                new ApiDateTime(ApiDateUtils.after(ApiDateUtils.yesterday()),
                        reservationModel.getSince().getTime())));
    }

    @Test
    void outsideHours() throws Exception {
        // arrange
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(users.currentUser()).thenReturn(USER_A);

        // action + assert
        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
                reservationModel.getRoomId(), reservationModel.getTitle(),
                new ApiDateTime(reservationModel.getSince().getDate(),
                        ApiTime.MINIMUM),
                new ApiDateTime(reservationModel.getUntil().getDate(),
                        ApiTime.MAXIMUM)));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
                reservationModel.getRoomId(), reservationModel.getTitle(),
                new ApiDateTime(reservationModel.getSince().getDate(),
                        ApiTime.MINIMUM),
                new ApiDateTime(reservationModel.getUntil().getDate(),
                        ROOM_A.getBuilding().getOpen())));

        assertThrows(InvalidData.class, () -> service.makeOwnReservation(
                reservationModel.getRoomId(), reservationModel.getTitle(),
                new ApiDateTime(reservationModel.getSince().getDate(),
                        ROOM_A.getBuilding().getClose()),
                new ApiDateTime(reservationModel.getUntil().getDate(),
                        ApiTime.MAXIMUM)));
    }

    @Test
    void roomUnderClosure() throws Exception {
        var closed =
                new RoomModel(ROOM_A.getId(), ROOM_A.getSuffix(), ROOM_A.getName(),
                        ROOM_A.getCapacity(), ROOM_A.getBuilding(),
                        ROOM_A.getEquipment().toArray(EquipmentModel[]::new),
                        new ClosureModel("Maintenance", ApiDateUtils.yesterday(),
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
    void secretaryReservation()
            throws ApiException, InvalidData, EntityNotFound, InvalidGroupCredentialsException {
        final var captor = ArgumentCaptor.forClass(Reservation.class);

        // arrange
        when(users.currentUser()).thenReturn(USER_A);
        when(groups.getGroupsOfSecretary(any())).thenReturn(GROUPS);
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(reservations.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.makeUserReservation(reservationModel.getRoomId(),
                USER_B.getId(), reservationModel.getTitle(),
                reservationModel.getSince(), reservationModel.getUntil());

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(reservationModel.getTitle(), entity.getTitle());
        assertEquals(reservationModel.getRoomId(), entity.getRoomId());
        assertEquals(reservationModel.getSince().toLocal(), entity.getSince().toLocalDateTime());
        assertEquals(reservationModel.getUntil().toLocal(), entity.getUntil().toLocalDateTime());
        assertEquals(USER_B.getId(), entity.getUserId());
    }

    @Test
    void adminReservation() throws InvalidData, ApiException,
            EntityNotFound, InvalidGroupCredentialsException {
        final var captor = ArgumentCaptor.forClass(Reservation.class);

        // arrange
        when(users.currentUser()).thenReturn(USER_D);
        when(groups.getGroupsOfSecretary(any())).thenReturn(GROUPS);
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(reservations.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.makeUserReservation(reservationModel.getRoomId(),
                USER_C.getId(), reservationModel.getTitle(),
                reservationModel.getSince(), reservationModel.getUntil());

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(reservationModel.getTitle(), entity.getTitle());
        assertEquals(reservationModel.getRoomId(), entity.getRoomId());
        assertEquals(reservationModel.getSince().toLocal(), entity.getSince().toLocalDateTime());
        assertEquals(reservationModel.getUntil().toLocal(), entity.getUntil().toLocalDateTime());
        assertEquals(USER_C.getId(), entity.getUserId());
    }

    @Test
    void invalidSecretaryReservationNoGroups() throws ApiException {
        when(users.currentUser()).thenReturn(USER_A);
        when(groups.getGroupsOfSecretary(anyLong())).thenReturn(new ArrayList<>());
        assertThrows(InvalidGroupCredentialsException.class, () -> service.makeUserReservation(
                reservationModel.getRoomId(), USER_B.getId(), reservationModel.getTitle(),
                reservationModel.getSince(), reservationModel.getUntil()));
    }

    @Test
    void invalidSecretaryReservationForMember() throws ApiException {
        when(users.currentUser()).thenReturn(USER_B);
        when(groups.getGroupsOfSecretary(anyLong())).thenReturn(GROUPS);
        assertThrows(InvalidGroupCredentialsException.class, () -> service.makeUserReservation(
                reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                reservationModel.getSince(), reservationModel.getUntil()));
        verify(reservations, never()).save(new Reservation(reservationModel.getRoomId(),
                USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal())));
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
        service.editReservation(reservation.getId(), reservationModel.getTitle() + "!",
                null, null);

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
    void invalidEditReservationWithBlankTitle() throws ApiException {
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
        assertThrows(InvalidData.class, () -> service.editReservation(
                reservation.getId(), " ", null, null));
    }

    @Test
    void testEditInvalidSecretaryReservation() throws ApiException {
        List<Long> groupUsers = new ArrayList<>();
        groupUsers.add(USER_B.getId());
        GROUP_A.setGroupMembers(groupUsers);
        groupModelList.add(GROUP_A);
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );
        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_A);
        when(groups.getGroupsOfSecretary(anyLong())).thenReturn(groupModelList);

        // action
        assertThrows(ApiException.class, () -> service.editReservation(reservation.getId(),
                reservation.getTitle(), ApiDateTime.from(reservation.getSince()),
                ApiDateTime.from(reservation.getUntil())));

        // verify
        verify(reservations, never()).delete(reservation);
    }

    @Test
    void testEditSecretaryReservation() throws ApiException, EntityNotFound, InvalidData {
        final var captor = ArgumentCaptor.forClass(Reservation.class);
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );

        List<Long> groupUsers = new ArrayList<>();
        groupUsers.add(USER_C.getId());
        GROUP_A.setGroupMembers(groupUsers);
        groupModelList.add(GROUP_A);

        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_A);
        when(groups.getGroupsOfSecretary(anyLong())).thenReturn(groupModelList);
        when(reservations.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.editReservation(reservation.getId(), reservation.getTitle() + "!!",
                null, null);

        // verify
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(reservationModel.getTitle() + "!!", entity.getTitle());
        assertEquals(reservationModel.getRoomId(), entity.getRoomId());
        assertEquals(reservationModel.getSince().toLocal(), entity.getSince().toLocalDateTime());
        assertEquals(reservationModel.getUntil().toLocal(), entity.getUntil().toLocalDateTime());
        assertEquals(USER_C.getId(), entity.getUserId());
    }

    @Test
    void editReservationAdmin() throws ApiException, InvalidData, EntityNotFound {
        final var captor = ArgumentCaptor.forClass(Reservation.class);
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );

        when(rooms.getRoom(ROOM_A.getId())).thenReturn(Optional.of(ROOM_A));
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_D);
        when(reservations.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.editReservation(reservation.getId(), "New title",
                null, null);

        // verify
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals("New title", entity.getTitle());
        assertEquals(reservationModel.getRoomId(), entity.getRoomId());
        assertEquals(reservationModel.getSince().toLocal(), entity.getSince().toLocalDateTime());
        assertEquals(reservationModel.getUntil().toLocal(), entity.getUntil().toLocalDateTime());
        assertEquals(USER_C.getId(), entity.getUserId());
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
        assertThrows(ApiException.class,
                () -> service.deleteReservation(reservation.getId()));
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
    void deleteReservationBySecretary() throws ApiException, EntityNotFound {
        List<Long> groupUsers = new ArrayList<>();
        groupUsers.add(USER_C.getId());
        GROUP_A.setGroupMembers(groupUsers);
        groupModelList.add(GROUP_A);
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_A);
        when(groups.getGroupsOfSecretary(anyLong())).thenReturn(groupModelList);

        // action
        service.deleteReservation(reservation.getId());

        // verify
        verify(reservations).delete(reservation);
    }

    @Test
    void deleteInvalidSecretaryReservation() throws ApiException {
        List<Long> groupUsers = new ArrayList<>();
        groupUsers.add(USER_B.getId());
        GROUP_A.setGroupMembers(groupUsers);
        groupModelList.add(GROUP_A);
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_A);
        when(groups.getGroupsOfSecretary(anyLong())).thenReturn(groupModelList);

        // action
        assertThrows(ApiException.class, () -> service.deleteReservation(reservation.getId()));

        // verify
        verify(reservations, never()).delete(reservation);
    }

    @Test
    void deleteReservationByAdmin() throws ApiException, EntityNotFound {
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_C.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );
        when(reservations.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(users.currentUser()).thenReturn(USER_D);

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
                        new ApiDateTime(ApiDateUtils.yesterday(),
                                requestModel.getSince().getTime()),
                        new ApiDateTime(ApiDateUtils.yesterday(),
                                requestModel.getUntil().getTime()),
                        requestModel.getForUser()
                )));
    }

    @Test
    void inspectOwnReservationsTest() throws ApiException {
        final var page = new PageIndex(0, 1);
        final var reservation = new Reservation(
                9, reservationModel.getRoomId(), USER_A.getId(), reservationModel.getTitle(),
                Timestamp.valueOf(reservationModel.getSince().toLocal()),
                Timestamp.valueOf(reservationModel.getUntil().toLocal()),
                null
        );
        when(users.currentUser()).thenReturn(USER_A);

        final var list = Arrays.asList(reservation);

        when(reservations.findByUserId(USER_A.getId(), page.getPage(Sort.by("id"))))
                .thenReturn(new PageImpl<>(list));

        var result = service.inspectOwnReservation(page);
        assertEquals(new PageData<>(1, list.stream().map(Reservation::toModel)
                .collect(Collectors.toList())), result);
    }
}