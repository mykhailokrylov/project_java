package fish.api.service;

import fish.api.model.Fish;
import fish.api.repository.FishRepository;
import fish.api.model.FishReaction;
import fish.api.repository.FishReactionRepository;
import fish.api.model.User;
import fish.api.service.UserService;
//import fish.api.notifications.NotificationService; // Import your email service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FishService {

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private NotificationService emailNotificationService;

    @Autowired
    private FishReactionRepository fishReactionRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<?> createFish(Fish fish, Long userId) {
        try {
            validateFish(fish);
            User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            fish.setUser(user);
            Fish createdFish = fishRepository.save(fish);
            return ResponseEntity.ok(createdFish);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getConstraintViolations());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the fish");
        }
    }

    public ResponseEntity<?> updateFish(Long id, Fish fishDetails) {
        try {
            validateFish(fishDetails);
            Optional<Fish> fishOptional = fishRepository.findById(id);
            if (fishOptional.isPresent()) {
                Fish fish = fishOptional.get();
                fish.setName(fishDetails.getName());
                fish.setWeight(fishDetails.getWeight());
                fish.setLength(fishDetails.getLength());
                fish.setLocation(fishDetails.getLocation());
                Fish updatedFish = fishRepository.save(fish);
                return ResponseEntity.ok(updatedFish);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fish not found with id " + id);
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getConstraintViolations());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the fish");
        }
    }

    public ResponseEntity<?> deleteFish(Long id) {
        try {
            fishRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the fish");
        }
    }

    public ResponseEntity<?> reactToFish(Long fishId, Long userId, String reactionType) {
        try {
            Optional<Fish> fishOptional = fishRepository.findById(fishId);
            if (fishOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fish not found with id " + fishId);
            }

            Fish fish = fishOptional.get();
            Optional<FishReaction> existingReaction = fishReactionRepository
                    .findByFishIdAndUserId(fishId, userId);

            if (existingReaction.isPresent()) {
                FishReaction reaction = existingReaction.get();
                if (reaction.getReactionType().equals(reactionType)) {
                    // Remove reaction if it's the same type (unlike/undislike)
                    fishReactionRepository.delete(reaction);
                    fish.getReactions().remove(reaction);
                } else {
                    // Update reaction type if different
                    reaction.setReactionType(reactionType);
                    fishReactionRepository.save(reaction);
                }
            } else {
                // Create new reaction
                FishReaction reaction = new FishReaction();
                reaction.setFish(fish);
                reaction.setUserId(userId);
                reaction.setReactionType(reactionType);
                FishReaction savedReaction = fishReactionRepository.save(reaction);
                fish.getReactions().add(savedReaction);
            }

            Fish updatedFish = fishRepository.save(fish);
            return ResponseEntity.ok(updatedFish);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the reaction");
        }
    }

    public ResponseEntity<?> getAllFishes() {
        try {
            List<Fish> fishes = fishRepository.findAll();
            return ResponseEntity.ok(fishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fishes");
        }
    }

    public ResponseEntity<?> getFishById(Long id) {
        try {
            Optional<Fish> fishOptional = fishRepository.findById(id);
            if (fishOptional.isPresent()) {
                return ResponseEntity.ok(fishOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fish not found with id " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fish");
        }
    }

    public ResponseEntity<?> searchFishes(Double minWeight, Double maxWeight, Double minLength, Double maxLength) {
        try {
            List<Fish> fishes = fishRepository.findAll().stream()
                    .filter(fish -> (minWeight == null || fish.getWeight() >= minWeight) &&
                                    (maxWeight == null || fish.getWeight() <= maxWeight) &&
                                    (minLength == null || fish.getLength() >= minLength) &&
                                    (maxLength == null || fish.getLength() <= maxLength))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fishes");
        }
    }

    private void validateFish(Fish fish) {
        Set<ConstraintViolation<Fish>> violations = validator.validate(fish);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
