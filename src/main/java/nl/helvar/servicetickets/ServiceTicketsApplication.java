package nl.helvar.servicetickets;

import nl.helvar.servicetickets.files.FileCreationDTO;
import nl.helvar.servicetickets.files.FileService;
import nl.helvar.servicetickets.files.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class ServiceTicketsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTicketsApplication.class, args);
    }
}
