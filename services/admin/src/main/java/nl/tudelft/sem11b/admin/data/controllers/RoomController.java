package nl.tudelft.sem11b.admin.data.controllers;

import java.text.ParseException;
import java.util.Optional;

import nl.tudelft.sem11b.admin.services.ServerInteractionHelper;
import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureObject;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST API proxy for {@link RoomsService}.
 */
@RestController
public class RoomController {
    private final RoomsService rooms;
    private ServerInteractionHelper serverInteractionHelper = new ServerInteractionHelper();

    /**
     * Instantiates the {@link RoomController} class.
     *
     * @param rooms The room handling service
     */
    public RoomController(RoomsService rooms) {
        this.rooms = rooms;
    }

    public void setServerInteractionHelper(ServerInteractionHelper serverInteractionHelper) {
        this.serverInteractionHelper = serverInteractionHelper;
    }

    /**
     * Lists all rooms in the system.
     *
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of rooms
     */
    @GetMapping("/rooms")
    public PageData<RoomStudModel> listRooms(
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        return rooms.listRooms(index);
    }

    /**
     * Lists all rooms in a building with the given id.
     *
     * @param id ID of a building to get the rooms of
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of rooms
     */
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

    /**
     * Gets room information.
     *
     * @param id ID of room to fetch
     * @return Room information
     */
    @GetMapping("/rooms/{id}")
    public RoomModel getRoom(@PathVariable int id) {
        var room = rooms.getRoom(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found!");
        }

        return room.get();
    }

    /**
     * Changes or adds a room closure.
     * @param token user's token
     * @param id ID of room to close
     * @param closure closure information
     */
    @PostMapping("/room/{id}/closure")
    public void closeRoom(@RequestHeader("Authorization") String token, @PathVariable int id,
                                    @RequestBody ClosureObject closure) {
        if (closure == null || !closure.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Closure object not valid");
        }

        try {
            serverInteractionHelper.verifyUserAdmin(token);
        } catch (CommunicationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.reason);
        }


        var room = rooms.getRoom(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found!");
        }

        try {
            rooms.closeRoom(id, closure);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dates are not valid");
        }
    }

    /**
     * Removes a room closure, i.e. reopens the room.
     * @param token user's token
     * @param id ID of room to reopen
     */
    @DeleteMapping("/room/{id}/closure")
    public void closeRoom(@RequestHeader("Authorization") String token, @PathVariable int id) {

        try {
            serverInteractionHelper.verifyUserAdmin(token);
        } catch (CommunicationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.reason);
        }

        var room = rooms.getRoom(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found!");
        }

        rooms.reopenRoom(id);
    }
}
