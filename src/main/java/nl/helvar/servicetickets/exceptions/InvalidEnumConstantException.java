package nl.helvar.servicetickets.exceptions;

import java.util.Arrays;
import java.util.Enumeration;

public class InvalidEnumConstantException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Enum<?>[] enumerationValues;


    public InvalidEnumConstantException() {
        super();
    }
    public InvalidEnumConstantException(Enum<?>[] enumerationValues) {
        super();
        this.enumerationValues = enumerationValues;
    }

    @Override
    public String getMessage() {
        return "Invalid enumeration value passed, the only constants allowed are: " + Arrays.toString(enumerationValues);
    }
}