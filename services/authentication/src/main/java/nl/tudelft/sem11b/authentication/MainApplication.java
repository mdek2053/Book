package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        var service = new Service("authentication");
        SpringApplication.run(MainApplication.class, service.buildSpringArgs(args));
    }
}
