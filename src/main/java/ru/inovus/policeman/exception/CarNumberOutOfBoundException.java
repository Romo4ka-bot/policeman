package ru.inovus.policeman.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CarNumberOutOfBoundException extends RuntimeException {
    public CarNumberOutOfBoundException(String message) {
        super(message);
    }
}