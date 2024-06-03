package dev.test.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import dev.test.domain.PlayOffMatches;
import dev.test.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayOffMatchesRepository extends JpaRepository<PlayOffMatches, UUID> {

  Optional<List<PlayOffMatches>> findByPhase(String phase);

  Optional<PlayOffMatches> findByPhaseAndWinner(String phase, Teams winner);
}
