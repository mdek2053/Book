package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.models.UserModel;

public interface UserService {

    public UserModel getCurrentUser() throws InvalidCredentialsException;

    public UserModel addUser(UserModel user) throws InvalidCredentialsException;

}
