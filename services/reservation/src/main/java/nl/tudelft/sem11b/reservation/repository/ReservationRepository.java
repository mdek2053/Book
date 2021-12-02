package nl.tudelft.sem11b.reservation.repository;

import nl.tudelft.sem11b.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository("ReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reservation (room_id, user_id, title, since, until, cancel) "
            + "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void makeReservation(long room_id, long user_id, String title, Timestamp since, Timestamp until, String cancel_reason);

    // two intervals A and B overlap iff
    // StartA < EndB or StartB < EndA
    @Transactional
    @Query(value = "SELECT * \n" +
            "FROM reservation\n" +
            "WHERE user_id = ?1\n" +
            "  AND (((?2 < until) AND (since < ?3)) OR ((since = ?2) AND (until = ?3)))", nativeQuery = true)
    List<Reservation> getUserConflicts(long user_id, Timestamp since, Timestamp until);

    @Transactional
    @Query(value = "SELECT * \n" +
            "FROM reservation\n" +
            "WHERE room_id = ?1\n" +
            "  AND (((?2 < until) AND (since < ?3)) OR ((since = ?2) AND (until = ?3)))", nativeQuery = true)
    List<Reservation> getRoomConflicts(long room_id, Timestamp since, Timestamp until);

    // debug testing method
    @Transactional
    @Query(value = "SELECT * \n" +
            "FROM reservation", nativeQuery = true)
    List<Reservation> getAll();
}
