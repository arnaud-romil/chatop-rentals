package com.chatop.rentalsapi.model.dto.response;

import java.util.List;

public class ValidationErrorResponseDTO {

  public final List<FieldErrorDTO> errors;

  public ValidationErrorResponseDTO(List<FieldErrorDTO> errors) {
    this.errors = errors;
  }

  public List<FieldErrorDTO> getErrors() {
    return errors;
  }
}
