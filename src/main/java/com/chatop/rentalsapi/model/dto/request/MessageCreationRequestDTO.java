package com.chatop.rentalsapi.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class MessageCreationRequestDTO {

    @NotNull
    private String message;
    @NotNull
    @JsonProperty("user_id")
    private Long userId;
    @NotNull
    @JsonProperty("rental_id")
    private Long rentalId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

}
