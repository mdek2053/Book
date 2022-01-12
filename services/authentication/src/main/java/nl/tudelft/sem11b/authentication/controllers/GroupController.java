package nl.tudelft.sem11b.authentication.controllers;

import java.util.List;

import nl.tudelft.sem11b.authentication.services.GroupServiceImpl;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.GroupModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    transient GroupServiceImpl groupService;

    @Autowired
    transient UserServiceImpl userService;

    public GroupController(GroupServiceImpl groupService, UserServiceImpl userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    /**
     * Tries to add a new group to the system.
     *
     * @param model of type List containing the groupMembers.
     * @return an object containing the new Group.
     */
    @PostMapping("")
    @PreAuthorize("hasRole('Admin')")
    public GroupModel postGroup(@RequestBody GroupModel model) {
        try {
            return groupService
                    .addGroup(model.getName(), model.getSecretary(), model.getGroupMembers());
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    @GetMapping(value = "/mine")
    public List<GroupModel> getGroupsOfCurrentUser()
            throws InvalidData, ApiException {
        return groupService.getGroupsOfCurrentUser(userService.currentUser().getId());
    }

    @GetMapping(value = "/user/{id}")
    public List<GroupModel> getGroupsOfUser(@PathVariable Long id) {
        return groupService.getGroupsOfUser(id);
    }

    @GetMapping(value = "/secretary/{id}")
    public List<GroupModel> getGroupsOfSecretary(@PathVariable Long id) {
        return groupService.getGroupsOfSecretary(id);
    }

    @GetMapping(value = "/{id}")
    public GroupModel getGroupInfo(@PathVariable Long id) throws
            ApiException, InvalidData {
        return groupService.getGroupInfo(id);
    }

    /**
     * Tries to add new groupMembers to a group.
     *
     * @param users of type List which contains the new groupMembers.
     * @param group of type Group containing the group which the users need to be added to.
     * @throws InvalidData when the group or one of the new group members is not valid.
     */
    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = "/members")
    public void addGroupMember(@RequestBody List<Long> users, @RequestBody GroupModel group)
            throws InvalidData {
        groupService.addGroupMembers(users, group);
    }
}
