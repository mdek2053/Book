package nl.tudelft.sem11b.authentication;

import nl.tudelft.sem11b.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//This exclude line is here temporarily, so we dont have to set up a database yet
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
public class MainApplication {
    public static void main(String[] args) {
        var service = new Service("authentication");
        SpringApplication.run(MainApplication.class, args);
    }
}
