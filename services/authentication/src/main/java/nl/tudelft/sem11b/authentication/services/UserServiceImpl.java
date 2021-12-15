package nl.tudelft.sem11b.authentication.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides a service which handles the authentication and registering of users.
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    /**
     * Finds a user by providing the netId/username as input.
     *
     * @param netId the username of a user which is used to identify a user in the system.
     * @return the data of a user if the user is found in the system.
     * @throws UsernameNotFoundException when no user exists with the provided username.
     */
    @Override
    public UserDetails loadUserByUsername(String netId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByNetId(netId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No such user exists.");
        }

        String password = user.get().getPassword();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.get().getRole()));

        return new org.springframework.security.core.userdetails.User(netId, password, authorities);
    }

    @Override
    public UserModel currentUser() throws ApiException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String netId = auth.getPrincipal().toString();
        Optional<User> user = userRepository.findUserByNetId(netId);
        if (user.isPresent()) {
            return user.get().toModel();
        } else {
            throw new ApiException("Authentication", "No user exists with this netId");
        }
    }

    /**
     * Saves the provided input in the userRepository.
     *
     * @param user contains data from a specific user of the system.
     */
    private void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public long addUser(String netId, String password, Roles role) throws InvalidData {
        if (netId == null || netId.isBlank() || password == null || password.isBlank()) {
            throw new InvalidData(
                "NetID and password must be given!");
        }
        if (userRepository.findUserByNetId(netId).isPresent()) {
            throw new InvalidData("User with this netId already exists.");
        }

        User newUser = new User(netId, role == null ? "employee" : role.toString(),
            passwordEncoder.encode(password));
        saveUser(newUser);

        return newUser.getId();
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll().stream().map(User::toModel).collect(Collectors.toList());
    }
}
