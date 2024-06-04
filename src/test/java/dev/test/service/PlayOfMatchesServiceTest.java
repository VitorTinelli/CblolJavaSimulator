package dev.test.service;

import static org.mockito.Mockito.*;
import java.util.*;
import dev.test.domain.Matches;
import dev.test.domain.PlayOffMatches;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.PlayOffMatchesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayOfMatchesServiceTest {

  PlayOffMatches finalMatch;
  Teams team;
  Teams team2;
  Teams team3;
  Teams team4;
  Matches match;
  PlayOffMatches semi1;
  PlayOffMatches semi2;
  UUID id;

  @InjectMocks
  PlayOffMatchesService playOfMatchesService;

  @Mock
  PlayOffMatchesRepository playOffMatchesRepository;

  @Mock
  MatchesService matchesService;

  @Mock
  TeamsService teamsService;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    semi1 = PlayOffMatches.builder().phase("semi1-Final").teamA(null).teamB(null).winner(null)
        .build();
    semi2 = PlayOffMatches.builder().phase("semi1-Final").teamA(null).teamB(null).winner(null)
        .build();
    team = Teams.builder().name("team").build();
    team2 = Teams.builder().name("team2").build();
    finalMatch = PlayOffMatches.builder().phase("Final").teamA(team).teamB(team2).winner(null)
        .build();
    team3 = Teams.builder().name("team3").build();
    team4 = Teams.builder().name("team4").build();
    match = Matches.builder().winner(team).build();
  }

  @Test
  @DisplayName("List all returns list of playoff matches when successful")
  void listAll_ReturnsListOfPOMatches_WhenSuccessful() {
    when(playOffMatchesRepository.findAll()).thenReturn(List.of(semi1, finalMatch));
    List<PlayOffMatches> matches = playOfMatchesService.listAll();
    Assertions.assertTrue(matches.contains(semi1));
    Assertions.assertTrue(matches.contains(finalMatch));
    verify(playOffMatchesRepository).findAll();
    verifyNoMoreInteractions(playOffMatchesRepository);
  }

  @Test
  @DisplayName("Find by id returns playoff match when successful")
  void findById_ReturnsPOMatch_WhenSuccessful() {
    when(playOffMatchesRepository.findById(semi1.getId())).thenReturn(java.util.Optional.of(semi1));
    PlayOffMatches match = playOfMatchesService.findByIdOrThrowBusinessException(semi1.getId());
    Assertions.assertEquals(semi1, match);
    verify(playOffMatchesRepository).findById(semi1.getId());
    verifyNoMoreInteractions(playOffMatchesRepository);
  }

  @Test
  @DisplayName("Find by id throws exception when playoff match not found")
  void findById_ThrowsException_WhenPOMatchNotFound() {
    when(playOffMatchesRepository.findById(semi1.getId())).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class,
        () -> playOfMatchesService.findByIdOrThrowBusinessException(semi1.getId()));
    verify(playOffMatchesRepository).findById(semi1.getId());
    verifyNoMoreInteractions(playOffMatchesRepository);
  }

  @Test
  @DisplayName("Create play off matches, returns a list of play off matches when successful")
  void createPlayOffMatches_ReturnsListOfPlayOffMatches_WhenSuccessful() {
    when(matchesService.listAll()).thenReturn(List.of(match));
    when(teamsService.listAll()).thenReturn(List.of(team, team2, team3, team4));
    when(playOffMatchesRepository.findAll()).thenReturn(
        Collections.emptyList(), List.of(semi1, semi2, finalMatch));
    when(playOffMatchesRepository.save(any(PlayOffMatches.class))).thenReturn(semi1, semi2,
        finalMatch);

    List<PlayOffMatches> playOffMatchesList = playOfMatchesService.createPlayOffMatches();
    Assertions.assertEquals(List.of(semi1, semi2, finalMatch), playOffMatchesList);
    verify(matchesService).listAll();
    verify(teamsService).listAll();
    verify(playOffMatchesRepository, times(2)).findAll();
    verify(playOffMatchesRepository, times(3)).save(any(PlayOffMatches.class));
  }

  @Test
  @DisplayName("Create play off matches throws exception when matches already created")
  void createPlayOffMatches_ThrowsException_WhenMatchesAlreadyCreated() {
    when(playOffMatchesRepository.findAll()).thenReturn(List.of(semi1, semi2, finalMatch));
    Assertions.assertThrows(BusinessException.class,
        () -> playOfMatchesService.createPlayOffMatches());
    verify(playOffMatchesRepository).findAll();
  }

  @Test
  @DisplayName("Create play off matches throws exception when point phase not finished yet")
  void createPlayOffMatches_ThrowsException_WhenPointPhaseNotFinishedYet() {
    Matches match = Matches.builder().winner(null).build();
    when(matchesService.listAll()).thenReturn(List.of(match));
    Assertions.assertThrows(BusinessException.class,
        () -> playOfMatchesService.createPlayOffMatches());
    verify(matchesService).listAll();
  }

  @Test
  void simulateSemis_ReturnsListOfPlayOffMatches_WhenSuccessful() {
    List<PlayOffMatches> semis = Arrays.asList(semi1, semi2, finalMatch);

    when(playOffMatchesRepository.findByPhase(any())).thenReturn(Optional.of(semis));
    when(playOffMatchesRepository.findByPhaseAndWinner(any(), any())).thenReturn(
        Optional.of(finalMatch));
    when(playOffMatchesRepository.saveAll(any(List.class))).thenAnswer(i -> i.getArguments()[0]);

    List<PlayOffMatches> result = playOfMatchesService.simulateSemis();

    Assertions.assertEquals(semis, result);
  }

  @Test
  @DisplayName("Simulate semis throws exception when semis already simulated")
  void simulateSemis_ThrowsException_WhenSemisAlreadySimulated() {
    List<PlayOffMatches> semis = Arrays.asList(semi1, semi2, finalMatch);
    semi1.setWinner(team);
    when(playOffMatchesRepository.findByPhase(any())).thenReturn(Optional.of(semis));
    Assertions.assertThrows(BusinessException.class, () -> playOfMatchesService.simulateSemis());
    verify(playOffMatchesRepository).findByPhase(any());
  }

  @Test
  @DisplayName("Simulate semis throws exception when matches not created")
  void simulateSemis_ThrowsException_WhenMatchesNotCreated() {
    when(playOffMatchesRepository.findByPhase(any())).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class, () -> playOfMatchesService.simulateSemis());
    verify(playOffMatchesRepository).findByPhase(any());
  }

  @Test
  @DisplayName("Simulate semis throws exception when final match not created")
  void simulateSemis_ThrowsException_WhenFinalMatchNotCreated() {
    List<PlayOffMatches> semis = Arrays.asList(semi1, semi2);
    when(playOffMatchesRepository.findByPhase(any())).thenReturn(Optional.of(semis));
    when(playOffMatchesRepository.findByPhaseAndWinner(any(), any())).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class, () -> playOfMatchesService.simulateSemis());
    verify(playOffMatchesRepository).findByPhase(any());
    verify(playOffMatchesRepository).findByPhaseAndWinner(any(), any());
  }

  @Test
  @DisplayName("Simulate final match, returns a the finalMatch when successful")
  void simulateFinalMatch_ReturnsFinalMatch_WhenSuccessful() {
    when(playOffMatchesRepository.findByPhaseAndWinner(any(), any())).thenReturn(
        Optional.of(finalMatch));
    when(playOffMatchesRepository.save(any(PlayOffMatches.class))).thenReturn(finalMatch);
    PlayOffMatches result = playOfMatchesService.simulateFinal();
    Assertions.assertEquals(finalMatch, result);
    verify(playOffMatchesRepository).findByPhaseAndWinner(any(), any());
    verify(playOffMatchesRepository).save(any(PlayOffMatches.class));
  }

  @Test
  @DisplayName("Simulate final match throws exception when final match not created")
  void simulateFinalMatch_ThrowsException_WhenFinalMatchNotCreated() {
    when(playOffMatchesRepository.findByPhaseAndWinner(any(), any())).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class, () -> playOfMatchesService.simulateFinal());
    verify(playOffMatchesRepository).findByPhaseAndWinner(any(), any());
  }

  @Test
  @DisplayName("Simulate final match throws exception when semis not finished yet")
  void simulateFinalMatch_ThrowsException_WhenSemisNotFinishedYet() {
    finalMatch.setTeamA(null);
    finalMatch.setTeamB(null);
    when(playOffMatchesRepository.findByPhaseAndWinner(any(), any())).thenReturn(
        Optional.of(finalMatch));
    Assertions.assertThrows(BusinessException.class, () -> playOfMatchesService.simulateFinal());
    verify(playOffMatchesRepository).findByPhaseAndWinner(any(), any());
  }

}
