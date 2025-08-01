package com.oak.finance_manager.service;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.domain.user.UserRequestDTO;
import com.oak.finance_manager.domain.user.UserResponseDTO;
import com.oak.finance_manager.exceptions.EmailAlreadyExistsException;
import com.oak.finance_manager.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser.getId().toString(), savedUser.getName(), savedUser.getEmail());
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
