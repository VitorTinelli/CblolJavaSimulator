package dev.test.Domain;

import java.util.UUID;
import dev.test.domain.Teams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamsTest {

  UUID id;

  @InjectMocks
  Teams teams;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
  }

  @Test
  @DisplayName("Test all getter and setter")
  void testAllGetterAndSetter() {
    teams.setId(id);
    teams.setChampionships(1);
    teams.setName("Oi");
    teams.setNicknames("Tchau");
    teams.getId();
    teams.getChampionships();
    teams.getName();
    teams.getNicknames();
  }

  @Test
  @DisplayName("Test all builder")
  void testAllBuilder() {
    Teams.builder().id(id).championships(1).name("Oi").nicknames("Tchau").build();
  }

  @Test
  @DisplayName("Test to string")
  void testToString() {
    Teams.builder().id(id).championships(1).name("Oi").nicknames("Tchau").toString();
  }
}
