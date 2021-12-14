package nl.tudelft.sem11b.services;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;

/**
 * API definition of the room service. This service is responsible for holding the information about
 * rooms.
 */
public interface RoomsService {
    /**
     * Lists a page of all rooms in the system. Note that only basic data about each room is
     * returned.
     *
     * @param page Page index to fetch
     * @return The requested page of rooms
     * @throws ApiException Thrown when a remote API encountered an error
     */
    PageData<RoomStudModel> listRooms(PageIndex page) throws ApiException;

    /**
     * Lists a page of all rooms in the given building. Note that only basic data about each room is
     * returned.
     *
     * @param page     Page index to fetch
     * @param building Unique numeric identifier of the building to list the rooms of
     * @return The requested page of rooms in the given building
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given building was not found
     */
    PageData<RoomStudModel> listRooms(PageIndex page, long building)
        throws ApiException, EntityNotFound;

    /**
     * Gets all information about a room with the given unique numeric identifier.
     *
     * @param id The unique numeric identifier of the room to fetch
     * @return Room information if found; an empty optional otherwise
     * @throws ApiException Thrown when a remote API encountered an error
     */
    Optional<RoomModel> getRoom(long id) throws ApiException;
}
