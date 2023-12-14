package nl.helvar.servicetickets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
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
}