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

  private static final String FINAL = "Final";
  private static final String SEMI = "Semi-Final";
  private final PlayOffMatchesRepository playOffMatchesRepository;
  private final MatchesService matchesService;
  private final TeamsService teamsService;
  private final Map<Teams, Integer> map = new HashMap<>();

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
    matches.forEach(match -> map.put(match.getWinner(), map.get(match.getWinner()) + 1));

    List<Map.Entry<Teams, Integer>> matchesList = new ArrayList<>(map.entrySet());
    matchesList.sort(Map.Entry.<Teams, Integer>comparingByValue().reversed());

    Teams first = matchesList.get(0).getKey();
    Teams second = matchesList.get(1).getKey();
    Teams third = matchesList.get(2).getKey();
    Teams fourth = matchesList.get(3).getKey();

    PlayOffMatches semi1 = PlayOffMatches.builder()
        .teamA(first)
        .teamB(fourth)
        .phase(SEMI)
        .build();
    playOffMatchesRepository.save(semi1);
    PlayOffMatches semi2 = PlayOffMatches.builder()
        .teamA(second)
        .teamB(third)
        .phase(SEMI)
        .build();
    playOffMatchesRepository.save(semi2);
    PlayOffMatches finalMatch = PlayOffMatches.builder()
        .teamA(null)
        .teamB(null)
        .phase(FINAL)
        .build();
    playOffMatchesRepository.save(finalMatch);
    return playOffMatchesRepository.findAll();
  }

  public List<PlayOffMatches> simulateSemis() {
    List<PlayOffMatches> simulatedMatches = new ArrayList<>();
    List<PlayOffMatches> matches = playOffMatchesRepository.findByPhase(SEMI)
        .orElseThrow(() -> new BusinessException("Matches not created"));
    if (matches.stream().anyMatch(match -> match.getWinner() != null)) {
      throw new BusinessException("Semis already simulated");
    }
    matches.forEach(match -> {
      Teams winner = SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB());
      match.setWinner(winner);
      simulatedMatches.add(match);
      defineFinal(winner);
    });
    return playOffMatchesRepository.saveAll(simulatedMatches);
  }

  private void defineFinal(Teams winner) {
    PlayOffMatches aFinal = playOffMatchesRepository.findByPhaseAndWinner(FINAL, null)
        .orElseThrow(() -> new BusinessException("Final not created"));
    if (aFinal.getTeamA() == null) {
      aFinal.setTeamA(winner);
    } else {
      aFinal.setTeamB(winner);
    }
    playOffMatchesRepository.save(aFinal);
  }

  public PlayOffMatches simulateFinal() {
    PlayOffMatches aFinal = playOffMatchesRepository.findByPhaseAndWinner(FINAL, null)
        .orElseThrow(() -> new BusinessException("Final not created"));
    if (aFinal.getTeamA() == null || aFinal.getTeamB() == null) {
      throw new BusinessException("Semis not finished yet");
    }
    Teams winner = SimulateMatches.simulateMatch(aFinal.getTeamA(), aFinal.getTeamB());
    aFinal.setWinner(winner);
    teamsService.increaseChampionships(winner.getId());
    return playOffMatchesRepository.save(aFinal);
  }
}
