package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.Service;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.models.UserModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Contains the main application of this microservice which is run when the system is started.
 */
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        var service = new Service("authentication");
        SpringApplication.run(MainApplication.class, service.buildSpringArgs(args));
    }

    @Bean
    CommandLineRunner run(UserServiceImpl userService) {
        return args -> {
            try {
                userService.addUser("SystemAdmin", "password", Roles.Admin);     // add admin if it doesn't exist in system already
            } catch (Exception e) {
                System.out.println("Admin already exists.");
            }
        };
    }
}
