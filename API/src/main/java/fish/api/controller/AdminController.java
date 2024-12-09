package fish.api.controller;

import fish.api.model.Admin;
import fish.api.model.User;
import fish.api.model.Fish;
import fish.api.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-default-admin")
    public ResponseEntity<String> createDefaultAdmin() {
        if (adminService.adminCount() == 0) {
            Admin admin = new Admin("admin", "12345678", "s97813@pollub.edu.pl");
            adminService.createAdmin(admin);
            return ResponseEntity.ok("Default admin created");
        } else {
            return ResponseEntity.ok("Admin already exists");
        }
    }

    @PostMapping("/suspend-user/{userId}")
    public ResponseEntity<String> suspendUser(@PathVariable Long userId, @RequestBody @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime until) {
        adminService.suspendUser(userId, until);
        return ResponseEntity.ok("User suspended until " + until);
    }

    @PostMapping("/unsuspend-user/{userId}")
    public ResponseEntity<String> unsuspendUser(@PathVariable Long userId) {
        adminService.unsuspendUser(userId);
        return ResponseEntity.ok("User unsuspended");
    }

    @DeleteMapping("/remove-fish/{fishId}")
    public ResponseEntity<String> removeFish(@PathVariable Long fishId) {
        adminService.removeFish(fishId);
        return ResponseEntity.ok("Fish removed");
    }

}
