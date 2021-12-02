package nl.tudelft.sem11b.authentication.repositories;

import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findGroupByGroupId(int groupId);

    Optional<List<Group>> findAllByGroupIdExists();

    Optional<Integer> findTopByOrderByGroupIdDesc();

}
