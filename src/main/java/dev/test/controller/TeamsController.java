package dev.test.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import dev.test.domain.Teams;
import dev.test.requestBody.TeamsReplaceRequestBody;
import dev.test.requestBody.TeamsSaveRequestBody;
import dev.test.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamsController {

  private final TeamsService teamsService;

  @GetMapping
  public ResponseEntity<List<Teams>> listAll() {
    return ResponseEntity.ok(teamsService.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Teams> findByIdOrThrowBusinessException(@PathVariable UUID id) {
    return ResponseEntity.ok(teamsService.findByIdOrThrowBusinessException(id));
  }

  @GetMapping("find/{name}")
  public ResponseEntity<Teams> findByName(@PathVariable String name) {
    return ResponseEntity.ok(teamsService.findByName(name));
  }

  @PostMapping("/save")
  public ResponseEntity<Teams> save(@Valid @RequestBody TeamsSaveRequestBody teams) {
    return new ResponseEntity<>(teamsService.save(teams), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    teamsService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/replace")
  public ResponseEntity<Teams> replace(@Valid @RequestBody TeamsReplaceRequestBody team) {
    return ResponseEntity.ok(teamsService.replace(team));
  }

}
