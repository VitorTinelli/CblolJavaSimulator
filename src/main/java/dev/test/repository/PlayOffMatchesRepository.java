package dev.test.repository;

import java.util.List;
import java.util.UUID;
import dev.test.domain.PlayOffMatches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayOffMatchesRepository extends JpaRepository<PlayOffMatches, UUID> {

  List<PlayOffMatches> findByPhaseSemi(String phase);

  PlayOffMatches findByPhaseFinal(String phase);
}
