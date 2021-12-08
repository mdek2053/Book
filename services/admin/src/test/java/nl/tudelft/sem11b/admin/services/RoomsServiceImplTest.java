package nl.tudelft.sem11b.admin.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Optional;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.Day;
import nl.tudelft.sem11b.data.models.ClosureObject;
import nl.tudelft.sem11b.services.RoomsService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RoomsServiceImplTest {
    @Autowired
    RoomsService roomService;

    @MockBean
    BuildingRepository buildings;

    @MockBean
    RoomRepository rooms;

    @Test
    void closeRoom() throws ParseException {
        ClosureObject closure = new ClosureObject("a banana fell",
                "2021-09-21", "2021-09-22");

        Room roomOpen = new Room();

        when(rooms.findById(1)).thenReturn(Optional.of(roomOpen));

        assertDoesNotThrow(() -> roomService.closeRoom(1, closure));

        Closure clo = new Closure(closure.getDescription(),
                Day.parse(closure.getSince()),
                Day.parse(closure.getUntil()));
        
        ArgumentCaptor<Room> argumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(rooms, times(1)).save(argumentCaptor.capture());
        assertSame(argumentCaptor.getValue().getClosure().getReason(), clo.getReason());
    }

    @Test
    void reopenRoom() throws ParseException {
        ClosureObject closure = new ClosureObject("a banana fell",
                "2021-09-21", "2021-09-22");

        Room roomClosed = new Room();
        Closure clo = new Closure(closure.getDescription(),
                Day.parse(closure.getSince()),
                Day.parse(closure.getUntil()));
        roomClosed.setClosure(clo);

        when(rooms.findById(1)).thenReturn(Optional.of(roomClosed));

        assertDoesNotThrow(() -> roomService.reopenRoom(1));

        ArgumentCaptor<Room> argumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(rooms, times(1)).save(argumentCaptor.capture());
        assertNull(argumentCaptor.getValue().getClosure());
    }
}