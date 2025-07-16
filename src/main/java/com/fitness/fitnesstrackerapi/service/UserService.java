package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> login(String email, String password);
}
