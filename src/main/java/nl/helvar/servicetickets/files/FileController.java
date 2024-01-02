package nl.helvar.servicetickets.files;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketService;
import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/serviceTickets/{ticketId}/files")
public class FileController {
    private final FileService fileService;
    private final ServiceTicketService serviceTicketService;

    public FileController(FileService fileService, ServiceTicketService serviceTicketService) {
        this.fileService = fileService;
        this.serviceTicketService = serviceTicketService;
    }

    @GetMapping(value = "/{fileId}")
    public ResponseEntity<byte[]> getTicketFile(@PathVariable("ticketId") Long ticketId,
                                                @PathVariable("fileId") Long fileId
    ) throws IOException {
        ServiceTicket serviceTicket = serviceTicketService.findById(ticketId);

        if (serviceTicket.hasFileById(fileId)) {
            byte[] file = FileUtils.decompressFile(fileService.getFile(fileId).getData());

            Tika tika = new Tika();
            String contentType = tika.detect(file);

            HttpHeaders headers = new HttpHeaders();

            if (contentType != null) {
                headers.setContentType(MediaType.parseMediaType(contentType));
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            return new ResponseEntity<>(file, headers, HttpStatus.OK);
        } else {
            throw new RecordNotFoundException("Could not find any file with id '" + fileId + "' in serviceticket with id: " + ticketId + ".");
        }
    }


//    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<String> addFileToTicket(@RequestPart MultipartFile data,
//                                                   @PathVariable("ticketId") Long id
//    ) throws IOException {
//        FileCreationDTO file = new FileCreationDTO();
//
//        String name = data.getOriginalFilename();
//        byte[] compressedData = FileUtils.compressFile(data.getBytes());
//
//        file.setTicketId(id);
//        file.setName(name);
//        file.setData(compressedData);
//
//        return new ResponseEntity<>(fileService.storeFile(file), HttpStatus.CREATED);
//    }
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<String> addFilesToTicket(@RequestPart("files") List<MultipartFile> files,
                                               @PathVariable("ticketId") Long id) throws IOException {
    StringBuilder responseBuilder = new StringBuilder();

    for (MultipartFile data : files) {
        FileCreationDTO file = new FileCreationDTO();

        String name = data.getOriginalFilename();
        byte[] compressedData = FileUtils.compressFile(data.getBytes());

        file.setTicketId(id);
        file.setName(name);
        file.setData(compressedData);

        String savedFileResponse = fileService.storeFile(file);
        responseBuilder.append(savedFileResponse).append("\n");
    }

    return new ResponseEntity<>(responseBuilder.toString(), HttpStatus.CREATED);
}


    @DeleteMapping(value = "/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileId") Long id) {
        return new ResponseEntity<>(fileService.deleteFile(id), HttpStatus.OK);
    }
}
