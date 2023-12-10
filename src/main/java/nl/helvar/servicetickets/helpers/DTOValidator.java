package nl.helvar.servicetickets.helpers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class DTOValidator {
    public String buildErrorMessage(BindingResult br) {
        StringBuilder sb = new StringBuilder();

        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField());
            sb.append(" : ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }

        return sb.toString();
    }
}
