package ru.inovus.policeman.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.inovus.policeman.model.CarNumber;

import java.util.Optional;

public interface CarNumberRepository extends MongoRepository<CarNumber, String> {
    Optional<CarNumber> findFirstByOrderByCreatedAtDesc();
    boolean existsByNumber(String number);
}
