package nl.tudelft.sem11b.admin.data.repositories;

import nl.tudelft.sem11b.admin.data.entities.Fault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaultRepository extends JpaRepository<Fault, Long> {
    Page<Fault> findAllByRoomId(long roomId, Pageable id);
}
