package com.chatop.rentalsapi.controller;

import com.chatop.rentalsapi.model.dto.response.UserResponseDTO;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  @Operation(
      summary = "Get user by ID",
      description = "Retrieves details of a user by their unique ID",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<UserResponseDTO> getUser(
      @Parameter(description = "ID of the requested user") @PathVariable Long userId) {
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
