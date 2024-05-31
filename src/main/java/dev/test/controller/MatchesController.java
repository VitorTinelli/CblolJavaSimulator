package dev.test.controller;

import java.util.List;
import java.util.UUID;
import dev.test.domain.MatchHistory;
import dev.test.domain.Matches;
import dev.test.service.MatchesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matches")
public class MatchesController {

  private final MatchesService matchesService;

  @GetMapping
  public ResponseEntity<List<Matches>> listAll() {
    return ResponseEntity.ok(matchesService.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Matches> findByIdOrThrowBusinessException(@PathVariable UUID id) {
    return ResponseEntity.ok(matchesService.findByIdOrThrowBusinessException(id));
  }

  @GetMapping("/find/{teamA}/{teamB}")
  public ResponseEntity<Matches> findByTeamAAndTeamB(@PathVariable UUID teamA,
      @PathVariable UUID teamB) {
    return ResponseEntity.ok(matchesService.findByTeamAAndTeamB(teamA, teamB));
  }

  @GetMapping("/create")
  public ResponseEntity<List<Matches>> createChampionshipMatches() {
    return ResponseEntity.ok(matchesService.createChampionshipMatches());
  }

  @PostMapping("simulate/day")
  public ResponseEntity<List<MatchHistory>> simulateByDay(@RequestParam int day) {
    return ResponseEntity.ok(matchesService.simulateByDay(day));
  }


}
