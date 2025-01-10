package com.chatop.rentalsapi.repository;

import com.chatop.rentalsapi.model.entity.Rental;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {

  Optional<Rental> findByIdAndUserId(Long id, Long userId);
}
