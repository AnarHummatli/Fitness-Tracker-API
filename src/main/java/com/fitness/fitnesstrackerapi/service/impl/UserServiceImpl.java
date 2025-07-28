package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.exception.EmailAlreadyExistsException;
import com.fitness.fitnesstrackerapi.exception.InvalidCredentialsException;
import com.fitness.fitnesstrackerapi.model.dto.LoginRequest;
import com.fitness.fitnesstrackerapi.model.dto.RegisterRequest;
import com.fitness.fitnesstrackerapi.model.entity.User;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.security.JwtService;
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
    private final JwtService jwtService;

    @Override
    public String registerUser(RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }
        String encodedPassword = passwordEncoder.encode(password);

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
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

        userRepository.save(user);
        return jwtService.generateToken(user.getEmail());
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {
        Optional<User> existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existingUser.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = existingUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
    }

}
