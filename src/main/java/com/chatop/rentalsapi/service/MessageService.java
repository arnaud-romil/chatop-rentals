package com.chatop.rentalsapi.service;

import com.chatop.rentalsapi.exception.DatabaseException;
import com.chatop.rentalsapi.exception.InvalidDataException;
import com.chatop.rentalsapi.model.dto.request.MessageCreationRequestDTO;
import com.chatop.rentalsapi.model.entity.Message;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.MessageRepository;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final UserService userService;
  private final RentalService rentalService;

  public MessageService(
      MessageRepository messageRepository, UserService userService, RentalService rentalService) {
    this.messageRepository = messageRepository;
    this.userService = userService;
    this.rentalService = rentalService;
  }

  public void createMessage(MessageCreationRequestDTO messageCreationRequest) {

    final Instant now = Instant.now();
    Optional<User> userOptional = userService.findById(messageCreationRequest.getUserId());
    Optional<Rental> rentalOptional = rentalService.findById(messageCreationRequest.getRentalId());
    if (userOptional.isPresent() && rentalOptional.isPresent()) {
      Message message = new Message();
      message.setValue(messageCreationRequest.getMessage());
      message.setUser(userOptional.get());
      message.setRental(rentalOptional.get());
      message.setCreatedAt(now);
      message.setUpdatedAt(now);
      saveMessage(message);
    } else {
      StringBuilder errorMessage = new StringBuilder("Could not find:");
      if (userOptional.isEmpty()) {
        errorMessage.append(" - user with id: " + messageCreationRequest.getUserId());
      }
      if (rentalOptional.isEmpty()) {
        errorMessage.append(" - rental with id: " + messageCreationRequest.getRentalId());
      }
      throw new InvalidDataException(errorMessage.toString());
    }
  }

  private Message saveMessage(Message message) {
    try {
      return messageRepository.save(message);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }
}
