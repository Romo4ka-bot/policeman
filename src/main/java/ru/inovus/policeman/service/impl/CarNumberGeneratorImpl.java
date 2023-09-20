package ru.inovus.policeman.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inovus.policeman.exception.CarNumberOutOfBoundException;
import ru.inovus.policeman.model.CarNumber;
import ru.inovus.policeman.repository.CarNumberRepository;
import ru.inovus.policeman.service.CarNumberGenerator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CarNumberGeneratorImpl implements CarNumberGenerator {
    private static final char[] LETTERS = "АЕТОРНУКХСВМ".toCharArray();
    private static final int MAX_DIGITS = 999;
    private static final Random RANDOM = new Random();
    private static final String REGION_CODE = "116 RUS";

    private final CarNumberRepository carNumberRepository;

    private static String previousNumber;

    @Override
    public String generateRandomNumber() {
        do {
            char letter1 = LETTERS[RANDOM.nextInt(LETTERS.length)];
            char letter2 = LETTERS[RANDOM.nextInt(LETTERS.length)];
            char letter3 = LETTERS[RANDOM.nextInt(LETTERS.length)];
            int digits = RANDOM.nextInt(MAX_DIGITS + 1);

            previousNumber = String.format("%c%d%d%d%c%c " + REGION_CODE, letter1, digits / 100, (digits % 100) / 10, digits % 10, letter2, letter3);
        } while (carNumberRepository.existsByNumber(previousNumber));

        carNumberRepository.save(CarNumber.builder()
                .number(previousNumber)
                .build());
        return previousNumber;
    }

    @Override
    public String generateNextNumber() {
        Optional<CarNumber> carNumber = carNumberRepository.findFirstByOrderByCreatedAtDesc();
        if (carNumber.isEmpty()) {
            previousNumber = "A000AA 116 RUS";
            carNumberRepository.save(CarNumber.builder()
                    .number(previousNumber)
                    .createdAt(LocalDateTime.now())
                    .build());
            return previousNumber;
        }
        previousNumber = carNumber.get().getNumber();

        do {
            // Парсим цифры из предыдущего номера
            int digits = Integer.parseInt(previousNumber.substring(1, 4));

            // Если цифры максимальные, переходим к следующим буквам
            if (digits == MAX_DIGITS) {
                int letterIndex1 = indexOfLetter(previousNumber.charAt(0));
                int letterIndex2 = indexOfLetter(previousNumber.charAt(4));
                int letterIndex3 = indexOfLetter(previousNumber.charAt(5));

                if (letterIndex3 == LETTERS.length - 1) {
                    if (letterIndex2 == LETTERS.length - 1) {
                        if (letterIndex1 == LETTERS.length - 1) {
                            throw new CarNumberOutOfBoundException("Car number out of bound");
                        }
                        letterIndex1 = (indexOfLetter(previousNumber.charAt(0)) + 1) % LETTERS.length;
                    } else {
                        letterIndex2 = (indexOfLetter(previousNumber.charAt(4)) + 1) % LETTERS.length;
                    }
                } else {
                    letterIndex3 = (indexOfLetter(previousNumber.charAt(5)) + 1) % LETTERS.length;
                }

                char letter1 = LETTERS[letterIndex1];
                char letter2 = LETTERS[letterIndex2];
                char letter3 = LETTERS[letterIndex3];

                previousNumber = String.format("%c000%c%c " + REGION_CODE, letter1, letter2, letter3);
            } else {

                // Увеличиваем цифры на 1
                digits++;
                char letter1 = previousNumber.charAt(0);
                char letter2 = previousNumber.charAt(4);
                char letter3 = previousNumber.charAt(5);
                previousNumber = String.format("%c%d%d%d%c%c " + REGION_CODE, letter1, digits / 100, (digits % 100) / 10, digits % 10, letter2, letter3);
            }
        } while (carNumberRepository.existsByNumber(previousNumber));

        carNumberRepository.save(CarNumber.builder()
                .number(previousNumber)
                .createdAt(LocalDateTime.now())
                .build());

        return previousNumber;
    }

    private int indexOfLetter(char letter) {
        for (int i = 0; i < LETTERS.length; i++) {
            if (LETTERS[i] == letter) {
                return i;
            }
        }
        return -1;
    }
}