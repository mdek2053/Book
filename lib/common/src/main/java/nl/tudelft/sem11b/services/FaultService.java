package nl.tudelft.sem11b.services;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;

/**
 * API definition of the fault service. This service is responsible for holding and managing the
 * information about room faults.
 */
public interface FaultService {

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
