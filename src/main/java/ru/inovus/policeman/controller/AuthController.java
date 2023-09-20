package ru.inovus.policeman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inovus.policeman.dto.UserDto;
import ru.inovus.policeman.security.jwt.dto.JwtResponse;
import ru.inovus.policeman.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public JwtResponse auth(@RequestBody UserDto userDto) {
        return authService.auth(userDto);
    }

}
