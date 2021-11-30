package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exceptions.BadTokenException;
import nl.tudelft.sem11b.reservation.exceptions.CommunicationException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    ServerInteractionHelper serv = new ServerInteractionHelper();

    @PostMapping("/reservations")
    ResponseEntity<String> makeReservation(@RequestHeader("Authorization") String token, @RequestBody String body) {
        try {
            long id = serv.getUserId(token);

            JSONObject obj = new JSONObject(body);
            if (!serv.checkRoomExists(token, obj.getLong("room")))
                return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);

            if(!obj.has("for")) {
                try {
                    reservationService.makeReservation(obj.getLong("room"), id,
                            obj.getString("title"), obj.getString("since"), obj.getString("until"));
                } catch (ParseException e) {
                    return new ResponseEntity<>("Date format not valid", HttpStatus.FORBIDDEN);
                }
            }
            return new ResponseEntity<>("", HttpStatus.OK);
        }
        catch (CommunicationException c) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (BadTokenException c) {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }
    }

}
