package nl.tudelft.sem11b.authentication;

import java.util.List;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.exceptions.InvalidCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.InvalidGroupCredentialsException;
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
     * @return an message indicating whether the user has been added.
     */
    @PostMapping(value = "/")
    public User postUser(@RequestBody User newUser) throws InvalidCredentialsException {
        newUser = service.addUser(newUser);

        return newUser;
    }

    @PostMapping(value = "/new-group")
    public Group postGroup(@RequestBody User secretary, @RequestBody List<User> groupMembers)
            throws InvalidGroupCredentialsException {
        return groupService.addGroup(secretary, groupMembers);
    }
}
