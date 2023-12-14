package nl.helvar.servicetickets.helpers;

import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;

public class EnumValidator {
    public static Enum<?> getEnumConstantFromString(Enum<?>[] enumerationValues, String value) {
        value = value.toUpperCase();

        for (Enum<?> constant : enumerationValues) {
            if (constant.name().equals(value)) {
                return constant;
            }
        }

        throw new InvalidEnumConstantException(enumerationValues);
    }
}
