package fish.api.service;

import fish.api.model.User;
import fish.api.model.Admin;
import fish.api.repository.UserRepository;
import fish.api.repository.AdminRepository;
import fish.api.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import fish.api.dto.RegisterRequest;
import fish.api.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<?> register(RegisterRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username is already taken");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email is already in use");
                return ResponseEntity.badRequest().body(error);
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            userRepository.save(user);

            String token = jwtTokenUtil.generateToken(user.getUsername(), "ROLE_USER");
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            logger.error("Error during registration", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    public ResponseEntity<?> login(LoginRequest request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Invalid username or password"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String token = jwtTokenUtil.generateToken(user.getUsername(), "ROLE_USER");
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            return ResponseEntity.ok(body);
        } catch (RuntimeException e) {
            logger.error("Invalid username or password", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            logger.error("Error during login", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    public ResponseEntity<?> registerAdmin(RegisterRequest request) {
        try {
            if (adminRepository.existsByUsername(request.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username is already taken");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
            if (adminRepository.existsByEmail(request.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email is already in use");
                return ResponseEntity.badRequest().body(error);
            }

            Admin admin = new Admin();
            admin.setUsername(request.getUsername());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setEmail(request.getEmail());
            adminRepository.save(admin);

            String token = jwtTokenUtil.generateToken(admin.getUsername(), "ROLE_ADMIN");
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            logger.error("Error during admin registration", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during admin registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    public ResponseEntity<?> loginAdmin(LoginRequest request) {
        try {
            Admin admin = adminRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Invalid username or password"));

            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String token = jwtTokenUtil.generateToken(admin.getUsername(), "ROLE_ADMIN");
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            return ResponseEntity.ok(body);
        } catch (RuntimeException e) {
            logger.error("Invalid username or password", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            logger.error("Error during admin login", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during admin login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
