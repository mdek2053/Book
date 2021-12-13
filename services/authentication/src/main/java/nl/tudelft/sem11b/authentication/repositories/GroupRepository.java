package nl.tudelft.sem11b.authentication.repositories;

import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.data.models.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<GroupModel> findGroupByGroupId(int groupId);   //Find group by specific group id

    Optional<List<GroupModel>> findAllByGroupIdExists();     //Find all existing groups

    Optional<List<GroupModel>> findGroupsBySecretary(User secretary); //Find groups of a secretary

}
