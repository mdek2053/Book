package nl.tudelft.sem11b.admin.data.repositories;

import nl.tudelft.sem11b.admin.data.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByBuildingId(long id, Pageable pageable);
}
