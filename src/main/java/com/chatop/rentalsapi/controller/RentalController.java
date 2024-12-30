package com.chatop.rentalsapi.controller;

import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.rentalsapi.model.dto.MessageResponseDTO;
import com.chatop.rentalsapi.model.dto.RentalCreationDTO;
import com.chatop.rentalsapi.model.dto.RentalListResponseDTO;
import com.chatop.rentalsapi.model.dto.RentalResponseDTO;
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

    @GetMapping("/{id}")
    @Operation(summary = "Returns the requested rental")
    public ResponseEntity<RentalResponseDTO> getRental(@PathVariable Long id) {
        ResponseEntity<RentalResponseDTO> result;
        Optional<Rental> rentalOptional = rentalService.findById(id);
        if (rentalOptional.isPresent()) {
            result = ResponseEntity.ok().body(new RentalResponseDTO(rentalOptional.get()));
        } else {
            result = ResponseEntity.status(401).build();
        }
        return result;
    }

    @GetMapping
    @Operation(summary = "Returns all rentals")
    public ResponseEntity<RentalListResponseDTO> getAllRentals() {
        RentalListResponseDTO rentals = rentalService.findAll();
        return ResponseEntity.ok().body(rentals);
    }

    @GetMapping("/pictures/{fileName}")
    @Operation(summary = "Returns the picture of a rental")
    public ResponseEntity<Resource> servePicture(@PathVariable String fileName) {
        ResponseEntity<Resource> result;
        Resource resource = rentalService.servePicture(fileName);
        if (resource != null) {
            result = ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            result = ResponseEntity.notFound().build();
        }
        return result;
    }

}
