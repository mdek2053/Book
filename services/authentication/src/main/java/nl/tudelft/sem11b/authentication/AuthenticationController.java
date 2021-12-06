package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.exceptions.InvalidCredentialsException;
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


    @GetMapping("/me")
    public User me() {
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
}
