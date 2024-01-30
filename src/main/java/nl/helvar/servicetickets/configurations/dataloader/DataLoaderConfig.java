package nl.helvar.servicetickets.configurations.dataloader;

import nl.helvar.servicetickets.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    private final FileService fileService;

    @Autowired
    public DataLoaderConfig(FileService fileService) {
        this.fileService = fileService;
    }

    @Bean
    @DependsOnDatabaseInitialization
    public DataLoader dataLoader() {
        return new DataLoader(fileService);
    }
}