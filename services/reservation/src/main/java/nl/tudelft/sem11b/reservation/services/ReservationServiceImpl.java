package nl.tudelft.sem11b.reservation.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.services.ReservationService;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservations;
    private final RoomsService rooms;

    static final SimpleDateFormat openingTimeFormat = new SimpleDateFormat("HH:mm");
    static final SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservations, RoomsService rooms) {
        this.reservations = reservations;
        this.rooms = rooms;
    }

    // Method for creating a reservation in the database

    /**
     * Creates a reservation in the database.
     *
     * @param roomId the id of the room
     * @param userId the user's id
     * @param title  title of the meeting
     * @param since  start date of the meeting
     * @param until  end date of the meeting
     * @return created reservation's id
     * @throws InvalidData    if there is anything illegal with the meeting
     * @throws ApiException   if there is any communication problem with the server
     * @throws EntityNotFound if the room does not exist
     */
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
        if (title == null || title.length() == 0) {
            throw new InvalidData("Reservation must have title");
        }

        var now = LocalDateTime.now();
        var sinceJava = since.toLocal();
        var untilJava = until.toLocal();

        // check if the reservation is not in the past
        if (sinceJava.compareTo(now) < 0) {
            throw new InvalidData("Reservation is in the past");
        }

        // check if the reservation is not too far in the future
        if (sinceJava.until(now, ChronoUnit.DAYS) > 14) {
            throw new InvalidData("Reservation is more than two weeks away");
        }

        if (!since.getDate().equals(until.getDate())) {
            throw new InvalidData("Reservation spans multiple days");
        }

        // now, actually check if in business hours
        if (room.getBuilding().getOpen().compareTo(since.getTime()) > 0
            || room.getBuilding().getClose().compareTo(since.getTime()) < 0) {
            throw new InvalidData("Reservation not between room opening hours");
        }

        var sinceTs = Timestamp.valueOf(sinceJava);
        var untilTs = Timestamp.valueOf(untilJava);

        // check if it doesn't conflict with user's other reservations
        List<Reservation> conflictsUser = reservations
            .getUserConflicts(userId, sinceTs, untilTs);
        if (conflictsUser != null && conflictsUser.size() > 0) {
            throw new InvalidData("Reservation conflicts with user's existing reservations");
        }

        // check if it doesn't conflict with room's other reservations
        List<Reservation> conflictsRoom = reservations
            .getRoomConflicts(roomId, sinceTs, untilTs);
        if (conflictsRoom != null && conflictsRoom.size() > 0) {
            throw new InvalidData("Reservation conflicts with room's existing reservations");
        }

        // room should also NOT be under maintenance
        var closure = room.getClosure();
        if (closure != null &&
            (closure.getUntil() == null || closure.getUntil().compareTo(since.getDate()) > 0)) {
            if (closure.getUntil() != null) {
                throw new InvalidData(
                    "Room is under maintenance (until " + closure.getUntil() + ")");
            }
            throw new InvalidData("Room is under maintenance");
        }

        var reservation = new Reservation(roomId, userId, title, sinceTs, untilTs);
        return reservations.saveAndFlush(reservation).getId();
    }

    /**
     * Creates a new reservation in the database for a user.
     *
     * @param roomId    the id of the room
     * @param userToken the user's authorization token
     * @param title     title of the meeting
     * @param since     start date of the meeting
     * @param until     end date of the meeting
     * @throws InvalidData    if there is anything illegal with the meeting
     * @throws ApiException   if there is any communication problem with the server
     * @throws EntityNotFound if the room does not exist
     */
    public long makeOwnReservation(long roomId, String title,
                                   ApiDateTime since, ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData {
        // TODO: Uncomment once API clients are in place
        // long userId = serv.getUserId(userToken);
        long userId = 0;
        return makeReservation(roomId, userId, title, since, until);
    }

    // debug testing method
    public List<Reservation> getAll() {
        return reservations.getAll();
    }
}
