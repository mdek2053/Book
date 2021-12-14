package nl.tudelft.sem11b.authentication;

import java.util.List;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.services.GroupServiceImpl;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.UserModel;
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
    GroupServiceImpl groupService;

    @Autowired
    UserServiceImpl userService;

    public GroupController(GroupServiceImpl groupService, UserServiceImpl userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    /**
     * Tries to add a new group to the system.
     *
     * @param groupMembers of type List containing the groupMembers.
     * @return an object containing the new Group.
     * @throws InvalidGroupCredentialsException when the provided credentials
     *      of the group are not valid.
     * @throws InvalidCredentialsException when the provided credentials
     *      of a specific user is not valid.
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping()
    public GroupModel postGroup(@RequestBody String name, @RequestBody UserModel secretary,
                           @RequestBody List<Long> groupMembers)
            throws InvalidGroupCredentialsException {
        try {
            return groupService.addGroup(name, secretary, groupMembers);
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    @GetMapping(value = "/mine")
    public List<GroupModel> getGroupsOfEmployee(@RequestBody UserModel user)
            throws NoAssignedGroupException {
        return groupService.getGroupsOfUser(user);
    }

    @GetMapping(value = "/{id}")
    public GroupModel getGroupInfo(@PathVariable int id) throws InvalidGroupCredentialsException {
        return groupService.getGroupInfo(id);
    }

    /**
     * Tries to add new groupMembers to a group.
     *
     * @param users of type List which contains the new groupMembers.
     * @param group of type Group containing the group which the users need to be added to.
     * @throws InvalidGroupCredentialsException when provided credentials
     *      of the group are not valid.
     */
    @PostMapping(value = "/groupmembers")
    public void addGroupMember(@RequestBody List<Long> users, @RequestBody GroupModel group)
            throws InvalidGroupCredentialsException {
        groupService.addGroupMembers(users, group);
    }
}
