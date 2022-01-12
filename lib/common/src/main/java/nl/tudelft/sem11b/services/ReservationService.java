package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;

/**
 * API definition of the reservation service. This service is responsible for management of
 * reservations.
 */
public interface ReservationService {
    /**
     * Creates a new reservation for the current user.
     *
     * @param roomId Unique numeric identifier of the room where the reservation takes place
     * @param title  Title of the reservation
     * @param since  Beginning date and time of the reservation
     * @param until  Ending date and time of the reservation
     * @return Unique numeric identifier of the newly created reservation
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     * @throws InvalidData    Thrown when the given data is invalid
     */
    long makeOwnReservation(long roomId, String title, ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData;


    /**
     * Creates a new reservation for a provided user, which can be made by a secretary or admin.
     *
     * @param roomId  Unique numeric identifier of the room where the reservation takes place
     * @param forUser Id of user for whom the reservation will be made
     * @param title   Title of the reservation
     * @param since   Beginning date and time of the reservation
     * @param until   Ending date and time of the reservation
     * @return Unique numeric identifier of the newly created reservation
     * @throws ApiException                     Thrown when a remote API encountered an error
     * @throws EntityNotFound                   Thrown when the given room was not found
     * @throws InvalidData                      Thrown when the given data/user is invalid
     */
    long makeUserReservation(long roomId, Long forUser, String title,
                             ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData;

    /**
     * Lists a page of reservations created by/for the current user.
     *
     * @param page Page index to fetch
     * @return The requested page of reservations
     * @throws ApiException Thrown when a remote API encountered an error
     */
    PageData<ReservationModel> inspectOwnReservation(PageIndex page) throws ApiException;

    /**
     * Updates the data of a reservation. Note that the current user must have sufficient
     * permissions to do so (be the owner, be a secretary, be an admin). Note that this method is
     * necessarily idempotent.
     *
     * @param reservationId Unique numeric identifier of the reservation to update
     * @param title         New name of the reservation ({@code null} to keep the old value)
     * @param since         New beginning date and time of the reservation ({@code null} to keep the
     *                      old value)
     * @param until         New ending date and time of the reservation ({@code null} to keep the
     *                      old value)
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given reservation was not found
     * @throws InvalidData    Thrown when the given data is invalid
     */
    void editReservation(long reservationId, String title, ApiDateTime since,
                         ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData;

    /**
     * Deletes reservation with provided reservationId if the user is authorized to do it.
     * @param reservationId   The id of reservation to be deleted
     * @throws EntityNotFound Thrown when the given reservation was not found
     * @throws ApiException   Thrown when the given data is invalid
     */
    void deleteReservation(long reservationId) throws ApiException, EntityNotFound;

    /**
     * Checks whether a reservation request is valid and whether the room is available for the
     * provided reservation request.
     *
     * @param roomModelId  Unique numeric identifier of the room for which the reservation
     *                     wants to be made
     * @param requestModel Object containing the reservation request
     * @return a boolean value of whether the room is available for the specified reservation
     * @throws InvalidData  Thrown when the given data is invalid
     * @throws ApiException Thrown when a remote API encountered an error
     */
    boolean checkAvailability(long roomModelId, ReservationRequestModel requestModel)
            throws InvalidData, ApiException;
}
