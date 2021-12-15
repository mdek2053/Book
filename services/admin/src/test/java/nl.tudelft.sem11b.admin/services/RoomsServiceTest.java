package nl.tudelft.sem11b.admin.services;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class RoomsServiceTest {

    @Mock
    RoomRepository roomRepository;

    @Mock
    BuildingRepository buildingRepository;

    @InjectMocks
    RoomsServiceImpl service;

    private final Building building1 = new Building(1, "idk", "drebbelweg", null, null, new HashSet<>());
    private final Building building2 = new Building(3, "idk", "EWI", null, null, new HashSet<>());

    private final Room room1 = new Room(1, "idk", "PC hall 1", 30, null, building1);
    private final Room room2 = new Room(2,  "idk", "PC hall 2", 50, null, building1);
    private final Room room3 = new Room(3,  "idk", "Boole", 50, null, building2);


    private final RoomStudModel room1StudModel = room1.toStudModel();
    private final RoomStudModel room2StudModel = room2.toStudModel();
    private final RoomStudModel room3StudModel = room3.toStudModel();

    @BeforeEach
    private void setup(){
        building1.addRoom(room1);
        building1.addRoom(room2);
        building2.addRoom(room3);
    }

    @Test
    public void searchRoomsNoFilterTest() {
        List<RoomStudModel> models = List.of(room1StudModel);
        PageData<RoomStudModel> expected = new PageData<RoomStudModel>(1, models);
        Map<String, Object> filters = new HashMap<String, Object>();
        PageIndex index = new PageIndex(0, 10);

        when(roomRepository.findAll(index.getPage(Sort.by("id")))).thenReturn(new PageImpl<Room>(List.of(room1)));

        try {
            assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchRoomsInvalidCapacityFilterTest() {
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("capacity", "String");

        PageIndex index = new PageIndex(0, 10);

        assertThrows(InvalidFilterException.class, () -> {
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

        when(roomRepository.findAll(index.getPage(Sort.by("id")))).thenReturn(new PageImpl<>(List.of(room1, room2)));

        try {
            assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchRoomsInvalidBuildingFilterTest() {
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("building", "String");

        PageIndex index = new PageIndex(0, 10);

        assertThrows(InvalidFilterException.class, () -> {
            service.searchRooms(index, filters);
        });
    }

    @Test
    public void searchRoomsNonExistentBuildingFilterTest() {
        Map<String, Object> filters = new HashMap<String, Object>();
        filters.put("building", 3L);

        when(buildingRepository.existsById(3L)).thenReturn(false);

        PageIndex index = new PageIndex(0, 10);

        assertThrows(EntityNotFound.class, () -> {
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

        when(buildingRepository.existsById(1L)).thenReturn(true);
        when(buildingRepository.getById(1L)).thenReturn(building1);

        when(roomRepository.findAll(index.getPage(Sort.by("id")))).thenReturn(new PageImpl<>(List.of(room1, room2, room3)));

        try {
            assertEquals(expected, service.searchRooms(index, filters));
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

        when(buildingRepository.existsById(1L)).thenReturn(true);
        when(buildingRepository.getById(1L)).thenReturn(building1);

        when(roomRepository.findAll(index.getPage(Sort.by("id")))).thenReturn(new PageImpl<>(List.of(room1, room2, room3)));

        try {
            assertEquals(expected, service.searchRooms(index, filters));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
