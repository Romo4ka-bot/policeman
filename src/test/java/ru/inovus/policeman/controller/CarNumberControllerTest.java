package ru.inovus.policeman.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.inovus.policeman.AbstractTestIT;
import ru.inovus.policeman.exception.CarNumberOutOfBoundException;
import ru.inovus.policeman.model.CarNumber;
import ru.inovus.policeman.repository.CarNumberRepository;
import ru.inovus.policeman.service.CarNumberGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarNumberControllerTest extends AbstractTestIT {

    private static final String BASE_URL = "/api/v1/numbers";
    private static final String AUTH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjMzMjUyMTEyMjI2LCJuYW1lIjoiUm9tYW4iLCJlbWFpbCI6InF3ZXJ0eSIsInJvbGVzIjpbIlVTRVIiXX0.RMT9N16ZLTbB7XVO17qtCYwqC7CxHSonrJOT_lMiUzxTJ6UFRdTTsXYIn-CN89arUw6ROMMHqMEXHasxrZGAcg";

    private final TestRestTemplate testRestTemplate;
    private final CarNumberRepository carNumberRepository;
    private final CarNumberGenerator carNumberGenerator;

    @Autowired
    public CarNumberControllerTest(TestRestTemplate testRestTemplate, CarNumberRepository carNumberRepository, CarNumberGenerator carNumberGenerator) throws IOException {
        this.testRestTemplate = testRestTemplate;
        this.carNumberRepository = carNumberRepository;
        this.carNumberGenerator = carNumberGenerator;
    }

    @AfterEach
    void tearDown() {
        carNumberRepository.deleteAll();
    }

    @Test
    void withAuthTokenMethodGetRandomNumberReturnSuccessfulResponse() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + AUTH_TOKEN);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/random", HttpMethod.GET,
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        CarNumber carNumber = carNumberRepository.findFirstByOrderByCreatedAtDesc().get();

        assertNotNull(carNumber);
        assertNotNull(carNumber.getId());
        assertNotNull(carNumber.getNumber());
    }

    @Test
    void withoutAuthTokenMethodGetRandomNumberThrowForbidden() {
        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/random", HttpMethod.GET,
                HttpEntity.EMPTY, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void withAuthTokenMethodGetNextNumberReturnSuccessfulResponse() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + AUTH_TOKEN);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/next", HttpMethod.GET,
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        CarNumber carNumber = carNumberRepository.findFirstByOrderByCreatedAtDesc().get();

        assertNotNull(carNumber);
        assertNotNull(carNumber.getId());
        assertEquals("A000AA 116 RUS", carNumber.getNumber());

        // add car numbers
        carNumberRepository.saveAll(List.of(CarNumber.builder()
                        .number("A001AA 116 RUS")
                        .createdAt(LocalDateTime.now())
                        .build(),
                CarNumber.builder()
                        .number("A002AA 116 RUS")
                        .createdAt(LocalDateTime.now())
                        .build(),
                CarNumber.builder()
                        .number("A003AA 116 RUS")
                        .createdAt(LocalDateTime.now())
                        .build()
        ));

        response = testRestTemplate.exchange(BASE_URL + "/next", HttpMethod.GET,
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        carNumber = carNumberRepository.findFirstByOrderByCreatedAtDesc().get();

        assertNotNull(carNumber);
        assertNotNull(carNumber.getId());
        assertEquals("A004AA 116 RUS", carNumber.getNumber());
    }

    @Test
    void withAuthTokenMethodsGetRandomNumberAndGetNextNumberReturnSuccessfulResponseTogether() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + AUTH_TOKEN);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/random", HttpMethod.GET,
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        CarNumber carNumber = carNumberRepository.findFirstByOrderByCreatedAtDesc().get();

        String number = carNumber.getNumber();

        assertNotNull(carNumber);
        assertNotNull(carNumber.getId());
        assertNotNull(number);

        response = testRestTemplate.exchange(BASE_URL + "/next", HttpMethod.GET,
                request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        carNumber = carNumberRepository.findFirstByOrderByCreatedAtDesc().get();

        String expectedNumber = getNextNumber(number);
        assertNotNull(carNumber);
        assertNotNull(carNumber.getId());
        assertEquals(expectedNumber, carNumber.getNumber());
    }

    private String getNextNumber(String number) {
        String previousNumber = number;
        char[] LETTERS = "АЕТОРНУКХСВМ".toCharArray();
        int MAX_DIGITS = 999;
        String REGION_CODE = "116 RUS";

        // Парсим цифры из предыдущего номера
        int digits = Integer.parseInt(previousNumber.substring(1, 4));

        // Если цифры максимальные, переходим к следующим буквам
        if (digits == MAX_DIGITS) {
            int letterIndex1 = indexOfLetter(previousNumber.charAt(0), LETTERS);
            int letterIndex2 = indexOfLetter(previousNumber.charAt(4), LETTERS);
            int letterIndex3 = indexOfLetter(previousNumber.charAt(5), LETTERS);

            if (letterIndex3 == LETTERS.length - 1) {
                if (letterIndex2 == LETTERS.length - 1) {
                    if (letterIndex1 == LETTERS.length - 1) {
                        throw new CarNumberOutOfBoundException("Car number out of bound");
                    }
                    letterIndex1 = (indexOfLetter(previousNumber.charAt(0), LETTERS) + 1) % LETTERS.length;
                } else {
                    letterIndex2 = (indexOfLetter(previousNumber.charAt(4), LETTERS) + 1) % LETTERS.length;
                }
            } else {
                letterIndex3 = (indexOfLetter(previousNumber.charAt(5), LETTERS) + 1) % LETTERS.length;
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

        return previousNumber;
    }

    private int indexOfLetter(char letter, char[] LETTERS) {
        for (int i = 0; i < LETTERS.length; i++) {
            if (LETTERS[i] == letter) {
                return i;
            }
        }
        return -1;
    }
}