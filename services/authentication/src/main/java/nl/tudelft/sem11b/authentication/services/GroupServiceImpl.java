package nl.tudelft.sem11b.authentication.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;
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
    transient GroupRepository groupRepository;

    @Autowired
    transient UserRepository userRepository;

    @Autowired
    transient UserServiceImpl userService;

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
        List<Group> groupList = groupRepository.findAll();

        if (!groupList.isEmpty()) {

            List<GroupModel> userGroupList = new ArrayList<>();
            for (Group group : groupList) {
                if (group.getGroupMembers().contains(user.getId())) {
                    GroupModel model = group.createGroupModel();
                    userGroupList.add(model);
                }
            }

            userGroupList = getGroupsOfSecretary(user, userGroupList);

            if (!userGroupList.isEmpty()) {
                return userGroupList;
            } else {
                throw new NoAssignedGroupException("User is not assigned to any group");
            }
        } else {
            throw new NoAssignedGroupException("There are no groups in the system");
        }
    }

    /**
     * Tries to get the groups of a specific secretary.
     *
     * @param user of type UserModel.
     * @return a list of groups for which the provided user is a secretary.
     */
    public List<GroupModel> getGroupsOfSecretary(UserModel user, List<GroupModel> groups) {
        Optional<User> secretary = userRepository.findUserByNetId(user.getLogin());
        if (secretary.isPresent()) {
            Optional<List<Group>> groupList =
                    groupRepository.findGroupsBySecretary(secretary.get().getId());

            if (groupList.isPresent()) {
                List<Group> secretaryGroups = groupList.get();
                for (Group group : secretaryGroups) {
                    GroupModel model = group.createGroupModel();
                    if (!groups.contains(model)) {
                        groups.add(model);
                    }
                }
            }
        }
        return groups;
    }

    /**
     * Adds a new group to the system after checking the validity of the input.
     *
     * @param secretaryId    of type Long, if of the secretary of the new group.
     * @param groupMembers of type List, contains a list of users who will be part of the group.
     * @return the group after it is added
     * @throws InvalidGroupCredentialsException when a group already exists
     *      with the specific groupId or when the credentials are invalid.
     */
    public GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws ApiException, InvalidGroupCredentialsException {
        User secretary;
        if (secretaryId == null) {
            UserModel secretaryModel;
            secretaryModel = userService.currentUser();
            secretary = userRepository.findUserByNetId(secretaryModel.getLogin()).get();
        } else {
            secretary = userRepository.findUserById(secretaryId).get();
        }

        verifyUsers(groupMembers);

        Group group = new Group(name, secretary.getId(), groupMembers);
        group = groupRepository.save(group);
        return new GroupModel(group.getName(), secretaryId, groupMembers, group.getGroupId());
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
            Optional<User> user = userRepository.findUserById(member);
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
    public GroupModel getGroupInfo(Long groupId) throws InvalidGroupCredentialsException,
            ApiException, InvalidCredentialsException {
        Optional<Group> group = groupRepository.findGroupByGroupId(groupId);
        if (group.isPresent()) {
            Group presentGroup = group.get();
            UserModel currentUser = userService.currentUser();
            if (currentUser.inRole(Roles.Admin)
                    || currentUser.getId() == presentGroup.getSecretary()
                    || presentGroup.getGroupMembers().contains(currentUser.getId())) {
                return new GroupModel(presentGroup.getName(), presentGroup.getSecretary(),
                    presentGroup.getGroupMembers(), presentGroup.getGroupId());
            } else {
                throw new InvalidCredentialsException("User not allowed to "
                        + "access this group's info.");
            }
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
