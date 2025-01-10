package com.chatop.rentalsapi.exception;

import com.chatop.rentalsapi.model.dto.response.FieldErrorDTO;
import com.chatop.rentalsapi.model.dto.response.ValidationErrorResponseDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(UserUnauthorizedException.class)
  public ResponseEntity<Void> handleUserUnauthorizedException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<String> handleDatabaseException(DatabaseException ex) {
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }

  @ExceptionHandler(PictureUploadException.class)
  public ResponseEntity<String> handlePictureUploadException(PictureUploadException ex) {
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(
      MethodArgumentNotValidException ex) {
    List<FieldErrorDTO> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
            .toList();
    return ResponseEntity.badRequest().body(new ValidationErrorResponseDTO(errors));
  }
}
