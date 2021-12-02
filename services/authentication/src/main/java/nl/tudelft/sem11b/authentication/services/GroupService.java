package nl.tudelft.sem11b.authentication.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.exceptions.InvalidCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.NoAssignedGroupException;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides a service to handle different requests with groups.
 */
@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    /**
     * Retrieves all groups where the user is part of.
     * @return a list of all groups which the current user is part of.
     * @throws NoAssignedGroupException when the user is not part of any group.
     */
    public List<Group> getGroupsOfCurrentUser()
            throws NoAssignedGroupException, InvalidCredentialsException {
        User current = userService.getCurrentUser();
        Optional<List<Group>> groupList = groupRepository.findAllByGroupIdExists();

        if (groupList.isPresent()) {
            List<Group> presentGroupList = groupList.get();
            List<Group> userGroupList = new ArrayList<>();
            for (Group group : presentGroupList) {
                if (group.getGroupId() != 0 && group.getGroupMembers().contains(current)) {
                    userGroupList.add(group);
                }
            }
            if (userGroupList.size() > 0) {
                return userGroupList;
            } else {
                throw new NoAssignedGroupException("User is not assigned to any groups");
            }
        } else {
            throw new NoAssignedGroupException("There are no group in the system");
        }
    }

    /* public List<User> getUsersOfCurrentGroup() throws NoAssignedGroupException {
        User current = userService.getCurrentUser();
        Optional<List<User>> userList = userRepository.findUsersByGroupId(current.getGroupId());
        if (userList.isPresent()) {
            return userList.get();
        } else {
            throw new NoAssignedGroupException
            ("The group which belongs to this groupId has no members");
        }
    }*/
    /*public Group getCurrentGroup() throws NoAssignedGroupException {
        User current = userService.getCurrentUser();
        Optional<Group> group = groupRepository.findGroupByGroupId(current.getGroupId());
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new NoAssignedGroupException
            ("The group which belongs to this groupId has no members");
        }
    }*/

    /**
     * Saves the input object to the groupRepository.
     * @param group of type Group which needs to be saved in the groupRepository.
     */
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    /**
     * Adds a new group to the system after checking the validity of the input.
     * @param secretary of type User, who is the secretary of the new group.
     * @param groupMembers of type List, contains a list of users who will be part of the group.
     * @return the group after it is added
     * @throws InvalidGroupCredentialsException when a group already exists
     *      with the specific groupId or when the credentials are invalid.
     */
    public Group addGroup(User secretary, List<User> groupMembers)
            throws InvalidGroupCredentialsException {
        if (secretary == null) {
            throw new InvalidGroupCredentialsException("Secretary needs to be provided");
        }
        Optional<Integer> optGroupId = groupRepository.findTopByOrderByGroupIdDesc();
        int groupId = 1;
        if (optGroupId.isPresent() && optGroupId.get() != 0) {
            groupId = optGroupId.get() + 1;
        }
        Group group = new Group(secretary, groupMembers, groupId);
        saveGroup(group);
        return group;
    }
}
