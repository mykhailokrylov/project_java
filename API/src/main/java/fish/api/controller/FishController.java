package fish.api.controller;

import fish.api.model.Fish;
import fish.api.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.FieldError;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fish")
public class FishController {

    @Autowired
    private FishService fishService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createFish(@Valid @RequestBody Fish fish) {
        return fishService.createFish(fish);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateFish(@PathVariable Long id, @Valid @RequestBody Fish fishDetails) {
        return fishService.updateFish(id, fishDetails);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteFish(@PathVariable Long id) {
        return fishService.deleteFish(id);
    }

    @PostMapping(value = "/{id}/like", produces = "application/json")
    public ResponseEntity<?> likeFish(@PathVariable Long id) {
        return fishService.likeFish(id);
    }

    @PostMapping(value = "/{id}/unlike", produces = "application/json")
    public ResponseEntity<?> unlikeFish(@PathVariable Long id) {
        return fishService.unlikeFish(id);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getFishById(@PathVariable Long id) {
        return fishService.getFishById(id);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllFishes() {
        return fishService.getAllFishes();
    }

    @GetMapping(value = "/weight/greater/{weight}", produces = "application/json")
    public ResponseEntity<?> getFishesByWeightGreaterThan(@PathVariable double weight) {
        return fishService.getFishesByWeightGreaterThan(weight);
    }

    @GetMapping(value = "/weight/less/{weight}", produces = "application/json")
    public ResponseEntity<?> getFishesByWeightLessThan(@PathVariable double weight) {
        return fishService.getFishesByWeightLessThan(weight);
    }

    @GetMapping(value = "/length/greater/{length}", produces = "application/json")
    public ResponseEntity<?> getFishesByLengthGreaterThan(@PathVariable double length) {
        return fishService.getFishesByLengthGreaterThan(length);
    }

    @GetMapping(value = "/length/less/{length}", produces = "application/json")
    public ResponseEntity<?> getFishesByLengthLessThan(@PathVariable double length) {
        return fishService.getFishesByLengthLessThan(length);
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<?> searchFishes(
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight,
            @RequestParam(required = false) Double minLength,
            @RequestParam(required = false) Double maxLength) {
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
