package dev.test.service;

import java.util.*;
import dev.test.domain.Matches;
import dev.test.domain.PlayOffMatches;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.PlayOffMatchesRepository;
import dev.test.utils.SimulateMatches;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayOffMatchesService {

  private final PlayOffMatchesRepository playOffMatchesRepository;
  private final MatchesService matchesService;
  private final TeamsService teamsService;
  private final Map<Teams, Integer> map = new HashMap<>();
  private Teams primeiro;
  private Teams segundo;
  private Teams terceiro;
  private Teams quarto;

  public List<PlayOffMatches> listAll() {
    return playOffMatchesRepository.findAll();
  }

  public PlayOffMatches findByIdOrThrowBusinessException(UUID id) {
    return playOffMatchesRepository.findById(id)
        .orElseThrow(() -> new BusinessException("Match not found"));
  }

  public List<PlayOffMatches> createPlayOffMatches() {
    List<Matches> matches = matchesService.listAll();
    List<Teams> teams = teamsService.listAll();
    if (!playOffMatchesRepository.findAll().isEmpty()) {
      throw new BusinessException("Matches already created");
    }
    if (matches.stream().anyMatch(match -> match.getWinner() == null)) {
      throw new BusinessException("Point phase not finished yet");
    }
    teams.forEach(team -> map.put(team, 0));
    matches.forEach(match -> {
      if (match.getWinner() != null) {
        map.put(match.getWinner(), map.get(match.getWinner()) + 1);
      }
    });
    primeiro = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
    map.remove(primeiro);
    segundo = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
    map.remove(segundo);
    terceiro = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
    map.remove(terceiro);
    quarto = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();

    PlayOffMatches semi1 = PlayOffMatches.builder()
        .teamA(primeiro)
        .teamB(quarto)
        .phase("Semi-Final")
        .build();
    playOffMatchesRepository.save(semi1);
    PlayOffMatches semi2 = PlayOffMatches.builder()
        .teamA(segundo)
        .teamB(terceiro)
        .phase("Semi-Final")
        .build();
    playOffMatchesRepository.save(semi2);
    PlayOffMatches finalMatch = PlayOffMatches.builder()
        .teamA(null)
        .teamB(null)
        .phase("Final")
        .build();
    playOffMatchesRepository.save(finalMatch);
    return playOffMatchesRepository.findAll();
  }

  public List<PlayOffMatches> simulateSemis() {
    if (playOffMatchesRepository.findAll().isEmpty()) {
      throw new BusinessException("Matches not created");
    }
    List<PlayOffMatches> matches = playOffMatchesRepository.findByPhase("Semi-Final");
    if (matches.stream().anyMatch(match -> match.getWinner() != null)) {
      throw new BusinessException("Semis already simulated");
    }
    matches.forEach(match -> {
      Teams winner = SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB());
      match.setWinner(winner);
      playOffMatchesRepository.save(match);
      List<PlayOffMatches> aFinal = playOffMatchesRepository.findByPhase("Final");
      if (aFinal.get(0).getTeamA() == null) {
        aFinal.get(0).setTeamA(winner);
      } else {
        aFinal.get(0).setTeamB(winner);
      }
      playOffMatchesRepository.save(aFinal.get(0));
    });
    return playOffMatchesRepository.findAll();
  }

  public PlayOffMatches simulateFinal() {
    if (playOffMatchesRepository.findAll().isEmpty()) {
      throw new BusinessException("Matches not created");
    }
    List<PlayOffMatches> matches = playOffMatchesRepository.findByPhase("Final");
    if (matches.stream().anyMatch(match -> match.getTeamA() == null || match.getTeamB() == null)) {
      throw new BusinessException("Semis not finished yet");
    }
    if (matches.stream().anyMatch(match -> match.getWinner() != null)) {
      throw new BusinessException("Final already simulated, please create a new championship");
    }
    Teams winner = SimulateMatches.simulateMatch(matches.get(0).getTeamA(),
        matches.get(0).getTeamB());
    matches.get(0).setWinner(winner);
    teamsService.increaseChampionships(winner.getId());
    return playOffMatchesRepository.save(matches.get(0));
  }
}
