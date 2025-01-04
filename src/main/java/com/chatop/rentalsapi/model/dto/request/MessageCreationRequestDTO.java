package com.chatop.rentalsapi.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data transfer object reprensenting a user's request to create a new message")
public class MessageCreationRequestDTO {

  @NotBlank private String message;

  @NotNull
  @JsonProperty("user_id")
  private Long userId;

  @NotNull
  @JsonProperty("rental_id")
  private Long rentalId;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getRentalId() {
    return rentalId;
  }

  public void setRentalId(Long rentalId) {
    this.rentalId = rentalId;
  }
}
