package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.LoginRequest;
import com.fitness.fitnesstrackerapi.model.dto.RegisterRequest;

public interface UserService {
    String registerUser(RegisterRequest request);
    String loginUser(LoginRequest loginRequest);
}
