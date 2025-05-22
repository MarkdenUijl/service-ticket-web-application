package nl.helvar.servicetickets.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtCryptoUtil {

    private final JwtConfig jwtConfig;

    public JwtCryptoUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private Key getSigningKey() {
        String secretKey = jwtConfig.getSecretKey();

        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("jwt.secret.key is not set in env.properties");
        }

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Map<String, Object> claims, String subject, long validPeriodMillis) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + validPeriodMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}