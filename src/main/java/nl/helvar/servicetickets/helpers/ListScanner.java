package nl.helvar.servicetickets.helpers;

import java.lang.reflect.Field;
import java.util.List;

public class ListScanner {
    public static boolean checkListForPropertyValue(List<?> list, String propertyName) {
        for (Object obj : list) {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(propertyName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
