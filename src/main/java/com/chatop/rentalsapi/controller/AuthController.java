package com.chatop.rentalsapi.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.model.dto.request.LoginRequestDTO;
import com.chatop.rentalsapi.model.dto.request.RegisterRequestDTO;
import com.chatop.rentalsapi.model.dto.response.TokenResponseDTO;
import com.chatop.rentalsapi.model.dto.response.UserResponseDTO;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.service.UserService;
import com.chatop.rentalsapi.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Authentication APIs")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtService;

    public AuthController(UserService userService, JwtUtil jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Creates a new user")
    public TokenResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        User user = userService.registerUser(registerRequest);
        return jwtService.generateToken(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        ResponseEntity<TokenResponseDTO> result;
        Optional<User> user = userService.login(loginRequest);
        if (user.isPresent()) {
            result = ResponseEntity.ok(jwtService.generateToken(user.get()));
        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }

    @GetMapping("/me")
    @Operation(summary = "Returns informations of the authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        ResponseEntity<UserResponseDTO> result;
        User user = userService.findByEmail(authentication.getName());
        if (user != null) {
            result = ResponseEntity.ok().body(new UserResponseDTO(user));
        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }
}
