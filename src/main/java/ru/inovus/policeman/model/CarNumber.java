package ru.inovus.policeman.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "car_numbers")
public class CarNumber {
    @Id
    private String id;

    @Field("number")
    private String number;

    @Field("created_at")
    private LocalDateTime createdAt;
}
