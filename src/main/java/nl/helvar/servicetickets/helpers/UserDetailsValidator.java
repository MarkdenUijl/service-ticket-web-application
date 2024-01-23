package nl.helvar.servicetickets.helpers;

import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsValidator {
    public static boolean hasPrivilege(String privilege, UserDetails userDetails) {
        return userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(privilege));
    }
}
