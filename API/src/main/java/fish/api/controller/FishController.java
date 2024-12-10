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

@RestController
@RequestMapping("/api/fish")
public class FishController {

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
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Valid token required");
        }
        if (isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot create fish");
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        Long userId = jwtService.getUserIdFromToken(token);
        return fishService.createFish(fish, userId);
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Valid token required");
        }
        if (isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot react to fish");
        }
        if (!reactionType.equals("LIKE") && !reactionType.equals("DISLIKE")) {
            return ResponseEntity.badRequest().body("Invalid reaction type");
        }
        
        Long userId = jwtService.getUserIdFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        return fishService.reactToFish(id, userId, reactionType);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getFishById(@PathVariable Long id, HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Valid token required");
        }
        return fishService.getFishById(id);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllFishes(HttpServletRequest request) {
        if (!isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Valid token required");
        }
        return fishService.getAllFishes();
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
