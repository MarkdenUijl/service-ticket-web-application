package nl.helvar.servicetickets.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ExceptionController {

    private final Environment environment;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public ExceptionController(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> recordNotFoundException (RecordNotFoundException recordNotFoundException) {
        return new ResponseEntity<>(recordNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadObjectCreationException.class)
    public ResponseEntity<Object> badObjectCreationException (BadObjectCreationException badObjectCreationException) {
        return new ResponseEntity<>(badObjectCreationException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidEnumConstantException.class)
    public ResponseEntity<Object> invalidEnumConstantException (InvalidEnumConstantException invalidEnumConstantException) {
        return new ResponseEntity<>(invalidEnumConstantException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicateInDatabaseException.class)
    public ResponseEntity<Object> duplicateInDatabaseException (DuplicateInDatabaseException duplicateInDatabaseException) {
        return new ResponseEntity<>(duplicateInDatabaseException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> invalidRequestException (InvalidRequestException invalidRequestException) {
        return new ResponseEntity<>(invalidRequestException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = EmailExistsException.class)
    public ResponseEntity<Object> emailExistsException (EmailExistsException emailExistsException) {
        return new ResponseEntity<>(emailExistsException.getMessage(), HttpStatus.CONFLICT);
    }

    // Existing exceptions overridden.
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> maxSizeExceededException(MaxUploadSizeExceededException maxUploadSizeExceededException) {
        return new ResponseEntity<>("The file you are trying to upload is too large, it can not exceed " + maxFileSize + ".",
                HttpStatus.PAYLOAD_TOO_LARGE);
    }
}