package nl.tudelft.sem11b.reservation;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST API proxy for {@link ReservationService}.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    transient ReservationService reservationService;

    /**
     * Instantiates the {@link ReservationController} class.
     *
     * @param reservationService The reservation handling service
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Creates a new reservation on behalf of the current user.
     *
     * @param req Reservation data
     * @return ID of the newly created reservation
     */
    @PostMapping()
    IdModel<Long> makeReservation(@RequestBody ReservationRequestModel req) {
        if (req == null || !req.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request body empty or doesn't contain all required fields");
        }

        if (req.getForUser() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

        try {
            long reservationId = reservationService.makeOwnReservation(req.getRoomId(),
                    req.getTitle(), req.getSince(), req.getUntil());
            return new IdModel<>(reservationId);
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    /**
     * Lists the reservations of the current user.
     *
     * @param page  Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of reservations
     */
    @GetMapping("/mine")
    public PageData<ReservationModel> inspectOwnReservation(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return reservationService.inspectOwnReservation(index);
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    /**
     * Checks availability of room compared to the request sent.
     *
     * @param roomModelId  id of a RoomModel for which the availability needs to be checked
     * @param requestModel of type ReservationRequestModel which contains the reservation request
     * @return a boolean value whether to request is valid and
     *      does not collide with any other reservations
     */
    @PostMapping("/availability/{roomModelId}")
    public boolean checkAvailability(@PathVariable long roomModelId,
                                     @RequestBody ReservationRequestModel requestModel) {
        try {
            return reservationService.checkAvailability(roomModelId, requestModel);
        } catch (ServiceException invalidData) {
            invalidData.toResponseException();
        }
        return false;
    }

    /**
     * Updates the reservation with the given unique numeric identifier.
     *
     * @param id  ID of the reservation to update
     * @param req New reservation data
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeReservation(@PathVariable long id, @RequestBody ReservationRequestModel req) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body empty");
        }

        try {
            reservationService.editReservation(id, req.getTitle(), req.getSince(), req.getUntil());
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    /**
     * Deletes reservation with given id.
     * @param id of reservation that should be deleted.
     */
    @DeleteMapping("/{id}")
    void deleteReservation(@PathVariable long id) {
        try {
            reservationService.deleteReservation(id);
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }
}
