package com.chatop.rentalsapi.model.dto.response;

public class TokenResponseDTO {

  private final String token;

  public TokenResponseDTO(String tokenValue) {
    this.token = tokenValue;
  }

  public String getToken() {
    return token;
  }
}
