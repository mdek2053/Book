package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.authentication.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
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

    @PostMapping(value = "/")
    public Map<String, Object> postUser(@RequestBody User newUser){
        Map<String, Object> toBeReturned = new HashMap<>();

        try{
            service.addUser(newUser);
        } catch (Exception e){
            toBeReturned.put("success", "false");
            toBeReturned.put("message", e.getMessage());
            return toBeReturned;
        }

        toBeReturned.put("success", true);
        toBeReturned.put("message", "user has been added");
        return toBeReturned;
    }
}
