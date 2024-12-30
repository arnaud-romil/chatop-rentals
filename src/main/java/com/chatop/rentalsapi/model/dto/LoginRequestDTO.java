package com.chatop.rentalsapi.model.dto;

import jakarta.validation.constraints.NotNull;

public class LoginRequestDTO {

    @NotNull
    private String login;
    @NotNull
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
