package fish.api.controller;

import fish.api.model.Fish;
import fish.api.service.FishService;
import fish.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.FieldError;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fish")
public class FishController {

    private static final Logger logger = LoggerFactory.getLogger(FishController.class);

    @Autowired
    private FishService fishService;
    
    @Autowired
    private JwtService jwtService;

    private boolean isValidToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.validateToken(token);
        }
        return false;
    }

    private boolean isAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return "ROLE_ADMIN".equals(jwtService.getRoleFromToken(token));
        }
        return false;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createFish(@Valid @RequestBody Fish fish, HttpServletRequest request) {
        logger.info("Received fish creation request");

        if (!isValidToken(request)) {
            logger.warn("Invalid token in fish creation request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Valid token required"));
        }

        if (isAdmin(request)) {
            logger.warn("Admin attempted to create fish");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Admins cannot create fish"));
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Valid authorization header required"));
        }

        try {
            String token = authHeader.substring(7);
            Long userId = jwtService.getUserIdFromToken(token);
            
            // Set minimum required fields if not provided
            if (fish.getName() == null) fish.setName("Unnamed Fish");
            if (fish.getWeight() <= 0) fish.setWeight(0.1);
            if (fish.getLength() <= 0) fish.setLength(0.1);
            if (fish.getLocation() == null) fish.setLocation("Unknown Location");

            logger.info("Processing fish creation for user ID: {}", userId);
            return fishService.createFish(fish, userId);
        } catch (Exception e) {
            logger.error("Error processing fish creation request:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error processing request: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateFish(@PathVariable Long id, @Valid @RequestBody Fish fishDetails, HttpServletRequest request) {
        if (isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot update fish");
        }
        return fishService.updateFish(id, fishDetails);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteFish(@PathVariable Long id) {
        return fishService.deleteFish(id);
    }

    @PostMapping(value = "/{id}/react", produces = "application/json")
    public ResponseEntity<?> reactToFish(
            @PathVariable Long id,
            @RequestParam String reactionType,
            HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Valid token required"));
        }
        if (isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Admins cannot react to fish"));
        }
        if (!reactionType.equals("LIKE") && !reactionType.equals("DISLIKE")) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid reaction type"));
        }
        
        Long userId = jwtService.getUserIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        return fishService.reactToFish(id, userId, reactionType);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getFishById(@PathVariable Long id, HttpServletRequest request) {
        if (!isValidToken(request)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Valid token required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            Optional<Fish> fish = fishService.getFishById(id);
            if (fish.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("fish", fish.get());
                response.put("status", "success");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Fish not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch fish");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllFishes(HttpServletRequest request) {
        logger.info("Received request for all fishes");
        
        if (!isValidToken(request)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Valid token required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            List<Fish> fishes = fishService.getAllFishes();
            Map<String, Object> response = new HashMap<>();
            response.put("fishes", fishes);
            response.put("count", fishes.size());
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch fishes");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?> getUserFishes(HttpServletRequest request) {
        logger.info("Received request for user's fishes");
        
        if (!isValidToken(request)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Valid token required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            Long userId = jwtService.getUserIdFromToken(token);
            List<Fish> userFishes = fishService.getUserFishes(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("fishes", userFishes);
            response.put("count", userFishes.size());
            response.put("userId", userId);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch user fishes");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(value = "/user/{userId}", produces = "application/json")
    public ResponseEntity<?> getUserFishesById(@PathVariable Long userId, HttpServletRequest request) {
        logger.info("Received request for user's fishes with ID: {}", userId);
        
        if (!isValidToken(request)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Valid token required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            List<Fish> userFishes = fishService.getUserFishes(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("fishes", userFishes);
            response.put("count", userFishes.size());
            response.put("userId", userId);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching fishes for user {}: {}", userId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch user fishes");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<?> searchFishes(
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight,
            @RequestParam(required = false) Double minLength,
            @RequestParam(required = false) Double maxLength,
            HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Valid token required");
        }
        return fishService.searchFishes(minWeight, maxWeight, minLength, maxLength);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");
        response.put("violations", ex.getBindingResult().getAllErrors().stream()
            .map(error -> {
                Map<String, String> violation = new HashMap<>();
                violation.put("field", ((FieldError) error).getField());
                violation.put("message", error.getDefaultMessage());
                return violation;
            })
            .collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
