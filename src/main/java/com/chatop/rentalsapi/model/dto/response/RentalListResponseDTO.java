package com.chatop.rentalsapi.model.dto.response;

import java.util.List;

public class RentalListResponseDTO {

    private List<RentalResponseDTO> rentals;

    public RentalListResponseDTO(List<RentalResponseDTO> rentals) {
        this.rentals = rentals;
    }

    public List<RentalResponseDTO> getRentals() {
        return rentals;
    }
}
