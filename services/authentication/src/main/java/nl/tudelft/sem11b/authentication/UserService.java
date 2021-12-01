package nl.tudelft.sem11b.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
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
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    /**
     * Finds a user by providing the netId/username as input.
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

    /**
     * Gets the data of the user which currently uses the system.
     * @return an object of type User of the current user.
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String netId = auth.getPrincipal().toString();

        return userRepository.findUserByNetId(netId).get();
    }

    /**
     * Saves the provided input in the userRepository.
     * @param user contains data from a specific user of the system.
     */
    private void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Adds a user to the system.
     * @param user contains data from a specific user of the system.
     * @return object of type User which contains the data after it is saved in the userRepository.
     * @throws InvalidCredentialsException when the credentials are not valid or
     *      when the user already exists in the system.
     */
    public User addUser(User user) throws InvalidCredentialsException {
        if (user.getNetId() == null || user.getPassword() == null || user.getRole() == null) {
            throw new InvalidCredentialsException(
                    "Expected netId, password and role to be provided");
        }
        if (userRepository.findUserByNetId(user.getNetId()).isPresent()) {
            throw new InvalidCredentialsException("User with this netId already exists.");
        }

        User newUser = new User(user.getNetId(), user.getRole(),
                passwordEncoder.encode(user.getPassword()));
        saveUser(newUser);
        return newUser;
    }
}
