package nl.tudelft.sem11b.authentication.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides a service to handle different requests with groups.
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Retrieves all groups where the provided user is part of.
     *
     * @param user of type User of whom we want to know the group he/she is part of.
     * @return a list of all groups which the current user is part of.
     * @throws NoAssignedGroupException when the user is not part of any group.
     */
    public List<GroupModel> getGroupsOfUser(UserModel user)
            throws NoAssignedGroupException {
        Optional<List<GroupModel>> groupList = groupRepository.findAllByGroupIdExists();

        if (groupList.isPresent()) {
            List<GroupModel> presentGroupList = groupList.get();
            List<GroupModel> userGroupList = new ArrayList<>();
            for (GroupModel group : presentGroupList) {
                if (group.getGroupMembers().contains(user.getId())) {
                    userGroupList.add(group);
                }
            }
            if (userGroupList.size() > 0) {
                userGroupList = getGroupsOfSecretary(user, userGroupList);
                return userGroupList;
            } else {
                throw new NoAssignedGroupException("User is not assigned to any groups");
            }
        } else {
            throw new NoAssignedGroupException("There are no group in the system");
        }
    }

    /**
     * Tries to get the groups of a specific secretary.
     *
     * @param user of type UserModel.
     * @return a list of groups for which the provided user is a secretary.
     */
    public List<GroupModel> getGroupsOfSecretary(UserModel user, List<GroupModel> groups) {
        User userNew = new User(user.getNetId(), user.getRole(),
                passwordEncoder.encode(user.getPassword()));
        Optional<List<GroupModel>> groupList = groupRepository.findGroupsBySecretary(userNew);

        if (groupList.isPresent()) {
            List<GroupModel> secretaryGroups = groupList.get();
            for (GroupModel group : secretaryGroups) {
                if (!groups.contains(group)) {
                    groups.add(group);
                }
            }
        }
        return groups;
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
     * @param secretary    of type UserModel, who is the secretary of the new group.
     * @param groupMembers of type List, contains a list of users who will be part of the group.
     * @return the group after it is added
     * @throws InvalidGroupCredentialsException when a group already exists
     *      with the specific groupId or when the credentials are invalid.
     */
    public GroupModel addGroup(String name, UserModel secretary, List<Long> groupMembers)
            throws ApiException, InvalidGroupCredentialsException {
        if (secretary == null) {
            UserModel secretaryModel;
            secretaryModel = userService.currentUser();
            secretary = userRepository.findUserByNetId(secretaryModel.getLogin()).get();
        }
        try {
            verifyUsers(groupMembers);
        } catch (InvalidGroupCredentialsException e) {
            throw new InvalidGroupCredentialsException("At least one provided member "
                    + "is not registered in the system yet");
        }
        Group group = new Group(name, new User(secretary.getNetId(), secretary.getRole(),
                passwordEncoder.encode(secretary.getPassword())), groupMembers);
        saveGroup(group);
        return new GroupModel(group.getName(), secretary, groupMembers);
    }

    /**
     * Verifies the list of users if they are registered in the system.
     *
     * @param groupMembers of type List containing new members of the group.
     * @throws InvalidGroupCredentialsException when at least one of the entries
     *      in the list is not registered in the system.
     */
    public void verifyUsers(List<Long> groupMembers) throws InvalidGroupCredentialsException {
        for (Long member : groupMembers) {
            Optional<UserModel> user = userRepository.findUserById(member);
            if (user.isEmpty()) {
                throw new InvalidGroupCredentialsException("At least one of the provided users "
                        + "is not registered in the system");
            }
        }
    }

    /**
     * Gets the info of a specific group.
     *
     * @param groupId over type Integer containing the groupId.
     * @return the group which is connected to the provided groupId.
     * @throws InvalidGroupCredentialsException when there is no group
     *      connected to the provided groupId.
     */
    public GroupModel getGroupInfo(int groupId) throws InvalidGroupCredentialsException {
        Optional<GroupModel> group = groupRepository.findGroupByGroupId(groupId);
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new InvalidGroupCredentialsException(
                    "There is no group with the provided group id");
        }
    }

    /**
     * Tries to add new members to an existing group.
     *
     * @param users of type List containing new members.
     * @param group of type GroupModel containing the group where we want to add the new members to.
     * @throws InvalidGroupCredentialsException when at least one provided user
     *      is not specified in the system.
     */
    public void addGroupMembers(List<Long> users, GroupModel group)
            throws InvalidGroupCredentialsException {
        try {
            verifyUsers(users);
        } catch (InvalidGroupCredentialsException e) {
            throw new InvalidGroupCredentialsException("At least one provided member "
                    + "is not registered in the system yet");
        }
        group.addToGroupMembers(users);
    }
}
