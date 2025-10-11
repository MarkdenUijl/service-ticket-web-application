package nl.helvar.servicetickets.security;

import nl.helvar.servicetickets.privileges.Privilege;
import nl.helvar.servicetickets.roles.Role;
import nl.helvar.servicetickets.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private final User user;
    private final List<GrantedAuthority> authorities;

    public MyUserDetails(User user) {
        this.user = user;
        this.authorities = buildAuthorities(user);
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static List<GrantedAuthority> buildAuthorities(User user) {
        List<String> names = new ArrayList<>();
        for (Role role : user.getRoles()) {
            // include role name (typically starts with ROLE_)
            names.add(role.getName());
            // include all privilege names
            for (Privilege p : role.getPrivileges()) {
                names.add(p.getName());
            }
        }
        List<GrantedAuthority> list = new ArrayList<>(names.size());
        for (String n : names) {
            list.add(new SimpleGrantedAuthority(n));
        }
        return list;
    }
}