package nl.helvar.servicetickets.exceptions;

public class JwtTokenExpiredException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public JwtTokenExpiredException() {
        super();
    }
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}