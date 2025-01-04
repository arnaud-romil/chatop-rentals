package com.chatop.rentalsapi.repository;

import com.chatop.rentalsapi.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  User findByEmail(String email);
}
