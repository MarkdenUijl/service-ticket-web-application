package nl.helvar.servicetickets.exceptions;

public class BadObjectCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public BadObjectCreationException() {
        super();
    }
    public BadObjectCreationException(String message) {
        super(message);
    }
}