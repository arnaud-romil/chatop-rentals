package com.chatop.rentalsapi.exception;

public class DatabaseException extends RuntimeException {

  public DatabaseException(Throwable cause) {
    super("Database error occurred", cause);
  }
}
