package nl.tudelft.sem11b.reservation.services;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.AbstractList;
import java.util.List;
import java.util.TimeZone;

import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.data.exception.NotFoundException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Creates a reservation in the database.
     * @param roomId the id of the room
     * @param userId the user's id
     * @param title title of the meeting
     * @param since start date of the meeting
     * @param until end date of the meeting
     * @return created reservation's id
     * @throws ForbiddenException if there is anything illegal with the meeting
     * @throws CommunicationException if there is any communication problem with the server
     * @throws NotFoundException if the room does not exist
     */
    public long makeReservation(long roomId, long userId,
                                String title, String since, String until)
            throws ForbiddenException, CommunicationException, NotFoundException {

        if (!serv.checkRoomExists(roomId)) {
            throw new NotFoundException("Room does not exist");
        }

        // must have non-empty title
        if (title == null || title.length() == 0) {
            throw new ForbiddenException("Reservation must have title");
        }

        // T O D O horrendous time handling, should fix
        long timeAtLocal = System.currentTimeMillis();
        long offset = TimeZone.getDefault().getOffset(timeAtLocal);

        Timestamp sinceDate;
        Timestamp untilDate;

        try {
            sinceDate = new Timestamp(dateFormat.parse(since).getTime());
            untilDate = new Timestamp(dateFormat.parse(until).getTime());
        } catch (ParseException c) {
            throw new ForbiddenException("Date format invalid");
        }

        Timestamp currentDate = new Timestamp(Instant.now(clock).toEpochMilli() - offset);
        long week = 1209600000; // two weeks in ms

        // check if the reservation is not too far in the future
        if (sinceDate.getTime() - currentDate.getTime() > week) {
            throw new ForbiddenException("Reservation is more than two weeks away");
        }

        // check if the reservation is not in the past
        if (sinceDate.getTime() < currentDate.getTime()) {
            throw new ForbiddenException("Reservation is in the past");
        }

        // check if it doesn't conflict with user's other reservations
        List<Reservation> conflictsUser = reservationRepository
                .getUserConflicts(userId, sinceDate, untilDate);
        if (conflictsUser != null && conflictsUser.size() > 0) {
            throw new ForbiddenException("Reservation conflicts with user's existing reservations");
        }

        // check if it doesn't conflict with room's other reservations
        List<Reservation> conflictsRoom = reservationRepository
                .getRoomConflicts(roomId, sinceDate, untilDate);
        if (conflictsRoom != null && conflictsRoom.size() > 0) {
            throw new ForbiddenException("Reservation conflicts with room's existing reservations");
        }

        // check room actually available
        List<String> openingTimesStrings = serv.getOpeningHours(roomId);

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
            throw new CommunicationException(); // something went horribly wrong
        }

        // assuming it can't span across days
        if (sinceDateMidnight.getTime() != untilDateMidnight.getTime()) {
            throw new ForbiddenException("Reservation spans multiple days");
        }

        opening = new Timestamp(opening.getTime() + sinceDateMidnight.getTime());
        closing = new Timestamp(closing.getTime() + sinceDateMidnight.getTime());

        // now, actually check if in business hours
        if (opening.after(sinceDate) || closing.before(untilDate)) {
            throw new ForbiddenException("Reservation not between room opening hours");
        }

        // room should also NOT be under maintenance
        String maintenanceEnding = serv.getMaintenance(roomId);
        if (maintenanceEnding != null) { // change this to show the ETA when implemented
            throw new ForbiddenException("Room is under maintenance: " + maintenanceEnding);
        }

        return reservationRepository.saveAndFlush(
                new Reservation(roomId, userId, title, sinceDate, untilDate)).getId();
    }

    /**
     * Creates a new reservation in the database for a user.
     * @param roomId the id of the room
     * @param userToken the user's authorization token
     * @param title title of the meeting
     * @param since start date of the meeting
     * @param until end date of the meeting
     * @throws ForbiddenException if there is anything illegal with the meeting
     * @throws CommunicationException if there is any communication problem with the server
     * @throws NotFoundException if the room does not exist
     * @throws UnauthorizedException if the token is invalid
     */
    public long makeOwnReservation(long roomId, String userToken, String title,
                                   String since, String until)
            throws ForbiddenException, CommunicationException,
            NotFoundException, UnauthorizedException {
        long userId = serv.getUserId(userToken);
        return makeReservation(roomId, userId, title, since, until);
    }

    public List<ReservationModel> inspectOwnReservation(String token) throws CommunicationException, UnauthorizedException, NoSuchFieldException, IllegalAccessException {
        List<Reservation> reservationList = reservationRepository.getOwnReservation(serv.getUserId(token));
        List<ReservationModel> reservationModels = null;
        for (Reservation reservation : reservationList) {
            Field roomId = reservation.getClass().getDeclaredField("roomId");
            roomId.setAccessible(true);
            int roomId02 = (int) roomId.get(reservation);

            Field since = reservation.getClass().getDeclaredField("since");
            since.setAccessible(true);
            Timestamp since02 = (Timestamp) since.get(reservation);

            Field until = reservation.getClass().getDeclaredField("until");
            until.setAccessible(true);
            Timestamp until02 = (Timestamp) until.get(reservation);

            Field title = reservation.getClass().getDeclaredField("title");
            title.setAccessible(true);
            String title02 = (String) title.get(reservation);

            reservationModels.add(new ReservationModel(roomId02, since02, until02, title02));
        }

        return reservationModels;
    }

    // debug testing method
    public List<Reservation> getAll() {
        return reservationRepository.getAll();
    }
}
