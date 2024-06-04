package dev.test.controller;

import static org.mockito.Mockito.*;
import java.util.List;
import dev.test.domain.Teams;
import dev.test.requestBody.TeamsReplaceRequestBody;
import dev.test.requestBody.TeamsSaveRequestBody;
import dev.test.service.TeamsService;
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
public class TeamsTestControllerTest {

  Teams teams;
  TeamsSaveRequestBody teamsSaveRequestBody;
  TeamsReplaceRequestBody teamsReplaceRequestBody;

  @InjectMocks
  TeamsController teamsController;
  @Mock
  TeamsService teamsService;

  @BeforeEach
  void setUp() {
    teams = Teams.builder().name("Team A").build();
    teamsSaveRequestBody = TeamsSaveRequestBody.builder().name("Team A").build();
    teamsReplaceRequestBody = TeamsReplaceRequestBody.builder().name("Team A").build();
  }

  @Test
  @DisplayName("List all teams")
  void listAll() {
    when(teamsService.listAll()).thenReturn(List.of(teams));
    ResponseEntity<List<Teams>> responseEntity = teamsController.listAll();
    Assertions.assertTrue(responseEntity.getBody().contains(teams));
    verify(teamsService).listAll();
    verifyNoMoreInteractions(teamsService);
  }

  @Test
  @DisplayName("Find team by id")
  void findByIdOrThrowBusinessException() {
    when(teamsService.findByIdOrThrowBusinessException(teams.getId())).thenReturn(teams);
    ResponseEntity<Teams> responseEntity = teamsController.findByIdOrThrowBusinessException(
        teams.getId());
    Assertions.assertEquals(teams, responseEntity.getBody());
    verify(teamsService).findByIdOrThrowBusinessException(teams.getId());
    verifyNoMoreInteractions(teamsService);
  }

  @Test
  @DisplayName("Find team by name")
  void findByName() {
    when(teamsService.findByName(teams.getName())).thenReturn(teams);
    ResponseEntity<Teams> responseEntity = teamsController.findByName(teams.getName());
    Assertions.assertEquals(teams, responseEntity.getBody());
    verify(teamsService).findByName(teams.getName());
    verifyNoMoreInteractions(teamsService);
  }

  @Test
  @DisplayName("Post team")
  void save() {
    when(teamsService.save(teamsSaveRequestBody)).thenReturn(teams);
    ResponseEntity<Teams> responseEntity = teamsController.save(teamsSaveRequestBody);
    Assertions.assertEquals(teams, responseEntity.getBody());
    verify(teamsService).save(teamsSaveRequestBody);
    verifyNoMoreInteractions(teamsService);
  }

  @Test
  @DisplayName("Delete team")
  void delete() {
    teamsController.delete(teams.getId());
    verify(teamsService).delete(teams.getId());
    verifyNoMoreInteractions(teamsService);
  }

  @Test
  @DisplayName("Replace team")
  void replace() {
    when(teamsService.replace(teamsReplaceRequestBody)).thenReturn(teams);
    ResponseEntity<Teams> responseEntity = teamsController.replace(teamsReplaceRequestBody);
    Assertions.assertEquals(teams, responseEntity.getBody());
    verify(teamsService).replace(teamsReplaceRequestBody);
    verifyNoMoreInteractions(teamsService);
  }

}
