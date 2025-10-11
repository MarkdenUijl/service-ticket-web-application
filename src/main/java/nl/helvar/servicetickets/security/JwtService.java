package nl.helvar.servicetickets.security;

import io.jsonwebtoken.Claims;
import nl.helvar.servicetickets.users.User;
import nl.helvar.servicetickets.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final JwtCryptoUtil jwtCryptoUtil;
    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;

    public JwtService(JwtCryptoUtil jwtCryptoUtil, MyUserDetailsService myUserDetailsService, UserRepository userRepository) {
        this.jwtCryptoUtil = jwtCryptoUtil;
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = jwtCryptoUtil.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, Boolean tokenPersist) {
        Map<String, Object> claims = new HashMap<>();

        var roles = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .filter(name -> name.startsWith("ROLE_"))
                .toList();

        claims.put("roles", roles);

        if (userDetails instanceof MyUserDetails customDetails) {
            List<String> privileges = myUserDetailsService.extractPrivilegesFromUser(customDetails.getUser());
            claims.put("privileges", privileges);
        }

        long validPeriod = tokenPersist
                ? 1000L * 60 * 60 * 24 * 30
                : 1000 * 60 * 60 * 3;

        return jwtCryptoUtil.createToken(claims, userDetails.getUsername(), validPeriod);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}