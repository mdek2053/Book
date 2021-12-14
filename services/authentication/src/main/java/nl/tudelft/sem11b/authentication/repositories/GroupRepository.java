package nl.tudelft.sem11b.authentication.repositories;

import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.data.models.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findGroupByGroupId(Long groupId);   //Find group by specific group id

    Optional<List<Group>> findGroupsBySecretary(User secretary); //Find groups of a secretary

}
