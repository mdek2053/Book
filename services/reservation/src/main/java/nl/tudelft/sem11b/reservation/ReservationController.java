package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ReservationController {
    ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
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

}
