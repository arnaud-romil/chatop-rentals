package com.chatop.rentalsapi.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatop.rentalsapi.model.dto.LoginRequestDTO;
import com.chatop.rentalsapi.model.dto.RegisterRequestDTO;
import com.chatop.rentalsapi.model.dto.UserResponseDTO;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequestDTO registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return saveUser(user);
    }

    public Optional<User> login(LoginRequestDTO loginRequest) {
        Optional<User> result;
        User user = userRepository.findByEmail(loginRequest.getLogin());
        if (user != null && isPasswordCorrect(user, loginRequest)) {
            result = Optional.of(user);
        } else {
            result = Optional.empty();
        }
        return result;
    }

    public Optional<UserResponseDTO> findUserByEmail(String email) {
        Optional<UserResponseDTO> result;
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            result = Optional.of(new UserResponseDTO(user));
        } else {
            result = Optional.empty();
        }
        return result;
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private boolean isPasswordCorrect(User user, LoginRequestDTO loginRequest) {
        return this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    }
}
