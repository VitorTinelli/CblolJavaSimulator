package dev.test.service;

import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.TeamsRepository;
import dev.test.requestBody.TeamsReplaceRequestBody;
import dev.test.requestBody.TeamsSaveRequestBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeamsServiceTest {

  UUID id;
  Teams teams;
  TeamsSaveRequestBody teamsSaveRequestBody;
  TeamsReplaceRequestBody teamsReplaceRequestBody;

  @InjectMocks
  TeamsService teamsService;

  @Mock
  TeamsRepository teamsRepository;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    teams = Teams.builder().id(id).championships(1).name("Oi").nicknames("Tchau").build();
    teamsSaveRequestBody = TeamsSaveRequestBody.builder().championships(1).name("Oi")
        .nicknames("Tchau").build();
    teamsReplaceRequestBody = TeamsReplaceRequestBody.builder().championships(1).name("Oi")
        .nicknames("Tchau").build();
  }

  @Test
  @DisplayName("List all teams")
  void listAll() {
    when(teamsRepository.findAll()).thenReturn(List.of(teams));
    List<Teams> teamsList = teamsService.listAll();
    Assertions.assertTrue(teamsList.contains(teams));
    verify(teamsRepository).findAll();
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Find team by id when successful")
  void findById_ReturnsTeams_WhenSuccessful() {
    when(teamsRepository.findById(id)).thenReturn(java.util.Optional.of(teams));
    Teams team = teamsService.findByIdOrThrowBusinessException(id);
    Assertions.assertEquals(teams, team);
    verify(teamsRepository).findById(id);
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Find team by id when throw BExpetion team is not found")
  void findById_ThrowBusinessException_WhenTeamIsNotFound() {
    when(teamsRepository.findById(id)).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class,
        () -> teamsService.findByIdOrThrowBusinessException(id));
    verify(teamsRepository).findById(id);
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Find team by name returns a team when successful")
  void findByName_ReturnsTeams_WhenSuccessful() {
    when(teamsRepository.findByName("Oi")).thenReturn(Optional.of(teams));
    Teams team = teamsService.findByName("Oi");
    Assertions.assertEquals(teams, team);
    verify(teamsRepository).findByName("Oi");
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Find team by name throws BExpetion team is not found")
  void findByName_ThrowBusinessException_WhenTeamIsNotFound() {
    when(teamsRepository.findByName("Oi")).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class,
        () -> teamsService.findByName("Oi"));
    verify(teamsRepository).findByName("Oi");
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Create team, returns a team when successful")
  void create_ReturnsTeams_WhenSuccessful() {
    when(teamsRepository.save(any(Teams.class))).thenReturn(teams);
    Teams team = teamsService.save(teamsSaveRequestBody);
    Assertions.assertEquals(teams, team);
  }

  @Test
  @DisplayName("Create team, throws BExpetion when team already exists")
  void create_ThrowBusinessException_WhenTeamAlreadyExists() {
    when(teamsRepository.findByName("Oi")).thenReturn(Optional.of(teams));
    Assertions.assertThrows(BusinessException.class,
        () -> teamsService.save(teamsSaveRequestBody));
    verify(teamsRepository).findByName("Oi");
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Delete team when successful")
  void delete() {
    when(teamsRepository.findById(id)).thenReturn(Optional.of(teams));
    teamsService.delete(id);
    verify(teamsRepository).delete(teams);
  }

  @Test
  @DisplayName("Delete throw BExpetion when team is not found")
  void delete_ThrowBusinessException_WhenTeamIsNotFound() {
    when(teamsRepository.findById(id)).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class,
        () -> teamsService.delete(id));
    verify(teamsRepository).findById(id);
    verifyNoMoreInteractions(teamsRepository);
  }

  @Test
  @DisplayName("Replace team, returns a team when successful")
  void replace_ReturnsTeams_WhenSuccessful() {
    when(teamsRepository.findById(any())).thenReturn(Optional.of(teams));
    when(teamsRepository.save(any(Teams.class))).thenReturn(teams);
    Teams team = teamsService.replace(teamsReplaceRequestBody);
    Assertions.assertEquals(teams, team);
  }

  @Test
  @DisplayName("Replace team, throws BExpetion when team is not found")
  void replace_ThrowBusinessException_WhenTeamIsNotFound() {
    when(teamsRepository.findById(any())).thenReturn(Optional.empty());
    Assertions.assertThrows(BusinessException.class,
        () -> teamsService.replace(teamsReplaceRequestBody));
    verify(teamsRepository).findById(any());
  }

  @Test
  @DisplayName("Replace team, returns a team when name with same name and nicknames when is blank")
  void replace_ReturnsTeams_WhenNameAndNicknamesIsBlank() {
    when(teamsRepository.findById(any())).thenReturn(Optional.of(teams));
    when(teamsRepository.save(any(Teams.class))).thenReturn(teams);
    teamsReplaceRequestBody.setName("");
    teamsReplaceRequestBody.setNicknames("");
    Teams team = teamsService.replace(teamsReplaceRequestBody);
    Assertions.assertEquals(teams, team);
  }

  @Test
  @DisplayName("Increase championships, returns a team when successful")
  void increaseChampionships_ReturnsTeams_WhenSuccessful() {
    when(teamsRepository.findById(id)).thenReturn(Optional.of(teams));
    when(teamsRepository.save(any(Teams.class))).thenReturn(teams);
    Teams team = teamsService.increaseChampionships(id);
    Assertions.assertEquals(teams, team);
  }
}
