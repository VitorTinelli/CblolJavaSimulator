package dev.test.Domain;

import java.util.UUID;
import dev.test.domain.Matches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MatchesTest {

  UUID id;

  @InjectMocks
  Matches matches;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
  }

  @Test
  @DisplayName("Test all getter and setter")
  void testAllGetterAndSetter() {
    matches.setId(id);
    matches.setDay(1);
    matches.setRound(1);
    matches.setTeamA(null);
    matches.setTeamB(null);
    matches.setWeek(1);
    matches.setWinner(null);
    matches.getId();
    matches.getDay();
    matches.getWeek();
    matches.getRound();
    matches.getTeamA();
    matches.getTeamB();
    matches.getWinner();
  }

  @Test
  @DisplayName("Test all builder")
  void testAllBuilder() {
    Matches.builder().id(id).day(1).round(1).week(1).teamA(null).teamB(null).winner(null).build();
  }

  @Test
  @DisplayName("Test to string")
  void testToString() {
    Matches.builder().id(id).day(1).round(1).week(1).teamA(null).teamB(null).winner(null)
        .toString();
  }


}
