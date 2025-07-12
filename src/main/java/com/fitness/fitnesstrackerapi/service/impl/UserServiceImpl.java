package com.fitness.fitnesstrackerapi.service.impl;

import com.fitness.fitnesstrackerapi.model.User;
import com.fitness.fitnesstrackerapi.repository.UserRepository;
import com.fitness.fitnesstrackerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
