package ru.inovus.policeman.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ru.inovus.policeman.dto.Role;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    private static final long serialVersionUID = 9132222312087392904L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    private Set<Role> roles;
}
