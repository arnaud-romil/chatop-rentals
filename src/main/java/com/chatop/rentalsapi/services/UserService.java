package com.chatop.rentalsapi.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatop.rentalsapi.models.LoginRequest;
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

    public Optional<User> login(LoginRequest loginRequest) {
        Optional<User> result;
        User user = userRepository.findByEmail(loginRequest.getLogin());
        if (user != null && isPasswordCorrect(user, loginRequest)) {
            result = Optional.of(user);
        } else {
            result = Optional.empty();
        }
        return result;
    }

    private boolean isPasswordCorrect(User user, LoginRequest loginRequest) {
        return this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    }
}
