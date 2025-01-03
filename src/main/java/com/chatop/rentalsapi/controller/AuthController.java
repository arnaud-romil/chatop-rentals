package com.chatop.rentalsapi.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller", description = "Endpoints for managing authentication features.")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtService;

    public AuthController(UserService userService, JwtUtil jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registers a new user", description = "Creates a new user and returns a JWT Token", responses = {
            @ApiResponse(responseCode = "200", description = "User registered", content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
    })
    public TokenResponseDTO register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        User user = userService.registerUser(registerRequest);
        return jwtService.generateToken(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticates a user", description = "Returns JWT Token if the user credentials are valid", responses = {
            @ApiResponse(responseCode = "200", description = "User is authenticated", content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "User is unauthorized", content = @Content())
    })
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        ResponseEntity<TokenResponseDTO> result;
        Optional<User> user = userService.login(loginRequest);
        if (user.isPresent()) {
            result = ResponseEntity.ok(jwtService.generateToken(user.get()));
        } else {
            result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return result;
    }

    @GetMapping("/me")
    @Operation(summary = "Get user details", description = "Returns details of the authenticated user", responses = {
            @ApiResponse(responseCode = "200", description = "User details returned", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "User is unauthorized", content = @Content())
    }, security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        ResponseEntity<UserResponseDTO> result;
        User user = userService.findByEmail(authentication.getName());
        if (user != null) {
            result = ResponseEntity.ok().body(new UserResponseDTO(user));
        } else {
            result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return result;
    }
}
