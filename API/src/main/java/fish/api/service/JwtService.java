package fish.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fish.api.util.JwtTokenUtil;
import fish.api.model.User;

import java.util.Optional;

@Service
public class JwtService {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public JwtService(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    public Optional<User> getUserFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (validateToken(token)) {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                return userService.getUserByUsername(username);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        try {
            return jwtTokenUtil.validateToken(token);
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String generateToken(String username, String role) {
        User user = userService.getUserByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtTokenUtil.generateToken(username, role, user.getId());
    }

    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    public String getRoleFromToken(String token) {
        return jwtTokenUtil.getRoleFromToken(token);
    }

    public Long getUserIdFromToken(String token) {
        return jwtTokenUtil.getUserIdFromToken(token);
    }
}