package nl.tudelft.sem11b.admin.data.controllers;

import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
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

    /**
     * Instantiates the {@link RoomController} class.
     *
     * @param rooms The room handling service
     */
    public RoomController(RoomsService rooms) {
        this.rooms = rooms;
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

        try {
            return rooms.listRooms(index);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
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
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Lists all rooms that pass the filters.
     *
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @param filters The filters and values to be applied
     * @return Filtered page of rooms
     */
    @GetMapping("/rooms/filter")
    public PageData<RoomStudModel> searchRooms(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Map<String, Object>> filters) {
        var index = PageIndex.fromQuery(page, limit);

        if (filters.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "filters not provided!");
        }

        try {
            return rooms.searchRooms(index, filters.get());
        } catch (ServiceException e) {
            throw e.toResponseException();
        } catch (InvalidFilterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
        Optional<RoomModel> room;
        try {
            room = rooms.getRoom(id);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }

        if (room.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found!");
        }

        return room.get();
    }

    /**
     * Changes or adds a room closure.
     *
     * @param id      ID of the room being modified
     * @param closure closure information
     */
    @PostMapping("/rooms/{id}/closure")
    public void closeRoom(@PathVariable int id, @RequestBody ClosureModel closure)
            throws EntityNotFound, ApiException {
        rooms.closeRoom(id, closure);
    }

    /**
     * Removes a room closure, i.e. reopens the room.
     *
     * @param id ID of room to reopen
     */
    @DeleteMapping("/rooms/{id}/closure")
    public void reopenRoom(@PathVariable int id) throws EntityNotFound, ApiException {
        rooms.reopenRoom(id);
    }

    /**
     * Lists all faults in the system.
     *
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of faults
     */
    @GetMapping("/faults")
    public PageData<FaultModel> listFaults(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return rooms.listFaults(index);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Lists all faults for a room with the given id.
     *
     * @param id ID of the room to get faults for
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of faults
     */
    @GetMapping("/rooms/{id}/faults")
    public PageData<FaultStudModel> listFaultsByRoom(
            @PathVariable int id,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return rooms.listFaults(index, id);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Stores a fault report.
     *
     * @param id    ID of the room that needs maintenance
     * @param fault Request object with the information about the fault.
     */
    @PostMapping("/rooms/{id}/faults")
    public void addFault(@PathVariable int id, @RequestBody FaultRequestModel fault) {
        try {
            rooms.addFault(id, fault);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Gets fault information.
     *
     * @param id ID of fault to fetch
     * @return Fault information
     */
    @GetMapping("/faults/{id}")
    public FaultModel getFault(@PathVariable int id) {
        Optional<FaultModel> fault;
        try {
            fault = rooms.getFault(id);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }

        if (fault.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fault not found!");
        }

        return fault.get();
    }

    /**
     * Removes a fault, i.e. resolved the fault.
     *
     * @param id ID of fault to reopen
     */
    @DeleteMapping("/faults/{id}")
    public void resolveFault(@PathVariable int id) throws EntityNotFound, ApiException {
        rooms.resolveFault(id);
    }
}
