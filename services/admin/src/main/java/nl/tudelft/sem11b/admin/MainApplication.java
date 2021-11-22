package nl.tudelft.sem11b.admin;

import nl.tudelft.sem11b.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        var service = new Service("admin");
        SpringApplication.run(MainApplication.class, service.buildSpringArgs(args));
    }
}
