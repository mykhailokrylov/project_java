package fish.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fish.api.service.AuthService;
import fish.api.service.JwtService;
import fish.api.dto.RegisterRequest;
import fish.api.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Validated @RequestBody RegisterRequest request, @RequestParam String token) {
        if (!jwtService.getUsernameFromToken(token).equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }
        return authService.registerAdmin(request);
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@Validated @RequestBody LoginRequest request) {
        return authService.loginAdmin(request);
    }
}