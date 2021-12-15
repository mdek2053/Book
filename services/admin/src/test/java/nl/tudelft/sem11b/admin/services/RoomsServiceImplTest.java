package nl.tudelft.sem11b.admin.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class RoomsServiceImplTest {

    @Mock
    BuildingRepository buildings;

    @Mock
    RoomRepository rooms;

    @Mock
    UserService users;

    RoomsServiceImpl service;

    private final Building building1 =
            new Building(1, "idk", "drebbelweg", null, null, new HashSet<>());
    private final Building building2 =
            new Building(3, "idk", "EWI", null, null, new HashSet<>());

    private final Room room1 = new Room(1, "idk", "PC hall 1", 30, null, building1);
    private final Room room2 = new Room(2,  "idk", "PC hall 2", 50, null, building1);
    private final Room room3 = new Room(3,  "idk", "Boole", 50, null, building2);


    private final RoomStudModel room1StudModel = room1.toStudModel();
    private final RoomStudModel room2StudModel = room2.toStudModel();
    private final RoomStudModel room3StudModel = room3.toStudModel();

    @BeforeEach
    private void setup() {
        building1.addRoom(room1);
        building1.addRoom(room2);
        building2.addRoom(room3);
    }

    @BeforeEach
    void initService() {
        service = new RoomsServiceImpl(buildings, rooms, users);
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

    @Test
    public void searchRoomsNoFilterTest() {
        List<RoomStudModel> models = List.of(room1StudModel);
        PageData<RoomStudModel> expected = new PageData<RoomStudModel>(1, models);
        Map<String, Object> filters = new HashMap<String, Object>();
        PageIndex index = new PageIndex(0, 10);

        when(rooms.findAll(index.getPage(Sort.by("id"))))
                .thenReturn(new PageImpl<Room>(List.of(room1)));

        try {
            Assert.assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchRoomsInvalidCapacityFilterTest() {
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("capacity", "String");

        PageIndex index = new PageIndex(0, 10);

        Assert.assertThrows(InvalidFilterException.class, () -> {
            service.searchRooms(index, filters);
        });
    }

    @Test
    public void searchRoomsOnlyCapacityFilterTest() {
        List<RoomStudModel> models = List.of(room2StudModel);
        PageData<RoomStudModel> expected = new PageData<>(1, models);
        Map<String, Object> filters = new HashMap<>();
        filters.put("capacity", 40);

        PageIndex index = new PageIndex(0, 10);

        when(rooms.findAll(index.getPage(Sort.by("id"))))
                .thenReturn(new PageImpl<>(List.of(room1, room2)));

        try {
            Assert.assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchRoomsInvalidBuildingFilterTest() {
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("building", "String");

        PageIndex index = new PageIndex(0, 10);

        Assert.assertThrows(InvalidFilterException.class, () -> {
            service.searchRooms(index, filters);
        });
    }

    @Test
    public void searchRoomsNonExistentBuildingFilterTest() {
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("building", 3L);

        when(buildings.existsById(3L)).thenReturn(false);

        PageIndex index = new PageIndex(0, 10);

        Assert.assertThrows(EntityNotFound.class, () -> {
            service.searchRooms(index, filters);
        });
    }

    @Test
    public void searchRoomsBuildingFilterTest() {
        List<RoomStudModel> models = List.of(room1StudModel, room2StudModel);
        PageData<RoomStudModel> expected = new PageData<>(2, models);
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("building", 1L);

        PageIndex index = new PageIndex(0, 10);

        when(buildings.existsById(1L)).thenReturn(true);
        when(buildings.getById(1L)).thenReturn(building1);

        when(rooms.findAll(index.getPage(Sort.by("id"))))
                .thenReturn(new PageImpl<>(List.of(room1, room2, room3)));

        try {
            Assert.assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchRoomsTwoFiltersTest() {
        List<RoomStudModel> models = List.of(room2StudModel);
        PageData<RoomStudModel> expected = new PageData<>(1, models);
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("capacity", 40);
        filters.put("building", 1L);

        PageIndex index = new PageIndex(0, 10);

        when(buildings.existsById(1L)).thenReturn(true);
        when(buildings.getById(1L)).thenReturn(building1);

        when(rooms.findAll(index.getPage(Sort.by("id"))))
                .thenReturn(new PageImpl<>(List.of(room1, room2, room3)));

        try {
            Assert.assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}