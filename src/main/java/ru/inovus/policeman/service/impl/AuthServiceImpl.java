package ru.inovus.policeman.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inovus.policeman.dto.UserDto;
import ru.inovus.policeman.exception.AuthException;
import ru.inovus.policeman.model.User;
import ru.inovus.policeman.security.jwt.JwtProvider;
import ru.inovus.policeman.security.jwt.dto.JwtResponse;
import ru.inovus.policeman.service.AuthService;
import ru.inovus.policeman.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse auth(UserDto userDto) {
        UserDto user = userService.getByUsername(userDto.getUsername())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));

        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            User map = modelMapper.map(user, User.class);
            map.setRoles(user.getRoles());
            String token = jwtProvider.generateToken(map);
            return new JwtResponse(token);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

}
