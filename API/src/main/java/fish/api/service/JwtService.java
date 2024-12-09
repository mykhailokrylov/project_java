package fish.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import fish.api.util.JwtTokenUtil;

@Service
public class JwtService {

    private final JwtTokenUtil jwtTokenUtil;

    public JwtService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String generateToken(String username, String role) {
        return jwtTokenUtil.generateToken(username, role);
    }

    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenUtil.validateToken(token);
    }
}