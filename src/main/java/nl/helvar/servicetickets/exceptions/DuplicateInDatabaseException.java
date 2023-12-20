package nl.helvar.servicetickets.exceptions;

public class DuplicateInDatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public DuplicateInDatabaseException() {
        super();
    }
    public DuplicateInDatabaseException(String message) {
        super(message);
    }
}