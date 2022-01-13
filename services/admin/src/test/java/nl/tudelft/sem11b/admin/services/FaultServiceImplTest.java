package nl.tudelft.sem11b.admin.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Fault;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.FaultRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FaultServiceImplTest {
    @Mock
    RoomRepository rooms;

    @Mock
    FaultRepository faults;

    @Mock
    UserService users;

    FaultServiceImpl service;


    @BeforeEach
    void initService() {
        service = new FaultServiceImpl(rooms, faults, users);
    }

    @Test
    void addFaultTestNoRoom() {
        // arrange
        when(rooms.existsById(1L)).thenReturn(false);

        FaultRequestModel fault = new FaultRequestModel(1,
            "Blue ball machine broke");

        // action + assert
        EntityNotFound exception = assertThrows(EntityNotFound.class,
            () -> service.addFault(1, fault));
        assertEquals(exception.getEntityName(), "Room");
    }

    @Test
    void addFaultTestSuccess() throws Exception {
        final var captor = ArgumentCaptor.forClass(Fault.class);

        // arrange
        UserModel simplyEmployee = new UserModel(1, "jjansen", new String[]{});
        when(users.currentUser()).thenReturn(simplyEmployee);

        Room room = new Room(1L,"BOL", "Boole", 100,
            null,
            new Building(
                1L, "EWI", "EEMCS building",
                new ApiTime(8, 0), new ApiTime(22, 0),
                Set.of()), Set.of());
        when(rooms.existsById(1L)).thenReturn(true);
        when(rooms.getById(1L)).thenReturn(room);

        Fault fault = new Fault(1, "Blue ball machine broke",
            room);
        when(faults.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        // action
        service.addFault(1, fault.toRequestModel());

        // assert
        final var entity = captor.getValue();
        assertNotNull(entity);
        assertEquals(entity.getRoom(), room);
        assertEquals(entity.getReporter(), simplyEmployee.getId());
    }

    @Test
    void listFaultsTestNoRoom() {
        // arrange
        PageIndex page = new PageIndex(1, 5);

        when(rooms.existsById(1L)).thenReturn(false);

        // action + assert
        EntityNotFound exception = assertThrows(EntityNotFound.class,
            () -> service.listFaults(page, 1));
        assertEquals(exception.getEntityName(), "Room");
    }

    @Test
    void listFaultsTestSuccess() throws Exception {
        // arrange
        when(rooms.existsById(1L)).thenReturn(true);
        Room room = new Room(1L,"BOL", "Boole", 100,
            null,
            new Building(
                1L, "EWI", "EEMCS building",
                new ApiTime(8, 0), new ApiTime(22, 0),
                Set.of()), Set.of());

        Fault faultA = new Fault(1L, "A", room);
        Fault faultB = new Fault(2L, "B", room);
        Page<Fault> pageObj = new PageImpl<>(List.of(faultA, faultB));
        when(faults.findAllByRoomId(anyLong(), any())).thenReturn(pageObj);

        PageIndex page = new PageIndex(1, 5);

        // action
        var faultsData = service.listFaults(page, 1L);

        // assert
        assertEquals(faultsData.getData().count(), 2);
    }

    @Test
    void listFaultsAllTestSuccess() {
        // arrange
        Room room = new Room(1L,"BOL", "Boole", 100,
            null,
            new Building(
                1L, "EWI", "EEMCS building",
                new ApiTime(8, 0), new ApiTime(22, 0),
                Set.of()), Set.of());

        Fault faultA = new Fault(1L, "A", room);
        Fault faultB = new Fault(2L, "B", room);
        Page<Fault> pageObj = new PageImpl<>(List.of(faultA, faultB));
        when(faults.findAll((Pageable) any())).thenReturn(pageObj);

        PageIndex page = new PageIndex(1, 5);

        // action
        var faultsData = service.listFaults(page);

        // assert
        assertEquals(faultsData.getData().count(), 2);
    }

    @Test
    void getFaultSuccessNotExists() {
        // arrange
        when(faults.findById(1L)).thenReturn(Optional.empty());

        // action
        var result = service.getFault(1L);

        // assert
        assertFalse(result.isPresent());
    }

    @Test
    void getFaultExists() {
        // arrange
        Room room = new Room(1L,"BOL", "Boole", 100,
            null,
            new Building(
                1L, "EWI", "EEMCS building",
                new ApiTime(8, 0), new ApiTime(22, 0),
                Set.of()), Set.of());

        Fault fault = new Fault(1L, "Blue ball machine broke", room);
        when(faults.findById(1L)).thenReturn(Optional.of(fault));

        // action
        var result = service.getFault(1L);

        // assert
        assertTrue(result.isPresent());
    }

    @Test
    void resolveFaultTestNotAdmin() throws Exception {
        // arrange
        UserModel simplyEmployee = new UserModel(1, "jjansen", new String[]{"employee"});
        when(users.currentUser()).thenReturn(simplyEmployee);

        // action + assert
        ApiException exception = assertThrows(ApiException.class,
            () -> service.resolveFault(1));
        assertEquals(exception.getService(), "Faults");
    }

    @Test
    void resolveFaultTestNoFault() throws Exception {
        // arrange
        UserModel admin = new UserModel(1, "jjansen", new String[]{"admin"});
        when(users.currentUser()).thenReturn(admin);

        when(faults.existsById(1L)).thenReturn(false);

        // action + assert
        EntityNotFound exception = assertThrows(EntityNotFound.class,
            () -> service.resolveFault(1));
        assertEquals(exception.getEntityName(), "Fault");
    }


    @Test
    void resolveFaultTestSuccess() throws Exception {
        // arrange
        UserModel admin = new UserModel(1, "jjansen", new String[]{"admin"});
        when(users.currentUser()).thenReturn(admin);

        Room room = new Room(1L,"BOL", "Boole", 100,
            null,
            new Building(
                1L, "EWI", "EEMCS building",
                new ApiTime(8, 0), new ApiTime(22, 0),
                Set.of()), Set.of());

        Fault fault = new Fault(1L, "Blue ball machine broke", room);

        when(faults.existsById(1L)).thenReturn(true);
        when(faults.getById(1L)).thenReturn(fault);

        // action
        service.resolveFault(1);

        // assert
        verify(faults, times(1)).delete(fault);
    }
}
