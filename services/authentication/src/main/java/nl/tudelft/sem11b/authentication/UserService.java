package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        String password = passwordEncoder.encode("password");
        System.out.println("password = " + password + " " + username);
        return new org.springframework.security.core.userdetails.User(username, password, authorities);
    }

    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String netId = auth.getPrincipal().toString();
        String role = auth.getAuthorities().toArray()[0].toString();
        saveUser(new User(netId, role, 34));
        return userRepository.findUserByNetId(netId).get();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public Map<String, Object> addUser(String netId, String password, String role) {
        if(userRepository.findUserByNetId(netId).isPresent()){
            Map<String, Object> toBeReturned = new HashMap<>();
            toBeReturned.put("success", false);
            toBeReturned.put("message", "netId already exists");
            return toBeReturned;
        }

        saveUser(new User(netId, role, 1));
        Map<String, Object> toBeReturned = new HashMap<>();
        toBeReturned.put("success", true);
        toBeReturned.put("message", "user has been added");
        return toBeReturned;
    }
}
