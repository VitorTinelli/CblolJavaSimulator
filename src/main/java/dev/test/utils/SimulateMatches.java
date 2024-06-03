package dev.test.utils;

import java.util.Random;
import dev.test.domain.Teams;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SimulateMatches {

  private final Random random = new Random();

  public Teams simulateMatch(Teams teamA, Teams teamB) {
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
}
