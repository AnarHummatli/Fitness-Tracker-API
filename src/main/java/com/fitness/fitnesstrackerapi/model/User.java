package com.fitness.fitnesstrackerapi.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Double height;
    private Double weight;

}
