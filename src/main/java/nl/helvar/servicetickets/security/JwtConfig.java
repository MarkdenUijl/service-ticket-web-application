package nl.helvar.servicetickets.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}