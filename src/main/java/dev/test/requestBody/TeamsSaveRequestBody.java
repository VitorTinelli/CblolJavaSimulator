package dev.test.requestBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamsSaveRequestBody {

  @NotBlank
  private String name;
  @NotBlank
  private String nicknames;
  @NotNull
  @PositiveOrZero
  private int championships;
}
