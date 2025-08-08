package com.fitness.fitnesstrackerapi.service;

import com.fitness.fitnesstrackerapi.model.dto.request.LoginRequest;
import com.fitness.fitnesstrackerapi.model.dto.request.RegisterRequest;

public interface UserService {
    String registerUser(RegisterRequest request);
    String loginUser(LoginRequest loginRequest);
}
