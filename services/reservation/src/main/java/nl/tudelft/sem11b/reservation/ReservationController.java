package nl.tudelft.sem11b.reservation;

import java.util.List;

import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.data.exception.NotFoundException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.entity.ReservationRequest;
import nl.tudelft.sem11b.reservation.entity.ReservationResponse;
import nl.tudelft.sem11b.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/reservations")
public class ReservationController {
    ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping()
    ReservationResponse makeReservation(@RequestHeader("Authorization") String token,
                                        @RequestBody ReservationRequest req) {
        if (req == null || !req.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request body empty or doesn't contain all required fields");
        }

        boolean ownReservation = (req.getForUser() == null);

        try {
            long reservationId = -1;
            if (ownReservation) {
                reservationId = reservationService.makeOwnReservation(req.getRoomId(), token,
                        req.getTitle(), req.getSince(), req.getUntil());
            }
            return new ReservationResponse(reservationId);
        } catch (CommunicationException c) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error communicating with other microservices");
        } catch (UnauthorizedException c) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, c.reason);
        } catch (NotFoundException c) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, c.reason);
        } catch (ForbiddenException c) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, c.reason);
        }
    }

    @GetMapping("/reservations/mine")
    public List<ReservationModel> inspectOwnReservation(
            @RequestHeader("Authorization") String token)
            throws CommunicationException, UnauthorizedException,
            NoSuchFieldException, IllegalAccessException {
        return reservationService.inspectOwnReservation(token);
    }

    @PostMapping("/{id}")
    ReservationResponse changeReservation(@PathVariable String id,
                                          @RequestHeader("Authorization") String token,
                                          @RequestBody ReservationRequest req) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Request body empty");
        }
        try {
            ReservationModel model = new ReservationModel(req.getRoomId(),
                    req.getSince(), req.getUntil(), req.getTitle());
            long reservationId = reservationService
                    .editReservation(token, model, Long.parseLong(id));
            return new ReservationResponse(reservationId);
        } catch (CommunicationException c) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error communicating with other microservices");
        } catch (UnauthorizedException c) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, c.reason);
        } catch (NotFoundException c) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, c.reason);
        } catch (ForbiddenException c) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, c.reason);
        }
    }
}
