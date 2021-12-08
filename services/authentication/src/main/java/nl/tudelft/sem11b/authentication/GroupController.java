package nl.tudelft.sem11b.authentication;

import java.util.List;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.exceptions.InvalidCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.NoAssignedGroupException;
import nl.tudelft.sem11b.authentication.services.GroupService;
import nl.tudelft.sem11b.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    GroupService groupService;

    @Autowired
    UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
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
    @PostMapping()
    public Group postGroup(@RequestBody String name, @RequestBody User secretary,
                           @RequestBody List<Long> groupMembers)
            throws InvalidGroupCredentialsException, InvalidCredentialsException {
        return groupService.addGroup(name, secretary, groupMembers);
    }

    @GetMapping(value = "/groups/mine")
    public List<Group> getGroupsOfEmployee(@RequestBody User user) throws NoAssignedGroupException {
        return groupService.getGroupsOfUser(user);
    }

    @GetMapping(value = "/{id}")
    public Group getGroupInfo(@PathVariable int id) throws InvalidGroupCredentialsException {
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
    public void addGroupMember(@RequestBody List<Long> users, @RequestBody Group group)
            throws InvalidGroupCredentialsException {
        groupService.addGroupMembers(users, group);
    }
}
