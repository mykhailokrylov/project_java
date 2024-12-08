package fish.api.service;

import org.springframework.stereotype.Service;
import fish.api.model.User;

@Service
public class NotificationService {

    public void sendNotification(User user, String message) {
        // Implement notification logic (email, SMS, etc.)
        System.out.println("Sending notification to " + user.getUsername() + ": " + message);
    }
}