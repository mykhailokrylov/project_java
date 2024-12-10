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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Service
public class FishService {
    private static final Logger logger = LoggerFactory.getLogger(FishService.class);

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
        logger.info("Attempting to create fish for user ID: {}", userId);

        try {
            // Get user and check suspension
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isEmpty()) {
                logger.error("User not found with ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found with id " + userId));
            }

            User user = userOptional.get();
            if (user.isSuspended()) {
                logger.warn("Suspended user {} attempted to create fish", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                        "error", "User is suspended",
                        "until", user.getSuspendedUntil().toString()
                    ));
            }

            if (fish == null) {
                logger.error("Fish data is null");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fish data must be provided"));
            }

            if (userId == null) {
                logger.error("User ID is null");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "User ID must be provided"));
            }

            // Validate fish data
            Set<ConstraintViolation<Fish>> violations = validator.validate(fish);
            if (!violations.isEmpty()) {
                logger.warn("Fish validation failed: {}", violations);
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Validation failed");
                response.put("violations", violations.stream()
                    .map(v -> Map.of(
                        "field", v.getPropertyPath().toString(),
                        "message", v.getMessage()
                    )).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(response);
            }

            fish.setUser(user);

            logger.debug("Saving fish: {}", fish);
            Fish createdFish = fishRepository.save(fish);
            logger.info("Successfully created fish with ID: {} for user ID: {}", createdFish.getId(), userId);

            return ResponseEntity.ok(createdFish);

        } catch (ConstraintViolationException e) {
            logger.error("Constraint violation while creating fish: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Validation failed");
            response.put("violations", e.getConstraintViolations().stream()
                .map(v -> Map.of(
                    "field", v.getPropertyPath().toString(),
                    "message", v.getMessage()
                )).collect(Collectors.toList()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Unexpected error while creating fish:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "An error occurred while creating the fish",
                    "message", e.getMessage()
                ));
        }
    }

    public ResponseEntity<?> updateFish(Long id, Fish fishDetails) {
        try {
            Optional<Fish> fishOptional = fishRepository.findById(id);
            if (fishOptional.isPresent()) {
                Fish fish = fishOptional.get();
                
                // Check if fish owner is suspended
                User owner = fish.getUser();
                if (owner != null && owner.isSuspended()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                            "error", "User is suspended",
                            "until", owner.getSuspendedUntil().toString()
                        ));
                }

                validateFish(fishDetails);
                fish.setName(fishDetails.getName());
                fish.setWeight(fishDetails.getWeight());
                fish.setLength(fishDetails.getLength());
                fish.setLocation(fishDetails.getLocation());
                Fish updatedFish = fishRepository.save(fish);
                return ResponseEntity.ok(updatedFish);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Fish not found with id " + id));
            }
        } catch (ConstraintViolationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Validation failed");
            response.put("violations", e.getConstraintViolations().stream()
                .map(v -> Map.of(
                    "field", v.getPropertyPath().toString(),
                    "message", v.getMessage()
                )).collect(Collectors.toList()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An error occurred while updating the fish"));
        }
    }

    public ResponseEntity<?> deleteFish(Long id) {
        try {
            if (!fishRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Fish not found with id " + id));
            }
            fishRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Fish deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An error occurred while deleting the fish"));
        }
    }

    public ResponseEntity<?> reactToFish(Long fishId, Long userId, String reactionType) {
        try {
            Optional<Fish> fishOptional = fishRepository.findById(fishId);
            if (fishOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Fish not found with id " + fishId));
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
                .body(Map.of("error", "An error occurred while processing the reaction"));
        }
    }

    public List<Fish> getAllFishes() {
        return fishRepository.findAll();
    }

    public Optional<Fish> getFishById(Long id) {
        return fishRepository.findById(id);
    }

    public List<Fish> getUserFishes(Long userId) {
        return fishRepository.findByUserId(userId);
    }

    public ResponseEntity<?> searchFishes(Double minWeight, Double maxWeight, Double minLength, Double maxLength) {
        try {
            // Set default values if parameters are null
            minWeight = (minWeight != null && minWeight > 0) ? minWeight : 0.0;
            maxWeight = (maxWeight != null && maxWeight > 0) ? maxWeight : Double.MAX_VALUE;
            minLength = (minLength != null && minLength > 0) ? minLength : 0.0;
            maxLength = (maxLength != null && maxLength > 0) ? maxLength : Double.MAX_VALUE;

            logger.debug("Searching with parameters: minWeight={}, maxWeight={}, minLength={}, maxLength={}", 
                minWeight, maxWeight, minLength, maxLength);

            // Check if min is greater than max
            if (minWeight > maxWeight || minLength > maxLength) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Minimum value cannot be greater than maximum value"));
            }

            List<Fish> fishes = fishRepository.searchFishes(minWeight, maxWeight, minLength, maxLength);
            
            Map<String, Object> response = new HashMap<>();
            response.put("fishes", fishes);
            response.put("count", fishes.size());
            response.put("filters", Map.of(
                "minWeight", minWeight,
                "maxWeight", maxWeight.equals(Double.MAX_VALUE) ? "unlimited" : maxWeight,
                "minLength", minLength,
                "maxLength", maxLength.equals(Double.MAX_VALUE) ? "unlimited" : maxLength
            ));
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while searching for fishes: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "An error occurred while searching for fishes",
                    "message", e.getMessage(),
                    "status", "error"
                ));
        }
    }

    private void validateFish(Fish fish) {
        Set<ConstraintViolation<Fish>> violations = validator.validate(fish);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
