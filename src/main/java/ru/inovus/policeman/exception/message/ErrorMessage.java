package ru.inovus.policeman.exception.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private HttpStatus statusCode;

    public static ErrorMessage getInstance(String message, HttpStatus httpStatus) {
        return ErrorMessage.builder()
                .message(message)
                .statusCode(httpStatus)
                .build();
    }
}