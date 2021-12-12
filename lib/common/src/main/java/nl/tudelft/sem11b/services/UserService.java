package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.UserModel;

/**
 * API definition of the user service. This service is responsible for holding the information about
 * users.
 */
public interface UserService {
    /**
     * Gets information about the current user.
     *
     * @return User information
     * @throws ApiException Thrown when a remote API encountered an error
     */
    UserModel currentUser() throws ApiException;
}
