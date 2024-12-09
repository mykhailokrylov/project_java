package fish.api.controller;

import fish.api.model.Admin;
import fish.api.model.User;
import fish.api.model.Fish;
import fish.api.service.AdminService;
import fish.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
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

    @PostMapping("/create-default-admin")
    public ResponseEntity<?> createDefaultAdmin(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        if (adminService.adminCount() == 0) {
            Admin admin = new Admin("admin", "12345678", "s97813@pollub.edu.pl");
            adminService.createAdmin(admin);
            return ResponseEntity.ok("Default admin created");
        } else {
            return ResponseEntity.ok("Admin already exists");
        }
    }

    @PostMapping("/suspend-user/{userId}")
    public ResponseEntity<String> suspendUser(@PathVariable Long userId, @RequestBody @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime until, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        adminService.suspendUser(userId, until);
        return ResponseEntity.ok("User suspended until " + until);
    }

    @PostMapping("/unsuspend-user/{userId}")
    public ResponseEntity<String> unsuspendUser(@PathVariable Long userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        adminService.unsuspendUser(userId);
        return ResponseEntity.ok("User unsuspended");
    }

    @DeleteMapping("/remove-fish/{fishId}")
    public ResponseEntity<String> removeFish(@PathVariable Long fishId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        adminService.removeFish(fishId);
        return ResponseEntity.ok("Fish removed");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        String username = jwtService.getUsernameFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        return adminService.getAdminByUsername(username)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestBody Admin adminDetails, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        String username = jwtService.getUsernameFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        return adminService.updateAdmin(username, adminDetails)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllAdmins(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
        return adminService.getAdminById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
