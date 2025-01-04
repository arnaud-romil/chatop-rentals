package com.chatop.rentalsapi.controller;

import com.chatop.rentalsapi.model.dto.request.RentalCreationRequestDTO;
import com.chatop.rentalsapi.model.dto.request.RentalUpdateRequestDTO;
import com.chatop.rentalsapi.model.dto.response.MessageResponseDTO;
import com.chatop.rentalsapi.model.dto.response.RentalListResponseDTO;
import com.chatop.rentalsapi.model.dto.response.RentalResponseDTO;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
@Tag(name = "Rental Controller", description = "Endpoints for managing rentals")
public class RentalController {

  private final RentalService rentalService;

  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @PostMapping(consumes = {"multipart/form-data"})
  @Operation(
      summary = "Creates a new rental",
      description = "Creates a new rental with the provided inputs",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Rental created",
            content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content()),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<MessageResponseDTO> create(
      @Valid @ModelAttribute RentalCreationRequestDTO rentalCreation,
      Authentication authentication) {
    ResponseEntity<MessageResponseDTO> result;
    Rental rental = rentalService.createRental(rentalCreation, authentication.getName());
    if (rental != null) {
      result = ResponseEntity.ok().body(new MessageResponseDTO("Rental created !"));
    } else {
      result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return result;
  }

  @PutMapping(
      consumes = {"multipart/form-data"},
      value = "/{id}")
  @Operation(
      summary = "Updates a rental",
      description = "Updates a rental with the provided inputs",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Rental updated",
            content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content()),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<MessageResponseDTO> update(
      @Valid @ModelAttribute RentalUpdateRequestDTO rentalUpdate,
      @Parameter(description = "ID of the rental to update.") @PathVariable Long id,
      Authentication authentication) {
    ResponseEntity<MessageResponseDTO> result;
    Rental rental = rentalService.updateRental(rentalUpdate, id, authentication.getName());
    if (rental != null) {
      result = ResponseEntity.ok().body(new MessageResponseDTO("Rental updated !"));
    } else {
      result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return result;
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get rental by ID",
      description = "Retrieves details of a rental by their unique ID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Rental found",
            content = @Content(schema = @Schema(implementation = RentalResponseDTO.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<RentalResponseDTO> getRental(
      @Parameter(description = "ID of the requested rental") @PathVariable Long id) {
    ResponseEntity<RentalResponseDTO> result;
    Optional<Rental> rentalOptional = rentalService.findById(id);
    if (rentalOptional.isPresent()) {
      result = ResponseEntity.ok().body(new RentalResponseDTO(rentalOptional.get()));
    } else {
      result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return result;
  }

  @GetMapping
  @Operation(
      summary = "Get all rentals",
      description = "Retrieves details of all rentals",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "All rentals returned",
            content = @Content(schema = @Schema(implementation = RentalListResponseDTO.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<RentalListResponseDTO> getAllRentals() {
    RentalListResponseDTO rentals = rentalService.findAllRentals();
    return ResponseEntity.ok().body(rentals);
  }

  @GetMapping("/pictures/{fileName}")
  @Operation(
      summary = "Get picture of a rental",
      description = "Returns the picture of a rental",
      responses = {
        @ApiResponse(responseCode = "200", description = "Picture found"),
        @ApiResponse(responseCode = "404", description = "Picture not found", content = @Content())
      })
  public ResponseEntity<Resource> servePicture(
      @Parameter(description = "Name of the file for the rental picture.") @PathVariable
          String fileName) {
    ResponseEntity<Resource> result;
    Resource resource = rentalService.servePicture(fileName);
    if (resource != null) {
      result =
          ResponseEntity.ok()
              .header(
                  HttpHeaders.CONTENT_DISPOSITION,
                  "attachment; filename=\"" + resource.getFilename() + "\"")
              .contentType(MediaType.IMAGE_JPEG)
              .body(resource);
    } else {
      result = ResponseEntity.notFound().build();
    }
    return result;
  }
}
