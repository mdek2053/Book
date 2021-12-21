package nl.tudelft.sem11b.clients;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;

/**
 * A client for the {@link UserService} API. This client requires authentication.
 */
public class UserClient implements UserService {
    private final transient ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link UserClient} class.
     *
     * @param api API client with authentication
     */
    public UserClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public UserModel currentUser() throws ApiException {
        return api.get("/users/me", new TypeReference<UserModel>() {}).unwrap();
    }

    @Override
    public long addUser(String netId, String password, Roles role)
        throws ApiException, InvalidData {
        return -1; // TODO: Implement
    }

    @Override
    public List<UserModel> getAllUsers() throws ApiException {
        return api.get("/users/all", new TypeReference<List<UserModel>>() {}).unwrap();
    }
}
