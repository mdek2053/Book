package nl.tudelft.sem11b.authentication;

import java.util.List;

import nl.tudelft.sem11b.authentication.services.GroupServiceImpl;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
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
     *                                          of the group are not valid.
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping()
    public GroupModel postGroup(@RequestBody String name, @RequestBody Long secretaryId,
                                @RequestBody List<Long> groupMembers)
            throws InvalidGroupCredentialsException {
        try {
            return groupService.addGroup(name, secretaryId, groupMembers);
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    @GetMapping(value = "/mine")
    public List<GroupModel> getGroupsOfEmployee()
            throws NoAssignedGroupException, ApiException {
        return groupService.getGroupsOfUser(userService.currentUser());
    }

    @GetMapping(value = "/{id}")
    public GroupModel getGroupInfo(@PathVariable Long id) throws InvalidGroupCredentialsException {
        return groupService.getGroupInfo(id);
    }

    /**
     * Tries to add new groupMembers to a group.
     *
     * @param users of type List which contains the new groupMembers.
     * @param group of type Group containing the group which the users need to be added to.
     * @throws InvalidGroupCredentialsException when provided credentials
     *                                          of the group are not valid.
     */
    @PostMapping(value = "/groupmembers")
    public void addGroupMember(@RequestBody List<Long> users, @RequestBody GroupModel group)
            throws InvalidGroupCredentialsException {
        groupService.addGroupMembers(users, group);
    }
}
