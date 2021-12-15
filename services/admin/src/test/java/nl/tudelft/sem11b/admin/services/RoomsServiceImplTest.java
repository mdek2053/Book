package nl.tudelft.sem11b.admin.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Set;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.FaultRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomsServiceImplTest {

    @Mock
    BuildingRepository buildings;

    @Mock
    RoomRepository rooms;

    @Mock
    FaultRepository faults;

    @Mock
    UserService users;

    RoomsServiceImpl service;

    @BeforeEach
    void initService() {
        service = new RoomsServiceImpl(buildings, rooms, faults, users);
    }

    @Test
    void closeRoomTestNotAdmin() throws Exception {
        // arrange
        UserModel simplyEmployee = new UserModel(1, "jjansen", new String[]{"employee"});
        when(users.currentUser()).thenReturn(simplyEmployee);

        ClosureModel closure = new ClosureModel("blue ball machine broke",
                new ApiDate(2021, 9, 1),
                new ApiDate(2021, 9, 13));

        // action + assert
        ApiException exception = assertThrows(ApiException.class,
                () -> service.closeRoom(1, closure));
        assertEquals(exception.getService(), "Rooms");
    }

    @Test
    void closeRoomTestNoRoom() throws Exception {
        // arrange
        UserModel admin = new UserModel(1, "jjansen", new String[]{"admin"});
        when(users.currentUser()).thenReturn(admin);

        ClosureModel closure = new ClosureModel("blue ball machine broke",
                new ApiDate(2021, 9, 1),
                new ApiDate(2021, 9, 13));

        when(rooms.existsById(1L)).thenReturn(false);

        // action + assert
        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> service.closeRoom(1, closure));
        assertEquals(exception.getEntityName(), "Room");
    }

    @Test
    void closeRoomTestSuccess() throws Exception {
        final var captor = ArgumentCaptor.forClass(Room.class);

        // arrange
        UserModel admin = new UserModel(1, "jjansen", new String[]{"admin"});
        when(users.currentUser()).thenReturn(admin);

        Room room = new Room(1L,"BOL", "Boole", 100,
                null,
                new Building(
                        1L, "EWI", "EEMCS building",
                        new ApiTime(8, 0), new ApiTime(22, 0),
                        Set.of()));
        when(rooms.existsById(1L)).thenReturn(true);
        when(rooms.getById(1L)).thenReturn(room);
        when(rooms.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        Closure closure = new Closure("blue ball machine broke",
                new ApiDate(2021, 9, 1),
                new ApiDate(2021, 9, 13));

        // action
        service.closeRoom(1, closure.toModel());

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(entity.getId(), room.getId());
        assertEquals(entity.getClosure(), closure);
    }

    @Test
    void reopenRoomTestNotAdmin() throws Exception {
        // arrange
        UserModel simplyEmployee = new UserModel(1, "jjansen", new String[]{"employee"});
        when(users.currentUser()).thenReturn(simplyEmployee);

        // action + assert
        ApiException exception = assertThrows(ApiException.class,
                () -> service.reopenRoom(1));
        assertEquals(exception.getService(), "Rooms");
    }

    @Test
    void reopenRoomTestNoRoom() throws Exception {
        // arrange
        UserModel admin = new UserModel(1, "jjansen", new String[]{"admin"});
        when(users.currentUser()).thenReturn(admin);

        when(rooms.existsById(1L)).thenReturn(false);

        // action + assert
        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> service.reopenRoom(1));
        assertEquals(exception.getEntityName(), "Room");
    }

    @Test
    void reopenRoomTestSuccess() throws Exception {
        final var captor = ArgumentCaptor.forClass(Room.class);

        // arrange
        UserModel admin = new UserModel(1, "jjansen", new String[]{"admin"});
        when(users.currentUser()).thenReturn(admin);

        Closure closure = new Closure("blue ball machine broke",
                new ApiDate(2021, 9, 1),
                new ApiDate(2021, 9, 13));

        Room room = new Room(1L,"BOL", "Boole", 100,
                closure,
                new Building(
                        1L, "EWI", "EEMCS building",
                        new ApiTime(8, 0), new ApiTime(22, 0),
                        Set.of()));
        when(rooms.existsById(1L)).thenReturn(true);
        when(rooms.getById(1L)).thenReturn(room);
        when(rooms.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.reopenRoom(1);

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(entity.getId(), room.getId());
        assertNull(entity.getClosure());
    }
}