package com.chatop.rentalsapi.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.model.dto.LoginRequestDTO;
import com.chatop.rentalsapi.model.dto.RegisterRequestDTO;
import com.chatop.rentalsapi.model.dto.TokenResponseDTO;
import com.chatop.rentalsapi.model.dto.UserResponseDTO;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.service.UserService;
import com.chatop.rentalsapi.util.JwtUtil;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtService;

    public AuthController(UserService userService, JwtUtil jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public TokenResponseDTO register(@RequestBody RegisterRequestDTO registerRequest) {
        User user = userService.registerUser(registerRequest);
        return jwtService.generateToken(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        ResponseEntity<TokenResponseDTO> result;
        Optional<User> user = userService.login(loginRequest);
        if (user.isPresent()) {
            result = ResponseEntity.ok(jwtService.generateToken(user.get()));
        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        ResponseEntity<UserResponseDTO> result;
        Optional<UserResponseDTO> userOptional = userService.findUserByEmail(authentication.getName());
        if (userOptional.isPresent()) {
            result = ResponseEntity.ok().body(userOptional.get());
        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }
}
