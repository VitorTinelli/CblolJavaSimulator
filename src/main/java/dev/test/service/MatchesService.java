package dev.test.service;

import java.util.*;
import java.util.stream.Collectors;
import dev.test.domain.MatchHistory;
import dev.test.domain.Matches;
import dev.test.domain.Teams;
import dev.test.exceptions.BusinessException;
import dev.test.repository.MatchHistoryRepository;
import dev.test.repository.MatchesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchesService {

  private static final String MATCH_NOT_FOUND = "Match not found";
  private final MatchesRepository matchesRepository;
  private final MatchHistoryRepository matchesHistoryRepository;
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

  private Teams simulateMatch(Teams teamA, Teams teamB) {
    while (true) {
      int teamAScore = random.nextInt(10);
      int teamBScore = random.nextInt(10);
      if (teamAScore > teamBScore) {
        return teamA;
      } else if (teamAScore < teamBScore) {
        return teamB;
      }
    }
  }

  public List<MatchHistory> simulateByDay(int day) {
    List<Matches> matches = matchesRepository.findByDay(day)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
    matches.forEach(match -> {
      match.setWinner(simulateMatch(match.getTeamA(), match.getTeamB()));
      matchesRepository.save(match);
    });
    return matchesHistoryRepository.findByDay(day)
        .orElseThrow(() -> new BusinessException(MATCH_NOT_FOUND));
  }

}
