package com.chatop.rentalsapi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.rentalsapi.model.dto.RentalCreationDTO;
import com.chatop.rentalsapi.model.dto.RentalListResponseDTO;
import com.chatop.rentalsapi.model.dto.RentalResponseDTO;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.RentalRepository;

@Service
public class RentalService {

    private final UserService userService;
    private final RentalRepository rentalRepository;

    private final Path storageLocation = Paths.get("uploads");

    public RentalService(UserService userService, RentalRepository rentalRepository) throws IOException {
        Files.createDirectories(storageLocation);
        this.userService = userService;
        this.rentalRepository = rentalRepository;
    }

    public Rental createRental(RentalCreationDTO rentalCreation, String owner) {
        try {
            final Instant now = Instant.now();
            User user = userService.findByEmail(owner);
            String picturePath = storePicture(rentalCreation.getPicture());

            Rental rental = new Rental();
            rental.setName(rentalCreation.getName());
            rental.setSurface(rentalCreation.getSurface());
            rental.setPrice(rentalCreation.getPrice());
            rental.setPicture(picturePath);
            rental.setDescription(rentalCreation.getDescription());
            rental.setUser(user);
            rental.setCreatedAt(now);
            rental.setUpdatedAt(now);

            return rentalRepository.save(rental);

        } catch (Exception ex) {
            return null;
        }
    }

    public Optional<Rental> findById(Long requestedRentalId) {
        return rentalRepository.findById(requestedRentalId);
    }

    public Resource servePicture(String fileName) {
        Resource result = null;
        try {
            Path filePath = storageLocation.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if ((resource.exists() && resource.isReadable())) {
                result = resource;
            }
        } catch (Exception ex) {
            result = null;
        }
        return result;
    }

    public RentalListResponseDTO findAll() {
        return new RentalListResponseDTO(
                StreamSupport.stream(rentalRepository.findAll().spliterator(), false)
                        .map(rental -> new RentalResponseDTO(rental))
                        .collect(Collectors.toList()));
    }

    private String storePicture(MultipartFile picture) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();
        Path destination = storageLocation.resolve(fileName);
        Files.copy(picture.getInputStream(), destination);
        return fileName;
    }

}
