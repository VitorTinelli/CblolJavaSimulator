package dev.test.service;

import java.util.*;
import java.util.stream.Collectors;
import dev.test.domain.Matches;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.MatchesRepository;
import dev.test.utils.SimulateMatches;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchesService {

  private static final String MATCH_NOT_FOUND = "Match not found";
  private final MatchesRepository matchesRepository;
  private final TeamsService teamsService;
  private final Random random = new Random();
  public List<Matches> listAll() {
    return matchesRepository.findAll().stream().sorted(Comparator.comparingInt(Matches::getDay))
        .collect(Collectors.toList());
  }

  public Matches findByIdOrThrowBusinessException(UUID id) {
    return matchesRepository.findById(id)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
  }

  public Matches findByTeamAAndTeamB(UUID teamA, UUID teamB) {
    return matchesRepository.findByTeamAAndTeamB(
            teamsService.findByIdOrThrowBusinessException(teamA),
            teamsService.findByIdOrThrowBusinessException(teamB))
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
  }

  public List<Matches> createChampionshipMatches() {
    if (!matchesRepository.findAll().isEmpty()) {
      throw new BusinessException("Matches already created");
    }
    List<Teams> teams = teamsService.listAll();
    Collections.shuffle(teams);
    int totalTeams = teams.size();
    int halfDays = (totalTeams - 1);
    int halfTeams = totalTeams / 2;
    List<Teams> halfTeamsList = new ArrayList<>(
        teams.subList(0, halfTeams));
    List<Teams> halfTeamsList2 = new ArrayList<>(
        teams.subList(halfTeams, totalTeams));

    for (int day = 0; day < halfDays; day++) {
      for (int i = 0; i < halfTeams; i++) {
        Matches match = Matches.builder()
            .teamA(halfTeamsList2.get(i))
            .teamB(halfTeamsList.get(i))
            .round(1)
            .day(day + 1)
            .build();
        matchesRepository.save(match);
        match = Matches.builder()
            .teamA(halfTeamsList.get(i))
            .teamB(halfTeamsList2.get(i))
            .round(2)
            .day(day + halfDays
                + 1)
            .build();
        matchesRepository.save(match);
      }
      halfTeamsList2.add(halfTeamsList2.remove(0));
    }
    setWeek();
    return matchesRepository.findAll().stream().sorted(Comparator.comparingInt(Matches::getDay))
        .collect(Collectors.toList());
  }

  private void setWeek() {
    List<Matches> matches = matchesRepository.findAll();
    matches.sort(Comparator.comparingInt(Matches::getDay));
    int week = 0;
    int ActualDay = 0;
    for (Matches match : matches) {
      if (match.getDay() % 2 != 0 && match.getDay() != ActualDay) {
        week++;
        ActualDay = match.getDay();
        match.setWeek(week);
      } else {
        match.setWeek(week);
      }
      matchesRepository.save(match);
    }
  }

  public Matches simulateMatch(UUID uuid) {
    Matches match = findByIdOrThrowBusinessException(uuid);
    if (match.getWinner() != null) {
      throw new BusinessException("Match already simulated");
    }
    match.setWinner(SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB()));
    matchesRepository.save(match);
    return match;
  }

  public List<Matches> simulateByDay(int day) {
    List<Matches> matches = matchesRepository.findByDay(day)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
    if (matches.stream().anyMatch(match -> match.getWinner() != null)) {
      throw new BusinessException("Matches already simulated");
    }
    matches.forEach(match -> {
      match.setWinner(SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB()));
      matchesRepository.save(match);
    });
    return matchesRepository.findByDay(day)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
  }

  public List<Matches> simulateByWeek(int week) {
    List<Matches> matches1 = matchesRepository.findByDay(week * 2 - 1)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
    List<Matches> matches2 = matchesRepository.findByDay(week * 2)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
    if (matches1.stream().allMatch(match -> match.getWinner() == null)) {
      matches1.forEach(match -> {
        match.setWinner(SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB()));
        matchesRepository.save(match);
      });
    }
    if (matches2.stream().allMatch(match -> match.getWinner() == null)) {
      matches2.forEach(match -> {
        match.setWinner(SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB()));
        matchesRepository.save(match);
      });
    }
    return matchesRepository.findByWeek(week)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
  }

  public List<Matches> simulateAll() {
    List<Matches> matches = matchesRepository.findAll();
    matches.stream().filter(match -> match.getWinner() == null).forEach(match -> {
      match.setWinner(SimulateMatches.simulateMatch(match.getTeamA(), match.getTeamB()));
      matchesRepository.save(match);
    });
    return matchesRepository.findAll();
  }

  public Matches rematch(UUID id) {
    Matches match = findByIdOrThrowBusinessException(id);
    if (match.getWinner() == null) {
      throw new BusinessException("Match not played yet");
    }
    match.setWinner(null);
    simulateMatch(id);
    return matchesRepository.save(match);
  }
}
