package ru.inovus.policeman.dto;

import lombok.Data;

@Data
public class SignUpDto {
    private String username;
    private String name;
    private String password;
    private String verifyPassword;
}
