package dev.test.Domain;

import java.util.UUID;
import dev.test.domain.PlayOffMatches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayOffMatchesTest {

  UUID id;

  @InjectMocks
  PlayOffMatches playOffMatches;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
  }

  @Test
  @DisplayName("Test all getter and setter")
  void testAllGetterAndSetter() {
    playOffMatches.setId(id);
    playOffMatches.setPhase("Semi-Final");
    playOffMatches.setTeamA(null);
    playOffMatches.setTeamB(null);
    playOffMatches.setWinner(null);
    playOffMatches.getId();
    playOffMatches.getPhase();
    playOffMatches.getTeamA();
    playOffMatches.getTeamB();
    playOffMatches.getWinner();
  }

  @Test
  @DisplayName("Test all builder")
  void testAllBuilder() {
    PlayOffMatches.builder().id(id).phase("Semi-Final").teamA(null).teamB(null).winner(null)
        .build();
  }

  @Test
  @DisplayName("Test to string")
  void testToString() {
    PlayOffMatches.builder().id(id).phase("Semi-Final").teamA(null).teamB(null).winner(null)
        .toString();
  }
}
