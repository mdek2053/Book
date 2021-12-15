package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;

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
     * @throws InvalidGroupCredentialsException Thrown when the current user is not a secretary
     * @throws InvalidData                      Thrown when the given data is invalid
     */
    long makeUserReservation(long roomId, Long forUser, String title,
                             ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidGroupCredentialsException, InvalidData;

    /**
     * Verifies whether the current user is a secretary of the provided user.
     *
     * @param forUser Id of user for whom the specific reservation is
     * @return boolean value of whether the current user has the correct rights
     * @throws ApiException Thrown when a remote API encountered an error
     */
    boolean verifySecretary(Long forUser) throws ApiException;

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
    void editReservation(long reservationId, String title, ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData;
}
