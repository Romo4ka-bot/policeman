package ru.inovus.policeman.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.inovus.policeman.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
