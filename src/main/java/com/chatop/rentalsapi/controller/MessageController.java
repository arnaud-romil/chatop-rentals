package com.chatop.rentalsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.model.dto.request.MessageCreationRequestDTO;
import com.chatop.rentalsapi.model.dto.response.MessageResponseDTO;
import com.chatop.rentalsapi.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/messages")
@Tag(name = "Message Controller", description = "Message APIs")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(summary = "Creates a new message")
    public ResponseEntity<MessageResponseDTO> createMessage(
            @Valid @RequestBody MessageCreationRequestDTO messageCreationRequest) {

        try {
            messageService.createMessage(messageCreationRequest);
            return ResponseEntity.ok().body(new MessageResponseDTO("Message send with success"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
