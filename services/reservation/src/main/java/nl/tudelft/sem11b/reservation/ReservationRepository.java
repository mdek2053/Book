package nl.tudelft.sem11b.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;

@Repository("ReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reservation (room_id, user_id, title, since, until, cancel) "
            + "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void makeReservation(long room_id, long user_id, String title, Date since, Date until, String cancel_reason);
}
