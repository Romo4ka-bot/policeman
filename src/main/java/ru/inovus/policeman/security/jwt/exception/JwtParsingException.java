package ru.inovus.policeman.security.jwt.exception;

public class JwtParsingException extends RuntimeException{
    public JwtParsingException(String message) {
        super(message);
    }
}
