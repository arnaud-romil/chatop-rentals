package com.chatop.rentalsapi.model.dto.response;

import com.chatop.rentalsapi.model.entity.Rental;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class RentalResponseDTO {

  private final Long id;
  private final String name;
  private final BigDecimal surface;
  private final BigDecimal price;
  private final String picture;
  private final String description;

  @JsonProperty("owner_id")
  private final Long ownerId;

  @JsonProperty("created_at")
  private final Instant createdAt;

  @JsonProperty("updated_at")
  private final Instant updatedAt;

  public RentalResponseDTO(Rental rental) {
    this.id = rental.getId();
    this.name = rental.getName();
    this.surface = rental.getSurface();
    this.price = rental.getPrice();
    this.picture = buildPictureLink(rental.getPicture());
    this.description = rental.getDescription();
    this.ownerId = rental.getUser().getId();
    this.createdAt = rental.getCreatedAt();
    this.updatedAt = rental.getUpdatedAt();
  }

  private String buildPictureLink(String picturePath) {
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/rentals/pictures/")
        .path(picturePath)
        .toUriString();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getSurface() {
    return surface;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getPicture() {
    return picture;
  }

  public String getDescription() {
    return description;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
