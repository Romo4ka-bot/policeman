package ru.inovus.policeman.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inovus.policeman.dto.Role;
import ru.inovus.policeman.dto.SignUpDto;
import ru.inovus.policeman.exception.AuthException;
import ru.inovus.policeman.model.User;
import ru.inovus.policeman.repository.UserRepository;
import ru.inovus.policeman.service.SignUpService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpDto signUpDto) {
        if (!signUpDto.getPassword().equals(signUpDto.getVerifyPassword())) {
            throw new AuthException("Пароли не совпадают");
        }

        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new AuthException("Пользователь с таким username уже существует");
        }

        User build = User.builder()
                .username(signUpDto.getUsername())
                .name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(Set.of(Role.USER)).build();
        userRepository.save(build);
    }
}
