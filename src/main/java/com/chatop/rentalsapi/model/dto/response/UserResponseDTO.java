package com.chatop.rentalsapi.model.dto.response;

import com.chatop.rentalsapi.model.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class UserResponseDTO {

  private final Long id;
  private final String email;
  private final String name;

  @JsonProperty("created_at")
  private final Instant createdAt;

  @JsonProperty("updated_at")
  private final Instant updatedAt;

  public UserResponseDTO(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.name = user.getName();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
