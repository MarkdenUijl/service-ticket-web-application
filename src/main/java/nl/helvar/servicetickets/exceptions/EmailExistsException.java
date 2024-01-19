package nl.helvar.servicetickets.exceptions;

public class EmailExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public EmailExistsException() {
        super();
    }
    public EmailExistsException(String message) {
        super(message);
    }
}