package nl.tudelft.sem11b.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void makeReservation(long room_id, long user_id, String title, String since, String until) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
        Date sinceDate = new Date(dateFormat.parse(since).getTime());
        Date untilDate = new Date(dateFormat.parse(until).getTime());

        reservationRepository.makeReservation(room_id, user_id, title, sinceDate, untilDate, null);
    }
}
