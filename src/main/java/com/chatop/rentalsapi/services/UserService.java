package com.chatop.rentalsapi.services;

import java.time.Instant;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatop.rentalsapi.models.RegisterRequest;
import com.chatop.rentalsapi.models.User;
import com.chatop.rentalsapi.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return saveUser(user);
    }
}
