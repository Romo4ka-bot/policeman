package ru.inovus.policeman.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}