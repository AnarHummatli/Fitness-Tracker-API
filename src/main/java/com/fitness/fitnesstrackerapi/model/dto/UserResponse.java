package com.fitness.fitnesstrackerapi.model.dto;

import com.fitness.fitnesstrackerapi.model.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Min(8)
    @Max(100)
    @NotNull
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
