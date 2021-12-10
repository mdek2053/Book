package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;

import java.util.Optional;

import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
}
