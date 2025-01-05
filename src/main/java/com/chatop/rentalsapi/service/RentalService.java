package com.chatop.rentalsapi.service;

import com.chatop.rentalsapi.exception.DatabaseException;
import com.chatop.rentalsapi.exception.PictureUploadException;
import com.chatop.rentalsapi.model.dto.request.RentalCreationRequestDTO;
import com.chatop.rentalsapi.model.dto.request.RentalUpdateRequestDTO;
import com.chatop.rentalsapi.model.dto.response.RentalListResponseDTO;
import com.chatop.rentalsapi.model.dto.response.RentalResponseDTO;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.RentalRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RentalService {

  private final UserService userService;
  private final RentalRepository rentalRepository;

  private final Path storageLocation = Paths.get("uploads");

  public RentalService(UserService userService, RentalRepository rentalRepository) {

    this.userService = userService;
    this.rentalRepository = rentalRepository;
  }

  public Rental createRental(RentalCreationRequestDTO rentalCreation, String owner) {

    final Instant now = Instant.now();
    User user = userService.findByEmail(owner);
    String picturePath = storePicture(rentalCreation.getPicture());

    Rental rental = new Rental();
    rental.setName(rentalCreation.getName());
    rental.setSurface(rentalCreation.getSurface());
    rental.setPrice(rentalCreation.getPrice());
    rental.setPicture("/uploads/" + picturePath);
    rental.setDescription(rentalCreation.getDescription());
    rental.setUser(user);
    rental.setCreatedAt(now);
    rental.setUpdatedAt(now);

    return saveRental(rental);
  }

  public RentalListResponseDTO findAllRentals() {
    return new RentalListResponseDTO(
        StreamSupport.stream(findAll().spliterator(), false).map(RentalResponseDTO::new).toList());
  }

  public Rental updateRental(RentalUpdateRequestDTO rentalUpdate, Long id, String ownerEmail) {
    Rental rental = null;
    User owner = userService.findByEmail(ownerEmail);
    Optional<Rental> rentalOptional = findByIdAndUserId(id, owner.getId());
    if (rentalOptional.isPresent()) {
      rental = rentalOptional.get();
      rental.setName(rentalUpdate.getName());
      rental.setSurface(rentalUpdate.getSurface());
      rental.setPrice(rentalUpdate.getPrice());
      rental.setDescription(rentalUpdate.getDescription());
      rental.setUpdatedAt(Instant.now());
      rental = saveRental(rental);
    }
    return rental;
  }

  public Optional<Rental> findById(Long rentalId) {
    try {
      return rentalRepository.findById(rentalId);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  private String storePicture(MultipartFile picture) {
    try {
      String fileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();
      Path destination = storageLocation.resolve(fileName);
      Files.copy(picture.getInputStream(), destination);
      return fileName;
    } catch (Exception ex) {
      throw new PictureUploadException("Error occurred while uploading rental picture", ex);
    }
  }

  private Iterable<Rental> findAll() {
    try {
      return rentalRepository.findAll();
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  private Optional<Rental> findByIdAndUserId(Long rentalId, Long ownerId) {
    try {
      return rentalRepository.findByIdAndUserId(rentalId, ownerId);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  private Rental saveRental(Rental rental) {
    try {
      return rentalRepository.save(rental);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }
}
