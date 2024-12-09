package fish.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fish.api.service.AuthService;
import fish.api.service.JwtService;
import fish.api.dto.RegisterRequest;
import fish.api.dto.LoginRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.FieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request) {
        try {
            return authService.register(request);
        } catch (Exception e) {
            logger.error("Error during registration", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) {
        try {
            return authService.login(request);
        } catch (Exception e) {
            logger.error("Error during login", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/register/admin", produces = "application/json")
    public ResponseEntity<?> registerAdmin(@Validated @RequestBody RegisterRequest request, @RequestParam String token) {
        try {
            if (!jwtService.getUsernameFromToken(token).equals("ROLE_ADMIN")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Unauthorized");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            return authService.registerAdmin(request);
        } catch (Exception e) {
            logger.error("Error during admin registration", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during admin registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/login/admin", produces = "application/json")
    public ResponseEntity<?> loginAdmin(@Validated @RequestBody LoginRequest request) {
        try {
            return authService.loginAdmin(request);
        } catch (Exception e) {
            logger.error("Error during admin login", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during admin login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}