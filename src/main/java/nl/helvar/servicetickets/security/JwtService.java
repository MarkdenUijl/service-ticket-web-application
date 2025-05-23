package nl.helvar.servicetickets.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final JwtCryptoUtil jwtCryptoUtil;

    public JwtService(JwtCryptoUtil jwtCryptoUtil) {
        this.jwtCryptoUtil = jwtCryptoUtil;
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
        long validPeriod = tokenPersist ? 1000L * 60 * 60 * 24 * 30 : 1000 * 60 * 60 * 3;
        return jwtCryptoUtil.createToken(claims, userDetails.getUsername(), validPeriod);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}