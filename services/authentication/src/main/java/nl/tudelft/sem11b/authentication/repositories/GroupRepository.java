package nl.tudelft.sem11b.authentication.repositories;

import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<List<Group>> findAllByGroupIdExists();     //Find all existing groups

    Optional<Integer> findTopByOrderByGroupIdDesc();    //Find the group with the highest groupId

    Optional<List<Group>> findGroupsBySecretary(User user);     //Find the groups of a specific secretary

}
