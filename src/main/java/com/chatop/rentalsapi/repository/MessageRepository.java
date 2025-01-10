package com.chatop.rentalsapi.repository;

import com.chatop.rentalsapi.model.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {}
