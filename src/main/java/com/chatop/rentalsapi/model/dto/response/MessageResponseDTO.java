package com.chatop.rentalsapi.model.dto.response;

public class MessageResponseDTO {

  private final String message;

  public MessageResponseDTO(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
