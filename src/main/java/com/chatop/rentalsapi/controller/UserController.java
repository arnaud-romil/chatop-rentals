package com.chatop.rentalsapi.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.model.dto.response.UserResponseDTO;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "User APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Returns informations of the authenticated user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long userId) {
        ResponseEntity<UserResponseDTO> result;
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            result = ResponseEntity.ok().body(new UserResponseDTO(userOptional.get()));
        } else {
            result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return result;
    }

}
