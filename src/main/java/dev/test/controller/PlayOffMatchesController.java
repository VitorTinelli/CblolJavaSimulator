package dev.test.controller;

import java.util.List;
import dev.test.domain.PlayOffMatches;
import dev.test.service.PlayOffMatchesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playoff")
@RequiredArgsConstructor
public class PlayOffMatchesController {

  private final PlayOffMatchesService playOffMatchesService;

  @GetMapping
  public ResponseEntity<List<PlayOffMatches>> listAll() {
    return ResponseEntity.ok(playOffMatchesService.listAll());
  }

  @PostMapping("/create")
  public ResponseEntity<List<PlayOffMatches>> createPlayOffMatches() {
    return ResponseEntity.ok(playOffMatchesService.createPlayOffMatches());
  }

  @PutMapping("/simulate/semis")
  public ResponseEntity<List<PlayOffMatches>> simulateSemis() {
    return ResponseEntity.ok(playOffMatchesService.simulateSemis());
  }

  @PutMapping("/simulate/final")
  public ResponseEntity<PlayOffMatches> simulateFinal() {
    return ResponseEntity.ok(playOffMatchesService.simulateFinal());
  }


}
