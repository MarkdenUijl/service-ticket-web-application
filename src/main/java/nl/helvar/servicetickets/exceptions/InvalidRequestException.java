package nl.helvar.servicetickets.exceptions;

public class InvalidRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidRequestException() {
        super();
    }
    public InvalidRequestException(String message) {
        super(message);
    }
}