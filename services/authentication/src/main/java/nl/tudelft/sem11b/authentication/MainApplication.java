package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.Service;
import nl.tudelft.sem11b.authentication.entities.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        var service = new Service("authentication");
        SpringApplication.run(MainApplication.class, service.buildSpringArgs(args));
    }

    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            User admin = new User("SystemAdmin", "admin", "password");
            try{
                userService.addUser(admin);     // add admin if it doesn't exist in system already
            } catch (Exception e){
            }
        };
    }
}
