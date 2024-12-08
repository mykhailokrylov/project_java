package fish.api.service;

import fish.api.model.User;
import fish.api.model.Fish;
import fish.api.model.Admin;
import fish.api.repository.UserRepository;
import fish.api.repository.FishRepository;
import fish.api.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void suspendUser(Long userId, @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime until) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User userToSuspend = user.get();
            userToSuspend.setSuspendedUntil(until);
            userRepository.save(userToSuspend);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void unsuspendUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User userToUnsuspend = user.get();
            userToUnsuspend.setSuspendedUntil(null);
            userRepository.save(userToUnsuspend);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void removeFish(Long fishId) {
        Optional<Fish> fish = fishRepository.findById(fishId);
        if (fish.isPresent()) {
            fishRepository.delete(fish.get());
        } else {
            throw new RuntimeException("Fish not found");
        }
    }

    public Admin createAdmin(Admin admin) {
        validateAdmin(admin);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public long adminCount() {
        return adminRepository.count();
    }

    private void validateAdmin(Admin admin) {
        if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (admin.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }
}
