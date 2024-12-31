package com.chatop.rentalsapi.model.dto.response;

public class TokenResponseDTO {

    private String token;

    public TokenResponseDTO(String tokenValue) {
        this.token = tokenValue;
    }

    public String getToken() {
        return token;
    }
}
