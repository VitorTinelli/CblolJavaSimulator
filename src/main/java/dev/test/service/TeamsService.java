package dev.test.service;

import java.util.List;
import java.util.UUID;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.TeamsRepository;
import dev.test.requestBody.TeamsReplaceRequestBody;
import dev.test.requestBody.TeamsSaveRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamsService {

  private final TeamsRepository teamsRepository;

  public List<Teams> listAll() {
    return teamsRepository.findAll();
  }

  public Teams findByName(String name) {
    return teamsRepository.findByName(name)
        .orElseThrow(() -> new BusinessException("Team not found"));
  }

  public Teams save(TeamsSaveRequestBody teams) {
    if (teamsRepository.findByName(teams.getName()).isPresent()) {
      throw new BusinessException("Team already exists");
    }
    return teamsRepository.save(Teams.builder()
        .name(teams.getName())
        .nicknames(teams.getNicknames())
        .championships(teams.getChampionships())
        .build());
  }

  public Teams findByIdOrThrowBusinessException(UUID id) {
    return teamsRepository.findById(id)
        .orElseThrow(() -> new BusinessException("Team not found"));
  }

  public void delete(UUID id) {
    teamsRepository.delete(findByIdOrThrowBusinessException(id));
  }

  public Teams replace(TeamsReplaceRequestBody team) {
    Teams savedTeam = findByIdOrThrowBusinessException(team.getId());
    if (team.getName().isBlank()) {
      team.setName(savedTeam.getName());
    }
    if (team.getNicknames().isBlank()) {
      team.setNicknames(savedTeam.getNicknames().toString().replace("[", "").replace("]", ""));
    }

    return teamsRepository.save(Teams.builder()
        .id(savedTeam.getId())
        .name(team.getName())
        .nicknames(team.getNicknames())
        .championships(team.getChampionships())
        .build());
  }

  public Teams increaseChampionships(UUID id) {
    Teams team = findByIdOrThrowBusinessException(id);
    team.setChampionships(team.getChampionships() + 1);
    return teamsRepository.save(team);
  }
}
