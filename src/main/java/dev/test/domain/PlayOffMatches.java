package dev.test.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayOffMatches {

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @ManyToOne
  private Teams teamA;
  @ManyToOne
  private Teams teamB;
  @ManyToOne
  private Teams winner;

  private String phase;
}

