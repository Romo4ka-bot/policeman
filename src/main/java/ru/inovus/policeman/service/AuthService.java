package ru.inovus.policeman.service;

import ru.inovus.policeman.security.jwt.dto.JwtResponse;
import ru.inovus.policeman.dto.UserDto;

public interface AuthService {
    JwtResponse auth(UserDto userDto);
}
