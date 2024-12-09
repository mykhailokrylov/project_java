package fish.api.service;

import fish.api.model.Fish;
import fish.api.repository.FishRepository;
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

    public Fish createFish(Fish fish) {
        validateFish(fish);
        return fishRepository.save(fish);
    }

    public Fish updateFish(Long id, Fish fishDetails) {
        validateFish(fishDetails);
        Optional<Fish> fishOptional = fishRepository.findById(id);
        if (fishOptional.isPresent()) {
            Fish fish = fishOptional.get();
            fish.setName(fishDetails.getName());
            fish.setWeight(fishDetails.getWeight());
            fish.setLength(fishDetails.getLength());
            fish.setLocation(fishDetails.getLocation());
            return fishRepository.save(fish);
        } else {
            throw new RuntimeException("Fish not found with id " + id);
        }
    }

    public void deleteFish(Long id) {
        fishRepository.deleteById(id);
    }

    public Fish likeFish(Long id) {
        Optional<Fish> fishOptional = fishRepository.findById(id);
        if (fishOptional.isPresent()) {
            Fish fish = fishOptional.get();
            fish.setLikes(fish.getLikes() + 1);
            Fish updatedFish = fishRepository.save(fish);

            // Send notification
            emailNotificationService.sendEmail(
                    "admin@example.com", // Replace with actual recipient logic if needed
                    "Fish Liked",
                    "The fish '" + fish.getName() + "' has been liked. It now has " + fish.getLikes() + " likes."
            );

            return updatedFish;
        } else {
            throw new RuntimeException("Fish not found with id " + id);
        }
    }

    public Fish unlikeFish(Long id) {
        Optional<Fish> fishOptional = fishRepository.findById(id);
        if (fishOptional.isPresent()) {
            Fish fish = fishOptional.get();
            fish.setLikes(fish.getLikes() - 1);
            Fish updatedFish = fishRepository.save(fish);

            // Send notification
            emailNotificationService.sendEmail(
                    "admin@example.com", // Replace with actual recipient logic if needed
                    "Fish Unliked",
                    "The fish '" + fish.getName() + "' has been unliked. It now has " + fish.getLikes() + " likes."
            );

            return updatedFish;
        } else {
            throw new RuntimeException("Fish not found with id " + id);
    public ResponseEntity<?> createFish(Fish fish) {
        try {
            validateFish(fish);
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

    public ResponseEntity<?> likeFish(Long id) {
        try {
            Optional<Fish> fishOptional = fishRepository.findById(id);
            if (fishOptional.isPresent()) {
                Fish fish = fishOptional.get();
                fish.setLikes(fish.getLikes() + 1);
                Fish likedFish = fishRepository.save(fish);
                return ResponseEntity.ok(likedFish);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fish not found with id " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while liking the fish");
        }
    }

    public ResponseEntity<?> unlikeFish(Long id) {
        try {
            Optional<Fish> fishOptional = fishRepository.findById(id);
            if (fishOptional.isPresent()) {
                Fish fish = fishOptional.get();
                fish.setLikes(fish.getLikes() - 1);
                Fish unlikedFish = fishRepository.save(fish);
                return ResponseEntity.ok(unlikedFish);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fish not found with id " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while unliking the fish");
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

    public ResponseEntity<?> getFishesByWeightGreaterThan(double weight) {
        try {
            List<Fish> fishes = fishRepository.findAll().stream()
                    .filter(fish -> fish.getWeight() > weight)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fishes");
        }
    }

    public ResponseEntity<?> getFishesByWeightLessThan(double weight) {
        try {
            List<Fish> fishes = fishRepository.findAll().stream()
                    .filter(fish -> fish.getWeight() < weight)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fishes");
        }
    }

    public ResponseEntity<?> getFishesByLengthGreaterThan(double length) {
        try {
            List<Fish> fishes = fishRepository.findAll().stream()
                    .filter(fish -> fish.getLength() > length)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fishes");
        }
    }

    public ResponseEntity<?> getFishesByLengthLessThan(double length) {
        try {
            List<Fish> fishes = fishRepository.findAll().stream()
                    .filter(fish -> fish.getLength() < length)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fishes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the fishes");
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
