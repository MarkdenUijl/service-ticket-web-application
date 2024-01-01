package nl.helvar.servicetickets.servicetickets;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import nl.helvar.servicetickets.images.FileUtils;
import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.zip.DataFormatException;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;
import static nl.helvar.servicetickets.helpers.UriCreator.createUri;

@RestController
@RequestMapping("/serviceTickets")
public class ServiceTicketController {
    private final ServiceTicketService service;

    public ServiceTicketController(ServiceTicketService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceTicketDTO>> getAllServiceTickets(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        List<ServiceTicketDTO> serviceTicketDTOS = service.getAllServiceTickets(type, status);

        return new ResponseEntity<>(serviceTicketDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTicketDTO> findServiceTicketById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/file")
    public ResponseEntity<byte[]> getTicketFile(@PathVariable("id") Long id) throws IOException, DataFormatException {
        byte[] file = FileUtils.decompressFile(service.getTicketFile(id));

        Tika tika = new Tika();
        String contentType = tika.detect(file);

        HttpHeaders headers = new HttpHeaders();

        if (contentType != null) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ServiceTicketCreationDTO> addServiceTicket(@Valid @RequestPart ServiceTicketCreationDTO serviceTicket,
                                                                     @RequestPart(required = false) MultipartFile file,
                                                                     BindingResult br
    ) throws IOException {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            if (file != null) {
                byte[] fileToByte = FileUtils.compressFile(file.getBytes());
                serviceTicket.setFile(fileToByte);
            }

            serviceTicket = service.createServiceTicket(serviceTicket);

            URI uri = createUri(serviceTicket);

            return ResponseEntity.created(uri).body(serviceTicket);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTicketDTO> replaceServiceTicket(@PathVariable("id") Long id, @RequestBody ServiceTicketCreationDTO newServiceTicket) {
        return new ResponseEntity<>(service.replaceServiceTicket(id, newServiceTicket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceTicket(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.deleteServiceTicket(id), HttpStatus.OK);
    }
}
