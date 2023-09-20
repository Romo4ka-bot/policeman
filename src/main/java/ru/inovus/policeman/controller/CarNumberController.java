package ru.inovus.policeman.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inovus.policeman.service.CarNumberGenerator;

@RestController
@RequestMapping("/api/v1/numbers")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class CarNumberController {

    private final CarNumberGenerator carNumberGenerator;

    @Operation(summary = "Obtaining a random number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Number generated and returned",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)})
    @GetMapping("/random")
    public String getRandomNumber() {
        return carNumberGenerator.generateRandomNumber();
    }

    @Operation(summary = "Obtaining the next number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Number generated and returned",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content)})
    @GetMapping("/next")
    public String getNextNumber() {
        return carNumberGenerator.generateNextNumber();
    }
}
