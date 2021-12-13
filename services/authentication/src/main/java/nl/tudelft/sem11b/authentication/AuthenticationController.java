package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.data.models.UserRequestModel;
import nl.tudelft.sem11b.services.UserService;
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
    @PostMapping(value = "/")
    public IdModel<Long> postUser(@RequestBody UserRequestModel model) {
        long id;
        try {
            id = service.addUser(model.getLogin(), model.getPassword(), Roles.valueOf(model.getRole()));
        } catch (ServiceException ex) {
            throw ex.toResponseException();
        }

        return new IdModel<>(id);
    }


}
