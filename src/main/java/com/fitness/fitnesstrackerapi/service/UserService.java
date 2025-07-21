package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.LoginRequest;
import com.fitness.fitnesstrackerapi.model.dto.RegisterRequest;
import com.fitness.fitnesstrackerapi.model.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(RegisterRequest registerRequest);
    UserResponse loginUser(LoginRequest loginRequest);
}
