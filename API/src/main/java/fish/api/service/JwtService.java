package fish.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import fish.api.util.JwtTokenUtil;
import fish.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JwtService {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public JwtService(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    public String generateToken(String username, String role) {
        User user = userService.getUserByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtTokenUtil.generateToken(username, role, user.getId());
    }

    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenUtil.validateToken(token);
    }

    public String getRoleFromToken(String token) {
        return jwtTokenUtil.getRoleFromToken(token);
    }

    public Long getUserIdFromToken(String token) {
        return jwtTokenUtil.getUserIdFromToken(token);
    }
}