package dev.test.controller;

import static org.mockito.Mockito.*;
import java.util.List;
import java.util.UUID;
import dev.test.domain.Matches;
import dev.test.domain.Teams;
import dev.test.service.MatchesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MatchesControllerTest {

  Matches matches;
  Teams teamA;
  Teams teamB;
  UUID id;
  @InjectMocks
  MatchesController matchesController;
  @Mock
  MatchesService matchesService;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    teamA = Teams.builder().id(id).name("Team A").build();
    teamB = Teams.builder().id(id).name("Team B").build();
    matches = Matches.builder().id(id).teamA(teamA).teamB(teamB).build();
  }

  @Test
  @DisplayName("List all matches")
  void listAll() {
    when(matchesService.listAll()).thenReturn(List.of(matches));
    ResponseEntity<List<Matches>> responseEntity = matchesController.listAll();
    Assertions.assertTrue(responseEntity.getBody().contains(matches));
    verify(matchesService).listAll();
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Find match by id")
  void findByIdOrThrowBusinessException() {
    when(matchesService.findByIdOrThrowBusinessException(id)).thenReturn(matches);
    ResponseEntity<Matches> responseEntity = matchesController.findByIdOrThrowBusinessException(id);
    Assertions.assertEquals(matches, responseEntity.getBody());
    verify(matchesService).findByIdOrThrowBusinessException(id);
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Find match by team A and team B")
  void findByTeamAAndTeamB() {
    when(matchesService.findByTeamAAndTeamB(id, id)).thenReturn(matches);
    ResponseEntity<Matches> responseEntity = matchesController.findByTeamAAndTeamB(id, id);
    Assertions.assertEquals(matches, responseEntity.getBody());
    verify(matchesService).findByTeamAAndTeamB(id, id);
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Create championship matches")
  void createChampionshipMatches() {
    when(matchesService.createChampionshipMatches()).thenReturn(List.of(matches));
    ResponseEntity<List<Matches>> responseEntity = matchesController.createChampionshipMatches();
    Assertions.assertTrue(responseEntity.getBody().contains(matches));
    verify(matchesService).createChampionshipMatches();
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Simulate matches by day")
  void simulateByDay() {
    when(matchesService.simulateByDay(1)).thenReturn(List.of(matches));
    matches.setWinner(teamA);
    ResponseEntity<List<Matches>> responseEntity = matchesController.simulateByDay(1);
    Assertions.assertTrue(responseEntity.getBody().contains(matches));
    Assertions.assertNotNull(responseEntity.getBody().get(0).getWinner());
    verify(matchesService).simulateByDay(1);
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Simulate matches by week")
  void simulateByWeek() {
    when(matchesService.simulateByWeek(1)).thenReturn(List.of(matches));
    matches.setWinner(teamA);
    ResponseEntity<List<Matches>> responseEntity = matchesController.simulateByWeek(1);
    Assertions.assertTrue(responseEntity.getBody().contains(matches));
    Assertions.assertNotNull(responseEntity.getBody().get(0).getWinner());
    verify(matchesService).simulateByWeek(1);
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Simulate all matches")
  void simulateAll() {
    when(matchesService.simulateAll()).thenReturn(List.of(matches));
    matches.setWinner(teamA);
    ResponseEntity<List<Matches>> responseEntity = matchesController.simulateAll();
    Assertions.assertTrue(responseEntity.getBody().contains(matches));
    Assertions.assertNotNull(responseEntity.getBody().get(0).getWinner());
    verify(matchesService).simulateAll();
    verifyNoMoreInteractions(matchesService);
  }

  @Test
  @DisplayName("Rematch")
  void rematch() {
    when(matchesService.rematch(id)).thenReturn(matches);
    matches.setWinner(teamA);
    matchesController.simulateAll();
    ResponseEntity<Matches> responseEntity = matchesController.rematch(id);
    Assertions.assertTrue(responseEntity.getBody().getWinner() != null);
  }

  @Test
  @DisplayName("Restart championship")
  void voidrestart() {
    matchesController.restart();
    verify(matchesService).restart();
    verifyNoMoreInteractions(matchesService);
  }
}
