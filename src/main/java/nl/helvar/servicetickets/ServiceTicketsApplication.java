package nl.helvar.servicetickets;

import nl.helvar.servicetickets.files.FileCreationDTO;
import nl.helvar.servicetickets.files.FileService;
import nl.helvar.servicetickets.files.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class ServiceTicketsApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceTicketsApplication.class, args);

        // Get the FileService bean from the application context
        FileService fileService = context.getBean(FileService.class);

        // Create an instance of ServiceTicketsApplication
        ServiceTicketsApplication app = new ServiceTicketsApplication(fileService);

        // Call initializeFileData method with the fileService as a parameter
        app.initializeFileData(fileService);
    }

    // LOADING FILE DATA INTO DATABASE:
    private final FileService fileService;

    public ServiceTicketsApplication(FileService fileService) {
        this.fileService = fileService;
    }

    public void initializeFileData(FileService fileService) {
        try {
            byte[] pngFileData = FileUtils.compressFile(Files.readAllBytes(Path.of("src/main/resourcefiles/sample png file.png")));
            byte[] pdfFileData = FileUtils.compressFile(Files.readAllBytes(Path.of("src/main/resourcefiles/sample pdf file.pdf")));
            byte[] mp4FileData = FileUtils.compressFile(Files.readAllBytes(Path.of("src/main/resourcefiles/sample mp4 file.mp4")));

            FileCreationDTO pngFileDto =  new FileCreationDTO();
                pngFileDto.setTicketId(1L);
                pngFileDto.setName("sample png file.png");
                pngFileDto.setData(pngFileData);
                fileService.storeFile(pngFileDto);

            FileCreationDTO pdfFileDto =  new FileCreationDTO();
                pdfFileDto.setTicketId(51L);
                pdfFileDto.setName("sample pdf file.pdf");
                pdfFileDto.setData(pdfFileData);
                fileService.storeFile(pdfFileDto);

            FileCreationDTO mp4FileDto =  new FileCreationDTO();
                mp4FileDto.setTicketId(101L);
                mp4FileDto.setName("sample mp4 file.mp4");
                mp4FileDto.setData(mp4FileData);
                fileService.storeFile(mp4FileDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
