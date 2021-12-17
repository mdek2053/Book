package nl.tudelft.sem11b.services;


import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
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

    PageData<RoomStudModel> searchRooms(PageIndex page, Map<String, Object> filterValues)
            throws ApiException, EntityNotFound, InvalidFilterException;

    /**
     * Gets all information about a room with the given unique numeric identifier.
     *
     * @param id The unique numeric identifier of the room to fetch
     * @return Room information if found; an empty optional otherwise
     * @throws ApiException Thrown when a remote API encountered an error
     */
    Optional<RoomModel> getRoom(long id) throws ApiException;

    void addRoom(RoomModel model) throws ApiException;

    /**
     * Adds a closure to a room. The user making the change must have admin rights.
     *
     * @param id       The id of the room that is modified.
     * @param closure  Object containing information about the closure.
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     */
    void closeRoom(long id, ClosureModel closure) throws ApiException, EntityNotFound;

    /**
     * Removes any existing closure from a room. The user making the change must have admin rights.
     *
     * @param id The id of the room that is modified.
     */
    void reopenRoom(long id) throws EntityNotFound, ApiException;

    /**
     * Creates a fault report.
     * @param roomId the id of the room that needs maintenance
     * @param faultRequest  Object containing information about the fault
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     **/
    void addFault(long roomId, FaultRequestModel faultRequest) throws ApiException, EntityNotFound;

    /**
     * Lists a page of all faults for a given room.
     *
     * @param page   Page index to fetch
     * @param roomId ID of the room for which we get faults for
     * @return The requested page of faults in the given room
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     */
    PageData<FaultStudModel> listFaults(PageIndex page, long roomId)
            throws ApiException, EntityNotFound;

    /**
     * Lists a page of all faults.
     *
     * @param page   Page index to fetch
     * @return The requested page of faults in the given room
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     */
    PageData<FaultModel> listFaults(PageIndex page) throws ApiException, EntityNotFound;

    /**
     * Gets all information about a fault with the given unique numeric identifier.
     *
     * @param id The unique numeric identifier of the fault to fetch
     * @return Fault information if found; an empty optional otherwise
     * @throws ApiException Thrown when a remote API encountered an error
     */
    Optional<FaultModel> getFault(long id) throws ApiException;

    /**
     * Removes a fault. The user making the change must have admin rights.
     *
     * @param id The id of the fault that is removed.
     */
    void resolveFault(long id) throws EntityNotFound, ApiException;
}
