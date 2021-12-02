package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.entity.ReservationRequest;
import nl.tudelft.sem11b.reservation.exception.ForbiddenException;
import nl.tudelft.sem11b.reservation.exception.NotFoundException;
import nl.tudelft.sem11b.reservation.exception.UnauthorizedException;
import nl.tudelft.sem11b.reservation.exception.CommunicationException;
import nl.tudelft.sem11b.reservation.services.ReservationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @PostMapping("/reservations")
    JSONObject makeReservation(@RequestHeader("Authorization") String token, @RequestBody ReservationRequest req) {
        if (req == null || !req.validate())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request body empty or doesn't contain all required fields");

        boolean ownReservation = (req.getFor_user() == null);

        try {
            long reservationId = -1;
            if (ownReservation) {
                reservationId = reservationService.makeOwnReservation(req.getRoom_id(), token,
                        req.getTitle(), req.getSince(), req.getUntil());
            }
            return new JSONObject().put("id", reservationId);
        }
        catch (CommunicationException c) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error communicating with other microservices");
        }
        catch (UnauthorizedException c) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, c.reason);
        }
        catch (NotFoundException c) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, c.reason);
        }
        catch (ForbiddenException c) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, c.reason);
        }
    }

}
