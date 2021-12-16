package nl.tudelft.sem11b.authentication.controllers;

import java.util.List;

import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.data.models.UserRequestModel;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    transient UserService service;

    /**
     * Gets the current user.
     *
     * @return Current user
     */
    @GetMapping("/me")
    public UserModel me() {
        try {
            return service.currentUser();
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    /**
     * Tries to add a new user to the system.
     *
     * @param model an object of type User.
     * @return an object with the new User.
     */
    @PostMapping(value = "")
    @PreAuthorize("hasRole('Admin')")
    public IdModel<Long> postUser(@RequestBody UserRequestModel model) {
        try {
            long id = service.addUser(model.getLogin(), model.getPassword(),
                Roles.valueOf(model.getRole()));
            return new IdModel<>(id);
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('Admin')")
    public List<UserModel> getAllUsers() throws ApiException {
        return service.getAllUsers();
    }
}
