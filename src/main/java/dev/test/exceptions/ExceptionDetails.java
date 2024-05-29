package dev.test.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@AllArgsConstructor
public class ExceptionDetails {

  private String title;
  private int status;
  private String detail;
  private LocalDateTime timestamp;
  private String developerMessage;
}