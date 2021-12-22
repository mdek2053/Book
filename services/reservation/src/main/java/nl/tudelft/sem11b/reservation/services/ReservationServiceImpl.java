package nl.tudelft.sem11b.reservation.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.Roles;
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
import nl.tudelft.sem11b.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final transient ReservationRepository reservations;
    private final transient RoomsService rooms;
    private final transient UserService users;
    private final transient String serviceName = "Reservation";

    private final transient String serviceName = "Reservation";

    /**
     * Instantiates the {@link ReservationServiceImpl} class.
     *
     * @param reservations Reservations repository
     * @param rooms        Rooms handling service
     * @param users        Users handling service
     */
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservations, RoomsService rooms,
                                  UserService users) {
        this.reservations = reservations;
        this.rooms = rooms;
        this.users = users;
    }

    /**
     * Creates a new reservation on behalf of the user with the given ID.
     *
     * @param roomId ID of the room where the reservation takes place
     * @param userId ID of the user making the reservation
     * @param title  Title of the reservation
     * @param since  Starting date and time of the reservation
     * @param until  Ending date and time of the reservation
     * @return ID of the newly created reservation
     * @throws ApiException   Thrown when a remote API encountered an error
     * @throws EntityNotFound Thrown when the given room was not found
     * @throws InvalidData    Thrown when the given data is invalid
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
        if (title == null || title.isBlank()) {
            throw new InvalidData("Reservation must have title");
        }

        validateTime(since, until);
        validateRoom(room, since, until);

        var sinceTs = Timestamp.valueOf(since.toLocal());
        var untilTs = Timestamp.valueOf(until.toLocal());

        validateConflicts(userId, roomId, sinceTs, untilTs);

        var reservation = new Reservation(roomId, userId, title, sinceTs, untilTs);
        return reservations.save(reservation).getId();
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
        int maxDaysInFuture = 14;
        if (now.until(sinceJava, ChronoUnit.DAYS) >= maxDaysInFuture) {
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
                || room.getBuilding().getClose().compareTo(until.getTime()) < 0) {
            throw new InvalidData("Reservation not between room opening hours");
        }

        // room should also NOT be under maintenance
        var closure = room.getClosure();
        if (closure == null) {
            return; // not under closure
        }

        // check if closure is still ongoing
        if (closure.getUntil() == null || closure.getUntil().compareTo(since.getDate()) >= 0) {
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

    @Override
    public long makeOwnReservation(long roomId, String title,
                                   ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData {
        return makeReservation(roomId, users.currentUser().getId(), title, since, until);
    }

    @Override
    public PageData<ReservationModel> inspectOwnReservation(PageIndex page) throws ApiException {
        var data =
                reservations.findByUserId(users.currentUser().getId(), page.getPage(Sort.by("id")));
        return new PageData<>(data.map(Reservation::toModel));
    }

    @Override
    public void editReservation(long reservationId, String title, ApiDateTime since,
                                ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData {
        var reservationOpt = reservations.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new EntityNotFound(serviceName);
        }
        var reservation = reservationOpt.get();

        if (since == null) {
            since = ApiDateTime.from(reservation.getSince());
        }

        if (until == null) {
            until = ApiDateTime.from(reservation.getUntil());
        }

        var user = users.currentUser();
        if (user.getId() != reservation.getUserId() && !user.inRole(Roles.Admin)) { //NOPMD
            throw new ApiException(serviceName,
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

        validateConflicts(user.getId(), reservation.getRoomId(), sinceTs, untilTs);

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
        var reservationOpt = reservations.findById(reservationId);

        if (reservationOpt.isEmpty()) {
            throw new EntityNotFound(serviceName);
        }
        var reservation = reservationOpt.get();

        var user = users.currentUser();
        if (!Objects.equals(user.getId(), reservation.getUserId()) && !user.inRole(Roles.Admin)) {
            throw new ApiException(serviceName,
                    "User not authorized to change given reservation.");
        }

        reservations.delete(reservation);
    }

    // debug testing method
    public List<Reservation> getAll() {
        return reservations.findAll();
    }
}
