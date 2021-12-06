package nl.tudelft.sem11b.authentication.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.exceptions.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.NoAssignedGroupException;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * Retrieves all groups where the provided user is part of.
     * @param user of type User of whom we want to know the group he/she is part of.
     * @return a list of all groups which the current user is part of.
     * @throws NoAssignedGroupException when the user is not part of any group.
     */
    public List<Group> getGroupsOfUser(User user)
            throws NoAssignedGroupException {
        Optional<List<Group>> groupList = groupRepository.findAllByGroupIdExists();

        if (groupList.isPresent()) {
            List<Group> presentGroupList = groupList.get();
            List<Group> userGroupList = new ArrayList<>();
            for (Group group : presentGroupList) {
                if (group.getGroupId() != 0 && group.getGroupMembers().contains(user)) {
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

    public List<Group> getGroupsOfSecretary(User user) throws NoAssignedGroupException {
        Optional<List<Group>> groupList = groupRepository.findGroupsBySecretary(user);

        if (groupList.isPresent()) {
            return groupList.get();
        } else {
            throw new NoAssignedGroupException("User has not been assigned to any group as secretary");
        }
    }

    /**
     * Saves the input object to the groupRepository.
     *
     * @param group of type Group which needs to be saved in the groupRepository.
     */
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    /**
     * Adds a new group to the system after checking the validity of the input.
     *
     * @param secretary    of type User, who is the secretary of the new group.
     * @param groupMembers of type List, contains a list of users who will be part of the group.
     * @return the group after it is added
     * @throws InvalidGroupCredentialsException when a group already exists
     *                                          with the specific groupId or when the credentials are invalid.
     */
    public Group addGroup(User secretary, List<User> groupMembers)
            throws InvalidGroupCredentialsException {
        if (secretary == null) {
            throw new InvalidGroupCredentialsException("Secretary needs to be provided");
        }
        try {
            verifyUsers(groupMembers);
        } catch (InvalidGroupCredentialsException e) {
            throw new InvalidGroupCredentialsException("At least one provided member is not registered in the system yet");
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

    /** Verifies the list of users if they are registered in the system.
     * @param groupMembers of type List<User> containing new members of the group.
     * @throws InvalidGroupCredentialsException when at least one of the entries
     *      in the list is not registered in the system.
     */
    public void verifyUsers(List<User> groupMembers) throws InvalidGroupCredentialsException {
        for (User member : groupMembers) {
            try {
                userService.loadUserByUsername(member.getNetId());
            } catch (UsernameNotFoundException e) {
                throw new InvalidGroupCredentialsException("At least one provided member is not registered in the system yet");
            }
        }
    }
}
