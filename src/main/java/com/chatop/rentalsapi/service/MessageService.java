package com.chatop.rentalsapi.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chatop.rentalsapi.model.dto.request.MessageCreationRequestDTO;
import com.chatop.rentalsapi.model.entity.Message;
import com.chatop.rentalsapi.model.entity.Rental;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RentalService rentalService;

    public MessageService(MessageRepository messageRepository, UserService userService, RentalService rentalService) {
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
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException();
        }
    }

}