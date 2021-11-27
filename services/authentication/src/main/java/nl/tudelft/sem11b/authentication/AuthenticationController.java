package nl.tudelft.sem11b.authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @GetMapping("/test")
    public String test(){
        return "<h1> TEST </h1>";
    }
}
