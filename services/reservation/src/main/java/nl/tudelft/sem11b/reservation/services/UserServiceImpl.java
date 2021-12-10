package nl.tudelft.sem11b.reservation.services;

import java.net.URI;

import nl.tudelft.sem11b.clients.AuthenticatedServiceClient;
import nl.tudelft.sem11b.clients.UserClient;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AuthenticatedServiceClient<UserClient> implements UserService {
    public UserServiceImpl() {
        super(URI.create("http://localhost:8082/"), "User", UserClient::new);
    }

    @Override public UserModel currentUser() throws ApiException {
        return openClient().currentUser();
    }
}
