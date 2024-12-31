package com.chatop.rentalsapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chatop.rentalsapi.model.entity.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {

    Optional<Rental> findByIdAndUserId(Long id, Long userId);

}
