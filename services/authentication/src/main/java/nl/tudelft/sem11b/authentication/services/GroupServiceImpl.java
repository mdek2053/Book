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
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
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
     * @param id of the user of whom we want to know the group he/she is part of.
     * @return a list of all groups which the current user is part of.
     */
    public List<GroupModel> getGroupsOfUser(Long id) {
        List<Group> groupList = groupRepository.findAll();
        List<GroupModel> userGroupList = new ArrayList<>();

        if (!groupList.isEmpty()) {
            for (Group group : groupList) {
                if (group.getGroupMembers().contains(id)) {
                    userGroupList.add(group.createGroupModel());
                }
            }
        }
        return userGroupList;
    }

    /**
     * Tries to get the groups of a specific secretary.
     *
     * @param id of the user of whom we want to know the groups they are part of.
     * @return a list of groups for which the provided user is a secretary.
     */
    public List<GroupModel> getGroupsOfSecretary(Long id) {
        Optional<User> secretary = userRepository.findUserById(id);
        List<GroupModel> groups = new ArrayList<>();
        if (secretary.isPresent()) {
            Optional<List<Group>> groupList =
                    groupRepository.findGroupsBySecretary(secretary.get().getId());

            if (groupList.isPresent()) {
                List<Group> temp = groupList.get();
                for (Group group : temp) {
                    groups.add(group.createGroupModel());
                }
            }
        }
        return groups;
    }

    /**
     * Retrieves a list of groups of which the current user is part of as member or secretary.
     *
     * @param id of the user of whom we want to get the groups of
     * @return a list of groups of which the provided user is a member or secretary
     */
    public List<GroupModel> getGroupsOfCurrentUser(Long id) throws InvalidData {
        if (id == null) {
            return null;
        }
        List<GroupModel> groups = new ArrayList<>();
        groups.addAll(getGroupsOfUser(id));
        groups.addAll(getGroupsOfSecretary(id));
        if (groups.isEmpty()) {
            throw new InvalidData("No groups found in the system or "
                    + "the user is not part of any group");
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
        User secretary = null;
        if (secretaryId == null) {
            UserModel secretaryModel;
            secretaryModel = userService.currentUser();
            Optional<User> optionalUser = userRepository.findUserByNetId(secretaryModel.getLogin());
            if (optionalUser.isPresent()) {
                secretary = optionalUser.get();
            }
        } else {
            Optional<User> optionalUser = userRepository.findUserById(secretaryId);
            if (optionalUser.isPresent()) {
                secretary = optionalUser.get();
            }
        }

        verifyUsers(groupMembers);

        assert secretary != null;
        Group group = new Group(name, secretary.getId(), groupMembers);
        group = groupRepository.save(group);
        return new GroupModel(group.getName(), secretary.getId(), groupMembers, group.getGroupId());
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
