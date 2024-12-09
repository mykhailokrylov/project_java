package fish.api.repository;

import fish.api.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
