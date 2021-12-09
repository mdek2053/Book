package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.models.UserModel;
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
    UserServiceImpl service;


    @GetMapping("/me")
    public UserModel me() throws InvalidCredentialsException {
        return service.getCurrentUser();
    }

    /**
     * Tries to add a new user to the system.
     *
     * @param newUser an object of type User.
     * @return an object with the new User.
     */
    @PostMapping(value = "/")
    public UserModel postUser(@RequestBody UserModel newUser) throws InvalidCredentialsException {
        newUser = service.addUser(newUser);

        return newUser;
    }


}
