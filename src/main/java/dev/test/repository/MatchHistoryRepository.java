package dev.test.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import dev.test.domain.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchHistoryRepository extends JpaRepository<MatchHistory, UUID> {

  Optional<List<MatchHistory>> findByDay(int day);
}
