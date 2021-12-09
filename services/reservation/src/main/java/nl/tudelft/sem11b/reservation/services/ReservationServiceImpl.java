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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private Clock clock = Clock.systemUTC();

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    static final SimpleDateFormat openingTimeFormat = new SimpleDateFormat("HH:mm");
    static final SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        // TODO: Inject room and building services
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    // Method for creating a reservation in the database

    /**
     * Creates a reservation in the database.
     * @param roomId the id of the room
     * @param userId the user's id
     * @param title title of the meeting
     * @param since start date of the meeting
     * @param until end date of the meeting
     * @return created reservation's id
     * @throws InvalidData if there is anything illegal with the meeting
     * @throws ApiException if there is any communication problem with the server
     * @throws EntityNotFound if the room does not exist
     */
    public long makeReservation(long roomId, long userId,
                                String title, ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData {

//        TODO: Uncomment once API clients are in place
//        if (!serv.checkRoomExists(roomId)) {
//            throw new EntityNotFound("Room");
//        }

        // must have non-empty title
        if (title == null || title.length() == 0) {
            throw new InvalidData("Reservation must have title");
        }

        // T O D O horrendous time handling, should fix
        long timeAtLocal = System.currentTimeMillis();


        var now = LocalDateTime.now();
        var sinceDate = since.toLocal();
        var untilDate = until.toLocal();

        // check if the reservation is not in the past
        if (sinceDate.compareTo(now) < 0) {
            throw new InvalidData("Reservation is in the past");
        }

        // check if the reservation is not too far in the future
        if (sinceDate.until(now, ChronoUnit.DAYS) > 14) {
            throw new InvalidData("Reservation is more than two weeks away");
        }

        var sinceTs = Timestamp.valueOf(sinceDate);
        var untilTs = Timestamp.valueOf(untilDate);

        // check if it doesn't conflict with user's other reservations
        List<Reservation> conflictsUser = reservationRepository
                .getUserConflicts(userId, sinceTs, untilTs);
        if (conflictsUser != null && conflictsUser.size() > 0) {
            throw new InvalidData("Reservation conflicts with user's existing reservations");
        }

        // check if it doesn't conflict with room's other reservations
        List<Reservation> conflictsRoom = reservationRepository
                .getRoomConflicts(roomId, sinceTs, untilTs);
        if (conflictsRoom != null && conflictsRoom.size() > 0) {
            throw new InvalidData("Reservation conflicts with room's existing reservations");
        }

        // check room actually available
//        List<String> openingTimesStrings = serv.getOpeningHours(roomId);
//        TODO: Uncomment once API clients are in place
        List<String> openingTimesStrings = new ArrayList<>();

        Timestamp opening;
        Timestamp closing;
        Timestamp sinceDateMidnight;
        Timestamp untilDateMidnight;
        try {
            opening = new Timestamp(openingTimeFormat.parse(openingTimesStrings.get(0)).getTime());
            closing = new Timestamp(openingTimeFormat.parse(openingTimesStrings.get(1)).getTime());
            sinceDateMidnight = new Timestamp(onlyDateFormat.parse(onlyDateFormat.format(sinceDate))
                    .getTime());
            untilDateMidnight = new Timestamp(onlyDateFormat.parse(onlyDateFormat.format(untilDate))
                    .getTime());
        } catch (ParseException c) {
            throw new ApiException("rooms", c); // something went horribly wrong
        }

        // assuming it can't span across days
        if (sinceDateMidnight.getTime() != untilDateMidnight.getTime()) {
            throw new InvalidData("Reservation spans multiple days");
        }

        opening = new Timestamp(opening.getTime() + sinceDateMidnight.getTime());
        closing = new Timestamp(closing.getTime() + sinceDateMidnight.getTime());

        // now, actually check if in business hours
        if (opening.after(sinceTs) || closing.before(untilTs)) {
            throw new InvalidData("Reservation not between room opening hours");
        }

        // room should also NOT be under maintenance
        // TODO: Uncomment once API clients are in place
        // String maintenanceEnding = serv.getMaintenance(roomId);
        String maintenanceEnding = null;
        if (maintenanceEnding != null) { // change this to show the ETA when implemented
            throw new InvalidData("Room is under maintenance: " + maintenanceEnding);
        }

        return reservationRepository.saveAndFlush(
                new Reservation(roomId, userId, title, sinceTs, untilTs)).getId();
    }

    /**
     * Creates a new reservation in the database for a user.
     * @param roomId the id of the room
     * @param userToken the user's authorization token
     * @param title title of the meeting
     * @param since start date of the meeting
     * @param until end date of the meeting
     * @throws InvalidData if there is anything illegal with the meeting
     * @throws ApiException if there is any communication problem with the server
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
        return reservationRepository.getAll();
    }
}
