package fish.api.controller;

import fish.api.model.User;
import fish.api.service.UserService;
import fish.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import fish.api.dto.UpdateProfileDTO;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;

    private boolean isAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return "ROLE_ADMIN".equals(jwtService.getRoleFromToken(token));
        }
        return false;
    }

    private String getAuthenticatedUsername(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("No authentication found");
        }
        return authentication.getName();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Optional<User> user = jwtService.getUserFromToken(authHeader);
        if (user.isPresent()) {
            if (isAdmin(request)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot access user profiles");
            }
            return ResponseEntity.ok(user.get());
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(Authentication authentication, @RequestBody UpdateProfileDTO updateDTO, HttpServletRequest request) {
        try {
            if (isAdmin(request)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot access user profiles");
            }
            String username = getAuthenticatedUsername(authentication);
            Optional<User> user = userService.getUserByUsername(username);
            return user.map(existingUser -> {
                User updatedUser = userService.updateUserProfile(existingUser.getId(), updateDTO);
                return ResponseEntity.ok(updatedUser);
            }).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMyProfile(Authentication authentication, HttpServletRequest request) {
        try {
            if (isAdmin(request)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot access user profiles");
            }
            String username = getAuthenticatedUsername(authentication);
            Optional<User> user = userService.getUserByUsername(username);
            if (user.isPresent()) {
                userService.deleteUser(user.get().getId());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id, HttpServletRequest request) {
        if (isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins cannot access user profiles");
        }
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
