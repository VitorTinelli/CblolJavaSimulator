package dev.test.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import dev.test.domain.Matches;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.MatchesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchesServiceTest {

  Matches match;
  Matches match2;
  Teams team1;
  Teams team2;
  List<Teams> teamsList;
  List<Matches> matchesList;

  @InjectMocks
  MatchesService matchesService;

  @Mock
  MatchesRepository matchesRepository;

  @Mock
  TeamsService teamsService;

  @BeforeEach
  void setUp() {
    match = Matches.builder().winner(null).build();
    match2 = Matches.builder().winner(null).build();
    team1 = Teams.builder().name("Team 1").build();
    team2 = Teams.builder().name("Team 2").build();
    teamsList = Arrays.asList(team1, team2);
    matchesList = Arrays.asList(match, match2);
  }

  @Test
  @DisplayName("Listall returns match list when successful")
  void listAll_ReturnsAllMatches_WhenSuccessful() {
    when(matchesRepository.findAll()).thenReturn(List.of(match));
    Assertions.assertFalse(matchesRepository.findAll().isEmpty());
    verify(matchesRepository).findAll();
  }

  @Test
  void findById_ReturnMatch_WhenSuccessful() {
    when(matchesRepository.findById(match.getId())).thenReturn(Optional.of(match));
    Assertions.assertEquals(match, matchesService.findByIdOrThrowBusinessException(match.getId()));
    verify(matchesRepository).findById(match.getId());
  }

  @Test
  void findById_ThrowBusinessException_WhenMatchDoesNotExist() {
    when(matchesRepository.findById(match.getId())).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class,
        () -> matchesService.findByIdOrThrowBusinessException(match.getId()));
    verify(matchesRepository).findById(match.getId());
  }

  @Test
  void createChampionshipMatches_ReturnsListOfMatches_WhenSuccessful() {
    when(teamsService.listAll()).thenReturn(teamsList);
    when(matchesRepository.findAll()).thenReturn(Collections.emptyList(), matchesList);
    when(matchesRepository.save(any(Matches.class))).thenAnswer(i -> i.getArguments()[0]);

    List<Matches> result = matchesService.createChampionshipMatches();
    Assertions.assertEquals(matchesList, result);
  }
}
