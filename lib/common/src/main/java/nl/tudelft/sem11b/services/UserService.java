package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.UserModel;

public interface UserService {
    UserModel currentUser() throws ApiException;
}
