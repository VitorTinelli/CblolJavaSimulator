package dev.test.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import dev.test.domain.Matches;
import dev.test.domain.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchesRepository extends JpaRepository<Matches, UUID> {
  Optional<Matches> findByTeamAAndTeamB(Teams teamA, Teams teamB);

  Optional<List<Matches>> findByDay(int day);
}
