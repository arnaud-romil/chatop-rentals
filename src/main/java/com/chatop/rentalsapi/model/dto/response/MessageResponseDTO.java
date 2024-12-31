package com.chatop.rentalsapi.model.dto.response;

public class MessageResponseDTO {

    private String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
