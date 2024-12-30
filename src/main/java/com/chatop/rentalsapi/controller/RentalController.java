package com.chatop.rentalsapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.model.dto.MessageResponseDTO;
import com.chatop.rentalsapi.model.dto.RentalCreationDTO;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentals")
@Tag(name = "Rental Controller", description = "Rental APIs")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping(consumes = { "multipart/form-data" })
    @Operation(summary = "Creates a new rental")
    public ResponseEntity<MessageResponseDTO> create(@Valid @ModelAttribute RentalCreationDTO rentalCreation,
            Authentication authentication) {
        ResponseEntity<MessageResponseDTO> result;
        Rental rental = rentalService.createRental(rentalCreation, authentication.getName());
        if (rental != null) {
            result = ResponseEntity.ok().body(new MessageResponseDTO("Rental created !"));

        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }

}
