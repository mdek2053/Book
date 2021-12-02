package nl.tudelft.sem11b.admin.data.repositories;

import nl.tudelft.sem11b.admin.data.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {
}
