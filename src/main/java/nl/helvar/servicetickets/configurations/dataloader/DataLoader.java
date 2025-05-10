package nl.helvar.servicetickets.configurations.dataloader;

import nl.helvar.servicetickets.files.FileCreationDTO;
import nl.helvar.servicetickets.files.FileService;
import nl.helvar.servicetickets.files.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class DataLoader implements InitializingBean {

    private final FileService fileService;

    public DataLoader(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void afterPropertiesSet() {
        initializeFileData();
    }

    private void initializeFileData() {
        try {
            byte[] pngFileData = FileUtils.compressFile(readResourceAsBytes("resourcefiles/sample png file.png"));
            byte[] pdfFileData = FileUtils.compressFile(readResourceAsBytes("resourcefiles/sample pdf file.pdf"));
            byte[] mp4FileData = FileUtils.compressFile(readResourceAsBytes("resourcefiles/sample mp4 file.mp4"));

            FileCreationDTO pngFileDto = new FileCreationDTO();
            pngFileDto.setTicketId(1L);
            pngFileDto.setName("sample png file.png");
            pngFileDto.setData(pngFileData);
            fileService.storeFile(pngFileDto);

            FileCreationDTO pdfFileDto = new FileCreationDTO();
            pdfFileDto.setTicketId(51L);
            pdfFileDto.setName("sample pdf file.pdf");
            pdfFileDto.setData(pdfFileData);
            fileService.storeFile(pdfFileDto);

            FileCreationDTO mp4FileDto = new FileCreationDTO();
            mp4FileDto.setTicketId(101L);
            mp4FileDto.setName("sample mp4 file.mp4");
            mp4FileDto.setData(mp4FileData);
            fileService.storeFile(mp4FileDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readResourceAsBytes(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }
}
