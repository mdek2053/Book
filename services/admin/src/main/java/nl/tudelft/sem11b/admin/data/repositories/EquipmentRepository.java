package nl.tudelft.sem11b.admin.data.repositories;

import java.util.Optional;

import nl.tudelft.sem11b.admin.data.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findByName(String name);
}
