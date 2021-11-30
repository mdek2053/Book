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
        String username = auth.getPrincipal().toString();
        String role = auth.getAuthorities().toArray()[0].toString();
        saveUser(new User(username, role, 34));
        return userRepository.findUserByNetId(username);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
