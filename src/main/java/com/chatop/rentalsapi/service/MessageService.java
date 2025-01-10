package com.chatop.rentalsapi.service;

import com.chatop.rentalsapi.exception.DatabaseException;
import com.chatop.rentalsapi.exception.UserUnauthorizedException;
import com.chatop.rentalsapi.model.dto.request.MessageCreationRequestDTO;
import com.chatop.rentalsapi.model.entity.Message;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.MessageRepository;
import java.time.Instant;
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

  public void createMessage(MessageCreationRequestDTO messageCreationRequest, String caller) {

    final Instant now = Instant.now();
    Rental rental = rentalService.findById(messageCreationRequest.getRentalId());
    User user = userService.findById(messageCreationRequest.getUserId());

    if (!user.getEmail().equals(caller)) {
      throw new UserUnauthorizedException("User is unauthorized to send message");
    }

    Message message = new Message();
    message.setValue(messageCreationRequest.getMessage());
    message.setUser(user);
    message.setRental(rental);
    message.setCreatedAt(now);
    message.setUpdatedAt(now);
    saveMessage(message);
  }

  private Message saveMessage(Message message) {
    try {
      return messageRepository.save(message);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }
}
