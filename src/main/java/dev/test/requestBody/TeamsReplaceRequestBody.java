package dev.test.requestBody;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamsReplaceRequestBody {

  @NotNull
  private UUID id;
  @NotNull
  private String name;
  @NotNull
  private String nicknames;
  @NotNull
  @PositiveOrZero
  private int championships;

}
