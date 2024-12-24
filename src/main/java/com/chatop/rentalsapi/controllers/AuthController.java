package com.chatop.rentalsapi.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.models.RegisterRequest;
import com.chatop.rentalsapi.models.TokenResponse;
import com.chatop.rentalsapi.models.User;
import com.chatop.rentalsapi.services.JwtService;
import com.chatop.rentalsapi.services.UserService;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public TokenResponse register(@RequestBody RegisterRequest registerRequest) {
        User user = userService.registerUser(registerRequest);
        return jwtService.generateToken(user);
    }
}
