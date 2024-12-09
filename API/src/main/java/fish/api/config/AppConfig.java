package fish.api.config;

import fish.api.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        String secret = "yourSecretKey"; // Replace with your actual secret key
        Long expiration = 86400000L; // Replace with your actual expiration time in milliseconds
        return new JwtTokenUtil(secret, expiration);
    }
}
