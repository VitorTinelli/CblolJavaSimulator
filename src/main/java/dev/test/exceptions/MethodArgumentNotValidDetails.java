package dev.test.exceptions;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MethodArgumentNotValidDetails extends ExceptionDetails {
  private final String fields;
  private final String fieldsMessage;

}