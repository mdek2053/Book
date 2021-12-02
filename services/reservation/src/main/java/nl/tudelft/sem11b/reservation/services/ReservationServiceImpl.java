package nl.tudelft.sem11b.reservation.services;

import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.data.exception.NotFoundException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class ReservationServiceImpl implements nl.tudelft.sem11b.services.ReservationService {
    private final ReservationRepository reservationRepository;
    private ServerInteractionHelper serv = new ServerInteractionHelper();
    private Clock clock = Clock.systemUTC();

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    static final SimpleDateFormat openingTimeFormat = new SimpleDateFormat("HH:mm");
    static final SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void setServ(ServerInteractionHelper serv) {
        this.serv = serv;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    // Method for creating a reservation in the database
    public long makeReservation(long room_id, long user_id, String title, String since, String until)
            throws ForbiddenException, CommunicationException, NotFoundException {

        if (!serv.checkRoomExists(room_id))
            throw new NotFoundException("Room does not exist");

        // must have non-empty title
        if (title == null || title.length() == 0)
            throw new ForbiddenException("Reservation must have title");

        // T O D O horrendous time handling, should fix
        Timestamp sinceDate, untilDate;

        try {
            sinceDate = new Timestamp(dateFormat.parse(since).getTime());
            untilDate = new Timestamp(dateFormat.parse(until).getTime());
        }
        catch (ParseException c) {
            throw new ForbiddenException("Date format invalid");
        }

        Timestamp currentDate = new Timestamp(Instant.now(clock).toEpochMilli());
        long week = 1209600000; // two weeks in ms

        // check if the reservation is not too far in the future
        if (sinceDate.getTime() - currentDate.getTime() > week)
            throw new ForbiddenException("Reservation is more than two weeks away");

        // check if the reservation is not in the past
        if (sinceDate.getTime() < currentDate.getTime())
            throw new ForbiddenException("Reservation is in the past");

        // check no conflicting reservations
        List<Reservation> conflicts = reservationRepository.getConflicts(user_id, sinceDate, untilDate);
        if (conflicts != null && conflicts.size() > 0)
            throw new ForbiddenException("Reservation conflicts with existing reservations");

        // check room actually available
        List<String> openingTimesStrings = serv.getOpeningHours(room_id);

        Timestamp opening, closing;
        Timestamp sinceDateMidnight, untilDateMidnight;
        try {
            opening = new Timestamp(openingTimeFormat.parse(openingTimesStrings.get(0)).getTime());
            closing = new Timestamp(openingTimeFormat.parse(openingTimesStrings.get(1)).getTime());
            sinceDateMidnight = new Timestamp(onlyDateFormat.parse(onlyDateFormat.format(sinceDate)).getTime());
            untilDateMidnight = new Timestamp(onlyDateFormat.parse(onlyDateFormat.format(untilDate)).getTime());
        }
        catch (ParseException c) {
            throw new CommunicationException(); // something went horribly wrong
        }

        // assuming it can't span across days
        if (sinceDateMidnight.getTime() != untilDateMidnight.getTime())
            throw new ForbiddenException("Reservation spans multiple days");

        opening = new Timestamp(opening.getTime() + sinceDateMidnight.getTime());
        closing = new Timestamp(closing.getTime() + sinceDateMidnight.getTime());

        // now, actually check if in business hours
        if (opening.after(sinceDate) || closing.before(untilDate))
            throw new ForbiddenException("Reservation not between room opening hours");

        // room should also NOT be under maintenance
        String maintenanceEnding = serv.getMaintenance(room_id);
        if (maintenanceEnding != null) // change this to show the ETA when implemented
            throw new ForbiddenException("Room is under maintenance: " + maintenanceEnding);

        return reservationRepository.saveAndFlush(new Reservation(room_id, user_id, title, sinceDate, untilDate)).getId();
    }

    /**
     * Creates a new reservation in the database for a user.
     * @param room_id the id of the room
     * @param user_token the user's authorization token
     * @param title title of the meeting
     * @param since start date of the meeting
     * @param until end date of the meeting
     * @throws ForbiddenException if there is anything illegal with the meeting
     * @throws CommunicationException if there is any communication problem with the server
     * @throws NotFoundException if the room does not exist
     * @throws UnauthorizedException if the token is invalid
     */
    public long makeOwnReservation(long room_id, String user_token, String title, String since, String until)
            throws ForbiddenException, CommunicationException, NotFoundException, UnauthorizedException {
        long user_id = serv.getUserId(user_token);
        return makeReservation(room_id, user_id, title, since, until);
    }

    // debug testing method
    public List<Reservation> getAll() {
        return reservationRepository.getAll();
    }
}
