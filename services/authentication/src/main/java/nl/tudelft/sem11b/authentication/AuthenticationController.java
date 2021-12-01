package nl.tudelft.sem11b.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping(value = "/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> postUser(@RequestBody Map<String, Object> requestPayload, HttpServletRequest request){

        if(!requestPayload.containsKey("netId") || !requestPayload.containsKey("password") || !requestPayload.containsKey("role")){
            Map<String, Object> toBeReturned = new HashMap<>();
            toBeReturned.put("success", "false");
            toBeReturned.put("message", "expected netId and password to be provided");

            return toBeReturned;
        }

        String netId;
        String password;
        String role;

        try {
            netId = (String) requestPayload.get("netId");
            password = (String) requestPayload.get("password");
            role = (String) requestPayload.get("role");
        } catch (Exception e) {
            Map<String, Object> toBeReturned = new HashMap<>();
            toBeReturned.put("success", "false");
            toBeReturned.put("message", e.getMessage());
            System.out.println(e.getMessage());
            return toBeReturned;
        }

        return service.addUser(netId, password, role);
    }
}
