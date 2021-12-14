package nl.tudelft.sem11b.reservation.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import nl.tudelft.sem11b.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // two intervals A and B overlap iff
    // StartA < EndB or StartB < EndA
    @Transactional
    @Query(value = "SELECT COUNT(*) > 0 \n"
            + "FROM Reservation\n"
            + "WHERE userId = ?1\n"
            + "  AND (((?2 < until) AND (since < ?3)) OR ((since = ?2) AND (until = ?3)))")
    boolean hasUserConflict(long userId, Timestamp since, Timestamp until);

    @Transactional
    @Query(value = "SELECT COUNT(*) > 0 \n"
            + "FROM Reservation\n"
            + "WHERE roomId = ?1\n"
            + "  AND (((?2 < until) AND (since < ?3)) OR ((since = ?2) AND (until = ?3)))")
    boolean hasRoomConflict(long roomId, Timestamp since, Timestamp until);

    Page<Reservation> findByUserId(long userId, Pageable page);
}
