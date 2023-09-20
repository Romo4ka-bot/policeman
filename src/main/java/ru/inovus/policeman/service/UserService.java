package ru.inovus.policeman.service;

import ru.inovus.policeman.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> getByUsername(String email);
}
