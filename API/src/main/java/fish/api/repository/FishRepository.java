package fish.api.repository;

import fish.api.model.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FishRepository extends JpaRepository<Fish, Long> {
    @Query("SELECT f FROM Fish f WHERE f.user.id = :userId")
    List<Fish> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f FROM Fish f WHERE " +
           "f.weight >= :minWeight AND " +
           "f.weight <= :maxWeight AND " +
           "f.length >= :minLength AND " +
           "f.length <= :maxLength " +
           "ORDER BY f.id DESC")
    List<Fish> searchFishes(
        @Param("minWeight") Double minWeight,
        @Param("maxWeight") Double maxWeight,
        @Param("minLength") Double minLength,
        @Param("maxLength") Double maxLength
    );
}
