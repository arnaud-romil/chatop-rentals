package com.chatop.rentalsapi.controller;

import com.chatop.rentalsapi.model.dto.request.MessageCreationRequestDTO;
import com.chatop.rentalsapi.model.dto.response.MessageResponseDTO;
import com.chatop.rentalsapi.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@Tag(name = "Message Controller", description = "Endpoints for managing messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  @Operation(
      summary = "Creates a new message",
      description = "Creates a new message with the provided inputs",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Message created",
            content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content()),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<MessageResponseDTO> createMessage(
      @Valid @RequestBody MessageCreationRequestDTO messageCreationRequest,
      Authentication authentication) {
    messageService.createMessage(messageCreationRequest, authentication.getName());
    return ResponseEntity.ok().body(new MessageResponseDTO("Message send with success"));
  }
}
