package com.chatop.rentalsapi.models;

public class TokenResponse {

    private String token;

    public TokenResponse(String tokenValue) {
        this.token = tokenValue;
    }

    public String getToken() {
        return token;
    }
}
