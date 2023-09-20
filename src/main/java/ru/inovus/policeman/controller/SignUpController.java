package ru.inovus.policeman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inovus.policeman.dto.SignUpDto;
import ru.inovus.policeman.service.SignUpService;

@RestController
@RequestMapping("/api/v1/sign-up")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
        signUpService.signUp(signUpDto);
        return ResponseEntity.ok("Sign up successful");
    }
}
