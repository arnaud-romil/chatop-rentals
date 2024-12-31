package com.chatop.rentalsapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chatop.rentalsapi.model.entity.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}
