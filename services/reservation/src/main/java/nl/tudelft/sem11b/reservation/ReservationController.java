package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exception.ForbiddenException;
import nl.tudelft.sem11b.reservation.exception.NotFoundException;
import nl.tudelft.sem11b.reservation.exception.UnauthorizedException;
import nl.tudelft.sem11b.reservation.exception.CommunicationException;
import nl.tudelft.sem11b.reservation.services.ReservationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @PostMapping("/reservations")
    ResponseEntity<String> makeReservation(@RequestHeader("Authorization") String token, @RequestBody String body) {
        JSONObject obj = new JSONObject(body);
        boolean ownReservation = !obj.has("for");

        try {
            if (ownReservation) {
                reservationService.makeOwnReservation(obj.getLong("room"), token,
                        obj.getString("title"), obj.getString("since"), obj.getString("until"));
            }

            return new ResponseEntity<>("", HttpStatus.OK);
        }
        catch (CommunicationException c) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (UnauthorizedException c) {
            return new ResponseEntity<>(c.reason, HttpStatus.UNAUTHORIZED);
        }
        catch (NotFoundException c) {
            return new ResponseEntity<>(c.reason, HttpStatus.NOT_FOUND);
        }
        catch (ForbiddenException c) {
            return new ResponseEntity<>(c.reason, HttpStatus.FORBIDDEN);
        }
    }

}
