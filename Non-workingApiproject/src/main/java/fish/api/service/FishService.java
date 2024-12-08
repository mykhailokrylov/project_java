package fish.api.service;

import fish.api.model.Fish;
import fish.api.repository.FishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

@Service
public class FishService {

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private Validator validator;

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
            return fishRepository.save(fish);
        } else {
            throw new RuntimeException("Fish not found with id " + id);
        }
    }

    public Fish unlikeFish(Long id) {
        Optional<Fish> fishOptional = fishRepository.findById(id);
        if (fishOptional.isPresent()) {
            Fish fish = fishOptional.get();
            fish.setLikes(fish.getLikes() - 1);
            return fishRepository.save(fish);
        } else {
            throw new RuntimeException("Fish not found with id " + id);
        }
    }

    private void validateFish(Fish fish) {
        Set<ConstraintViolation<Fish>> violations = validator.validate(fish);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
