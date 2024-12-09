package fish.api.repository;

import fish.api.model.FishReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FishReactionRepository extends JpaRepository<FishReaction, Long> {
    Optional<FishReaction> findByFishIdAndUserId(Long fishId, Long userId);
}
