package ru.inovus.policeman.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.inovus.policeman.exception.AuthException;
import ru.inovus.policeman.exception.CarNumberOutOfBoundException;
import ru.inovus.policeman.security.jwt.exception.JwtParsingException;
import ru.inovus.policeman.exception.message.ErrorMessage;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestDocumentExceptionHandler {

    @ExceptionHandler(value = {CarNumberOutOfBoundException.class, JwtParsingException.class, AuthException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ErrorMessage handleBadRequest(RuntimeException ex) {
        return ErrorMessage.getInstance(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
