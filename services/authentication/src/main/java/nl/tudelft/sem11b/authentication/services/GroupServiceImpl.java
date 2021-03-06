package nl.tudelft.sem11b.authentication.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Retrieves all groups where the provided user is part of.
     *
     * @param id of the user of whom we want to know the group he/she is part of.
     * @return a list of all groups which the current user is part of.
     */
    public List<GroupModel> getGroupsOfUser(Long id) {
        List<Group> groupList = groupRepository.findAll();
        List<GroupModel> groupModelList = new ArrayList<>();

        if (!groupList.isEmpty()) {
            for (Group group : groupList) {
                if (group.getGroupMembers().contains(id)) {
                    groupModelList.add(group.createGroupModel());
                }
            }
        }
        return groupModelList;
    }

    /**
     * Tries to get the groups of a specific secretary.
     *
     * @param id of the user of whom we want to know the groups they are part of.
     * @return a list of groups for which the provided user is a secretary.
     */
    public List<GroupModel> getGroupsOfSecretary(Long id) {
        Optional<User> secretary = userRepository.findUserById(id);
        List<GroupModel> groupModelList = new ArrayList<>();
        if (secretary.isPresent()) {
            Optional<List<Group>> optGroupList =
                    groupRepository.findGroupsBySecretary(secretary.get().getId());

            if (optGroupList.isPresent()) {
                List<Group> groupList = optGroupList.get();
                for (Group group : groupList) {
                    groupModelList.add(group.createGroupModel());
                }
            }
        }
        return groupModelList;
    }

    /**
     * Adds a new group to the system after checking the validity of the input.
     *
     * @param name         of type String, which contains the name of the group.
     * @param secretaryId  of type Long, if of the secretary of the new group.
     * @param groupMembers of type List, contains a list of users who will be part of the group.
     * @return the group after it is added
     * @throws ApiException when the current user is not a valid user of the system.
     * @throws InvalidData  when at least one of the group members is not a valid user.
     */
    public GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws ApiException, InvalidData {
        User secretary;
        if (secretaryId == null) {
            UserModel secretaryModel;
            secretaryModel = userService.currentUser();
            Optional<User> optionalUser = userRepository.findUserByNetId(secretaryModel.getLogin());
            if (optionalUser.isPresent()) {
                secretary = optionalUser.get();
            } else {
                throw new InvalidData("Invalid user");
            }
        } else {
            Optional<User> optionalUser = userRepository.findUserById(secretaryId);
            if (optionalUser.isPresent()) {
                secretary = optionalUser.get();
            } else {
                throw new InvalidData("Invalid user");
            }
        }

        verifyUsers(groupMembers);

        Group group = new Group(name, secretary.getId(), groupMembers);
        group = groupRepository.save(group);
        return new GroupModel(group.getName(), secretary.getId(),
                groupMembers, group.getGroupId());
    }

    /**
     * Verifies the list of users if they are registered in the system.
     *
     * @param groupMembers of type List containing new members of the group.
     * @throws InvalidData when at least one of the entries
     *                     in the list is not registered in the system.
     */
    public void verifyUsers(List<Long> groupMembers) throws InvalidData {
        for (Long member : groupMembers) {
            Optional<User> user = userRepository.findUserById(member);
            if (user.isEmpty()) {
                throw new InvalidData("At least one of the provided users "
                        + "is not registered in the system");
            }
        }
    }

    /**
     * Gets the info of a specific group.
     *
     * @param groupId over type Integer containing the groupId.
     * @return the group which is connected to the provided groupId.
     * @throws ApiException when the current user is not a valid user of the system.
     * @throws InvalidData  when the user is not allowed to access the group info or
     *                      when there is no group with the provided id.
     */
    public GroupModel getGroupInfo(Long groupId) throws
            ApiException, InvalidData {
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
                throw new InvalidData("User not allowed to "
                        + "access this group's info.");
            }
        } else {
            throw new InvalidData(
                    "There is no group with the provided group id");
        }
    }

    /**
     * Tries to add new members to an existing group.
     *
     * @param users of type List containing new members.
     * @param groupId of type Long containing the groupId of the group
     *                where we want to add the new members to.
     * @throws InvalidData when at least one provided user
     *                     is not specified in the system.
     */
    public void addGroupMembers(List<Long> users, Long groupId)
            throws InvalidData {
        Optional<Group> optGroup = groupRepository.findGroupByGroupId(groupId);
        if (optGroup.isEmpty()) {
            throw new InvalidData("No group found with this id");
        }
        GroupModel group = optGroup.get().createGroupModel();
        verifyUsers(users);
        group.addToGroupMembers(users);
    }
}
