package com.chatop.rentalsapi.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.models.LoginRequest;
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

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        ResponseEntity<TokenResponse> result;
        Optional<User> user = userService.login(loginRequest);
        if (user.isPresent()) {
            result = ResponseEntity.ok(jwtService.generateToken(user.get()));
        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }

    @GetMapping("/auth/me")
    public String me(Authentication authentication) {
        return "Hello " + authentication.getName();
    }
}
