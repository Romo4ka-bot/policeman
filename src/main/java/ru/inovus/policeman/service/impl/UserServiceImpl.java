package ru.inovus.policeman.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.inovus.policeman.dto.UserDto;
import ru.inovus.policeman.repository.UserRepository;
import ru.inovus.policeman.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<UserDto> getByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDto.setRoles(user.getRoles());
            return userDto;
        });
    }
}
