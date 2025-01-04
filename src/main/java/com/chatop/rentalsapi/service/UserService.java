package com.chatop.rentalsapi.service;

import com.chatop.rentalsapi.exception.DatabaseException;
import com.chatop.rentalsapi.exception.InvalidDataException;
import com.chatop.rentalsapi.model.dto.request.LoginRequestDTO;
import com.chatop.rentalsapi.model.dto.request.RegisterRequestDTO;
import com.chatop.rentalsapi.model.entity.User;
import com.chatop.rentalsapi.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User registerUser(RegisterRequestDTO registerRequest) {
    final Instant now = Instant.now();
    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setName(registerRequest.getName());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    return saveUser(user);
  }

  public Optional<User> login(LoginRequestDTO loginRequest) {
    Optional<User> result;
    User user = findByEmail(loginRequest.getEmail());
    if (user != null && isPasswordCorrect(user, loginRequest)) {
      result = Optional.of(user);
    } else {
      result = Optional.empty();
    }
    return result;
  }

  public User findByEmail(String email) {
    try {
      return userRepository.findByEmail(email);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  public Optional<User> findById(Long userId) {
    try {
      return this.userRepository.findById(userId);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  private User saveUser(User user) {
    try {
      return userRepository.save(user);
    } catch (DataIntegrityViolationException ex) {
      throw new InvalidDataException("A user with this email already exists", ex);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  private boolean isPasswordCorrect(User user, LoginRequestDTO loginRequest) {
    return this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
  }
}
