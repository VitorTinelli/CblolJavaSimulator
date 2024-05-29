package dev.test.repository;

import java.util.Optional;
import java.util.UUID;
import dev.test.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, UUID> {
  Optional<Teams> findByName(String name);
  Optional<Teams> findByNicknames(String nickname);
}
