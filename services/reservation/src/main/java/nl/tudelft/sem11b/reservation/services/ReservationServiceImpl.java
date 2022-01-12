package nl.tudelft.sem11b.reservation.services;

import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final transient ReservationRepository reservations;
    private final transient RoomValidationService validation;
    private final transient UserValidationService userValidation;

    /**
     * Instantiates the {@link ReservationServiceImpl} class.
     *
     * @param reservations Reservations repository
     */
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservations,
                                  RoomValidationService validation,
                                  UserValidationService userValidation) {
        this.reservations = reservations;
        this.validation = validation;
        this.userValidation = userValidation;
    }

    /**
     * Creates a new reservation on behalf of the user with the given ID.
     *
     * @param request ReservationRequestModel with reservation data
     * @return ID of the newly created reservation
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     * @throws InvalidData    Thrown when the given data is invalid
     */
    public long makeReservation(ReservationRequestModel request)
            throws ApiException, EntityNotFound, InvalidData {

        // must have non-empty title
        if (!request.checkTitle()) {
            throw new InvalidData("Reservation must have title");
        }

        validation.validateTime(request);
        validation.validateRoom(request.getRoomId(), request);

        userValidation.validateUserConflicts(request);
        validation.validateRoomConflicts(request.getRoomId(), request);

        Reservation reservation = Reservation.createReservation(request);

        return reservations.save(reservation).getId();
    }

    @Override
    public long makeOwnReservation(ReservationRequestModel request)
            throws ApiException, EntityNotFound, InvalidData {
        request.setForUser(userValidation.currentUserId());
        return makeReservation(request);
    }

    @Override
    public long makeUserReservation(ReservationRequestModel request)
            throws ApiException, InvalidData,
            EntityNotFound {
        if (userValidation.verifySecretary(request.getForUser())
                || userValidation.currentUserIsAdmin()) {
            return makeReservation(request);
        }
        throw new InvalidData("You are not a secretary of the provided user");
    }


    @Override
    public PageData<ReservationModel> inspectOwnReservation(PageIndex page)
            throws ApiException {
        var data =
                reservations.findByUserId(userValidation.currentUserId(),
                        page.getPage(Sort.by("id")));
        return new PageData<>(data.map(Reservation::toModel));
    }

    @Override
    public void editReservation(long reservationId, ReservationRequestModel request)
            throws ApiException, EntityNotFound, InvalidData {

        Reservation reservation = checkReservationExists(reservationId);
        userValidation.userCanModifyReservation(reservation.getUserId());

        reservation.fillOutTime(request);

        validation.validateTime(request);
        validation.validateRoom(reservation.getRoomId(), request);

        userValidation.validateUserConflicts(request);
        validation.validateRoomConflicts(reservation.getRoomId(), request);


        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()) {
                throw new InvalidData("Reservation must have a title");
            }
            reservation.setTitle(request.getTitle());
        }

        reservation.setTime(request);

        reservations.save(reservation);
    }

    /**
     * Deletes reservations if the person has permissions do it.
     * The person has permission to delete reservation only when they are admin
     * or when they created the reservation.
     * @param reservationId   The id of reservation to be deleted
     * @throws EntityNotFound is thrown when the reservation doesn't exist.
     * @throws ApiException is thrown when user is not authorized to
     *     delete reservation.
     */
    @Override
    public void deleteReservation(long reservationId) throws EntityNotFound, ApiException {
        Reservation reservation = checkReservationExists(reservationId);
        userValidation.userCanModifyReservation(reservation.getUserId());
        reservations.delete(reservation);
    }

    @Override
    public boolean checkAvailability(long roomModelId, ReservationRequestModel requestModel) {
        if (requestModel == null) {
            return false;
        }
        try {

            validation.validateRoomConflicts(roomModelId, requestModel);
            validation.validateTime(requestModel);
            validation.validateRoom(roomModelId, requestModel);
            return true;

        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    private Reservation checkReservationExists(Long reservationId)
            throws EntityNotFound {
        var reservationOpt = reservations.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new EntityNotFound("Reservation");
        }

        return reservationOpt.get();
    }
}
