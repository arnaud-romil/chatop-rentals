package com.chatop.rentalsapi.exception;

public class UserUnauthorizedException extends RuntimeException {

  public UserUnauthorizedException(String message) {
    super(message);
  }
}
