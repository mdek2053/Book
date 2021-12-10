package nl.tudelft.sem11b.reservation.services;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.services.ReservationService;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservations;
    private final RoomsService rooms;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservations, RoomsService rooms) {
        this.reservations = reservations;
        this.rooms = rooms;
    }

    public long makeReservation(long roomId, long userId,
                                String title, ApiDateTime since, ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData {

        // fetch room information (includes building information)
        var roomOpt = rooms.getRoom(roomId);
        if (roomOpt.isEmpty()) {
            throw new EntityNotFound("Room");
        }

        var room = roomOpt.get();

        // must have non-empty title
        if (title == null || title.isBlank()) {
            throw new InvalidData("Reservation must have title");
        }

        validateTime(since, until);
        validateRoom(room, since, until);

        var sinceTs = Timestamp.valueOf(since.toLocal());
        var untilTs = Timestamp.valueOf(until.toLocal());

        validateConflicts(userId, roomId, sinceTs, untilTs);

        var reservation = new Reservation(roomId, userId, title, sinceTs, untilTs);
        return reservations.saveAndFlush(reservation).getId();
    }

    private void validateTime(ApiDateTime since, ApiDateTime until) throws InvalidData {
        var now = LocalDateTime.now();
        var sinceJava = since.toLocal();
        var untilJava = until.toLocal();

        // check if since comes before until
        if (sinceJava.compareTo(untilJava) >= 0) {
            throw new InvalidData("Reservation ends before it starts");
        }

        // check if the reservation is not in the past
        if (sinceJava.compareTo(now) < 0) {
            throw new InvalidData("Reservation is in the past");
        }

        // check if the reservation is not too far in the future
        if (sinceJava.until(now, ChronoUnit.DAYS) > 14) {
            throw new InvalidData("Reservation is more than two weeks away");
        }

        // check if the reservation spans multiple days
        if (!since.getDate().equals(until.getDate())) {
            throw new InvalidData("Reservation spans multiple days");
        }
    }

    private void validateRoom(RoomModel room, ApiDateTime since, ApiDateTime until)
        throws InvalidData {
        // check if in business hours
        if (room.getBuilding().getOpen().compareTo(since.getTime()) > 0
            || room.getBuilding().getClose().compareTo(since.getTime()) < 0) {
            throw new InvalidData("Reservation not between room opening hours");
        }

        // room should also NOT be under maintenance
        var closure = room.getClosure();
        if (closure == null) {
            return; // not under closure
        }

        // check if closure is still ongoing
        if (closure.getUntil() == null || closure.getUntil().compareTo(since.getDate()) > 0) {
            if (closure.getUntil() != null) {
                throw new InvalidData(
                    "Room is under maintenance (until " + closure.getUntil() + ")");
            }

            throw new InvalidData("Room is under maintenance");
        }

        // closure has already ended
    }

    private void validateConflicts(long userId, long roomId, Timestamp since, Timestamp until)
        throws InvalidData {
        // check if it doesn't conflict with user's other reservations
        if (reservations.hasUserConflict(userId, since, until)) {
            throw new InvalidData("Reservation conflicts with user's existing reservations");
        }

        // check if it doesn't conflict with room's other reservations
        if (reservations.hasRoomConflict(roomId, since, until)) {
            throw new InvalidData("Reservation conflicts with room's existing reservations");
        }
    }

    public long makeOwnReservation(long roomId, String title,
                                   ApiDateTime since, ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData {
        // TODO: Uncomment once API clients are in place
        // long userId = serv.getUserId(userToken);
        long userId = 0;
        return makeReservation(roomId, userId, title, since, until);
    }

    public PageData<ReservationModel> inspectOwnReservation(PageIndex page) throws ApiException {
        // TODO: get user ID
        var userId = 0;
        var data = reservations.findByUserId(userId, page.getPage(Sort.by("id")));
        return new PageData<>(data.map(Reservation::toModel));
    }

    public void editReservation(long reservationId, String title, ApiDateTime since,
                                ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData {
        // TODO: Implement once user service interface is wired in
//        long userId = serv.getUserId(userToken);
//        String role = serv.getUserRole(userToken);
        long userId = 0;
        String role = "admin";
        var reservationOpt = reservations.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new EntityNotFound("Reservation");
        }
        var reservation = reservationOpt.get();

        if (userId != reservation.getUserId() && !role.equals("admin")) {
            throw new ApiException("Reservation",
                "User not authorized to change given reservation.");
        }

        var roomOpt = rooms.getRoom(reservation.getRoomId());
        if (roomOpt.isEmpty()) {
            throw new EntityNotFound("Room");
        }
        var room = roomOpt.get();

        validateTime(since, until);
        validateRoom(room, since, until);

        var sinceTs = Timestamp.valueOf(since.toLocal());
        var untilTs = Timestamp.valueOf(until.toLocal());

        validateConflicts(userId, reservation.getRoomId(), sinceTs, untilTs);

        if (title != null) {
            if (title.isBlank()) {
                throw new InvalidData("Reservation must have a title");
            }
            reservation.setTitle(title);
        }

        reservation.setSince(sinceTs);
        reservation.setUntil(untilTs);

        reservations.save(reservation);
    }

    // debug testing method
    public List<Reservation> getAll() {
        return reservations.findAll();
    }
}
