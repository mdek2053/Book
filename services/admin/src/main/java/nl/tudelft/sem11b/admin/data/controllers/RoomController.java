package nl.tudelft.sem11b.admin.data.controllers;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RoomController {
    private final RoomsService rooms;

    public RoomController(RoomsService rooms) {
        this.rooms = rooms;
    }

    @GetMapping("/rooms")
    public PageData<RoomStudModel> listRooms(
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        return rooms.listRooms(index);
    }

    @GetMapping("/buildings/{id}/rooms")
    public PageData<RoomStudModel> listRoomsByBuilding(
        @PathVariable int id,
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return rooms.listRooms(index, id);
        } catch (EntityNotFound e) {
            throw e.toResponseException();
        }
    }

    @GetMapping("/rooms/{id}")
    public RoomModel getRoom(@PathVariable int id) {
        var room = rooms.getRoom(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found!");
        }

        return room.get();
    }
}
