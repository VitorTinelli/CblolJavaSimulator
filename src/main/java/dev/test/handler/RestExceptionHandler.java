package dev.test.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import dev.test.exceptions.BusinessException;
import dev.test.exceptions.BusinessExceptionDetails;
import dev.test.exceptions.MethodArgumentNotValidDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<BusinessExceptionDetails> handleBusinessException(BusinessException be) {
    return new ResponseEntity<>(
        BusinessExceptionDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .title("Business Exception, Check the Documentation")
            .detail(be.getMessage())
            .developerMessage(be.getClass().getName())
            .build(), HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<MethodArgumentNotValidDetails> handlerMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

    String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
    String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

    return new ResponseEntity<>(
        MethodArgumentNotValidDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .title("Bad Request Exception, Invalid Fields")
            .detail("Check the field(s) error")
            .developerMessage(exception.getClass().getName())
            .fields(fields)
            .fieldsMessage(fieldsMessage)
            .build(), HttpStatus.BAD_REQUEST);
  }
}
