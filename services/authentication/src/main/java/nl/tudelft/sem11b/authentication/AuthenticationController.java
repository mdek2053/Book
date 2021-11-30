package nl.tudelft.sem11b.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    UserService service;

    @GetMapping("/test")
    public String test(){
        return "<h1> TEST </h1>";
    }

    @GetMapping("/me")
    public User me(){
        return service.getCurrentUser();
    }
}
