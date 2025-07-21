package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.EmailAlreadyExistsException;
import com.fitness.fitnesstrackerapi.exception.InvalidCredentialsException;
import com.fitness.fitnesstrackerapi.model.dto.LoginRequest;
import com.fitness.fitnesstrackerapi.model.dto.RegisterRequest;
import com.fitness.fitnesstrackerapi.model.dto.UserResponse;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }
        String encodedPassword = passwordEncoder.encode(password);

        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = new User(
                null,
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                encodedPassword,
                registerRequest.getAge(),
                registerRequest.getGender(),
                registerRequest.getHeight(),
                registerRequest.getWeight()
        );

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse loginUser(LoginRequest loginRequest) {
        Optional<User> existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existingUser.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = existingUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getGender(),
                user.getHeight(),
                user.getWeight()
        );
    }

}
