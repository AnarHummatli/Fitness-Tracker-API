package com.fitness.fitnesstrackerapi.model.dto;

import com.fitness.fitnesstrackerapi.model.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Min(8)
    @Max(100)
    @NotNull
    @Positive
    private Integer age;

    @NotNull
    private Gender gender;

    @NotNull
    @Positive
    private Double height;

    @NotNull
    @Positive
    private Double weight;
}
