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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Provides a controller to handle the authentication of the system.
 */
@RestController
@RequestMapping("/users")
public class AuthenticationController {

    @Autowired
    UserService service;

    @Autowired
    GroupService groupService;


    @GetMapping("/me")
    public User me() throws InvalidCredentialsException {
        return service.getCurrentUser();
    }

    /**
     * Tries to add a new user to the system.
     *
     * @param newUser an object of type User.
     * @return an object with the new User.
     */
    @PostMapping(value = "/")
    public User postUser(@RequestBody User newUser) throws InvalidCredentialsException {
        newUser = service.addUser(newUser);

        return newUser;
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
    @PostMapping(value = "/new-group")
    public Group postGroup(@RequestBody List<User> groupMembers)
            throws InvalidGroupCredentialsException, InvalidCredentialsException {
        return groupService.addGroup(service.getCurrentUser(), groupMembers);
    }

    @GetMapping(value = "/get-groups")
    public List<Group> getGroupsOfEmployee(@RequestBody User user) throws NoAssignedGroupException {
        return groupService.getGroupsOfUser(user);
    }

    @GetMapping(value = "/get-secretary-groups")
    public List<Group> getGroupsOfSecretary(@RequestBody User user)
            throws NoAssignedGroupException {
        return groupService.getGroupsOfSecretary(user);
    }

    /**
     * Tries to add new groupMembers to a group.
     *
     * @param users of type List which contains the new groupMembers.
     * @param group of type Group containing the group which the users need to be added to.
     * @return an object containing all the groupMembers.
     * @throws InvalidGroupCredentialsException when provided credentials
     *      of the group are not valid.
     */
    @PostMapping(value = "/add-members")
    public List<User> addGroupMember(@RequestBody List<User> users, @RequestBody Group group)
            throws InvalidGroupCredentialsException {
        try {
            groupService.verifyUsers(users);
        } catch (InvalidGroupCredentialsException e) {
            throw new InvalidGroupCredentialsException("At least one provided member "
                    + "is not registered in the system yet");
        }
        return group.addToGroupMembers(users);
    }
}
