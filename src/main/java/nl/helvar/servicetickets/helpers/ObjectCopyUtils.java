package nl.helvar.servicetickets.helpers;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ObjectCopyUtils {
    public static void copyNonNullProperties(Object source, Object target) {
        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        java.beans.PropertyDescriptor[] propertyDescriptors = srcWrapper.getPropertyDescriptors();

        for (java.beans.PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();

            // Exclude the 'class' property
            if (!propertyName.equals("class")) {
                Object srcValue = srcWrapper.getPropertyValue(propertyName);

                // Only copy non-null values and non-zero numeric values
                if (srcValue != null && !(srcValue instanceof Number && ((Number) srcValue).intValue() == 0)) {
                    targetWrapper.setPropertyValue(propertyName, srcValue);
                }
            }
        }
    }
}
