package nl.tudelft.sem11b.authentication;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String netId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByNetId(netId);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("No such user exists.");
        }

        String password = user.get().getPassword();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.get().getRole()));

        return new org.springframework.security.core.userdetails.User(netId, password, authorities);
    }

    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String netId = auth.getPrincipal().toString();

        return userRepository.findUserByNetId(netId).get();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User addUser(User user) throws InvalidCredentialsException{
        if(user.getNetId() == null || user.getPassword() == null || user.getRole() == null){
            throw new InvalidCredentialsException("Expected netId, password and role to be provided");
        }
        if(userRepository.findUserByNetId(user.getNetId()).isPresent()){
            throw new InvalidCredentialsException("User with this netId already exists.");
        }

        User newUser = new User(user.getNetId(), user.getRole(), passwordEncoder.encode(user.getPassword()));
        saveUser(newUser);
        return newUser;
    }
}
