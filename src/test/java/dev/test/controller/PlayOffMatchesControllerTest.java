package dev.test.controller;

import static org.mockito.Mockito.*;
import java.util.List;
import dev.test.domain.PlayOffMatches;
import dev.test.service.PlayOffMatchesService;
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
class PlayOffMatchesControllerTest {

  PlayOffMatches semi;
  PlayOffMatches aFinal;

  @InjectMocks
  PlayOffMatchesController playOffMatchesController;

  @Mock
  PlayOffMatchesService playOffMatchesService;

  @BeforeEach
  void setUp() {
    semi = PlayOffMatches.builder().phase("Semi-Final").build();
    aFinal = PlayOffMatches.builder().phase("Final").build();
  }

  @Test
  @DisplayName("List all play-off matches")
  void listAll() {
    when(playOffMatchesService.listAll()).thenReturn(List.of(semi, aFinal));
    ResponseEntity<List<PlayOffMatches>> responseEntity = playOffMatchesController.listAll();
    Assertions.assertTrue(responseEntity.getBody().contains(semi));
    Assertions.assertTrue(responseEntity.getBody().contains(aFinal));
    verify(playOffMatchesService).listAll();
    verifyNoMoreInteractions(playOffMatchesService);
  }

  @Test
  @DisplayName("Create play-off matches")
  void createPlayOffMatches() {
    when(playOffMatchesService.createPlayOffMatches()).thenReturn(List.of(semi, aFinal));
    ResponseEntity<List<PlayOffMatches>> responseEntity = playOffMatchesController.createPlayOffMatches();
    Assertions.assertTrue(responseEntity.getBody().contains(semi));
    Assertions.assertTrue(responseEntity.getBody().contains(aFinal));
    verify(playOffMatchesService).createPlayOffMatches();
    verifyNoMoreInteractions(playOffMatchesService);
  }

  @Test
  @DisplayName("Simulate semi-finals")
  void simulateSemis() {
    when(playOffMatchesService.simulateSemis()).thenReturn(List.of(semi));
    ResponseEntity<List<PlayOffMatches>> responseEntity = playOffMatchesController.simulateSemis();
    Assertions.assertTrue(responseEntity.getBody().contains(semi));
    verify(playOffMatchesService).simulateSemis();
    verifyNoMoreInteractions(playOffMatchesService);
  }

  @Test
  @DisplayName("Simulate final")
  void simulateFinal() {
    when(playOffMatchesService.simulateFinal()).thenReturn(aFinal);
    ResponseEntity<PlayOffMatches> responseEntity = playOffMatchesController.simulateFinal();
    Assertions.assertEquals(aFinal, responseEntity.getBody());
    verify(playOffMatchesService).simulateFinal();
    verifyNoMoreInteractions(playOffMatchesService);
  }
}
