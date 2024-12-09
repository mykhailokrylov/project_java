package fish.api.controller;

import fish.api.model.Fish;
import fish.api.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fish")
public class FishController {

    @Autowired
    private FishService fishService;

    @PostMapping
    public ResponseEntity<Fish> createFish(@Valid @RequestBody Fish fish) {
        Fish createdFish = fishService.createFish(fish);
        return ResponseEntity.ok(createdFish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fish> updateFish(@PathVariable Long id, @Valid @RequestBody Fish fishDetails) {
        Fish updatedFish = fishService.updateFish(id, fishDetails);
        return ResponseEntity.ok(updatedFish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFish(@PathVariable Long id) {
        fishService.deleteFish(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Fish> likeFish(@PathVariable Long id) {
        Fish likedFish = fishService.likeFish(id);
        return ResponseEntity.ok(likedFish);
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Fish> unlikeFish(@PathVariable Long id) {
        Fish unlikedFish = fishService.unlikeFish(id);
        return ResponseEntity.ok(unlikedFish);
    }
}
