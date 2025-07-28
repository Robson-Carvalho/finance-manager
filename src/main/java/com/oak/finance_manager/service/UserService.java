package com.oak.finance_manager.service;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.domain.user.UserRequestDTO;
import com.oak.finance_manager.domain.user.UserResponseDTO;
import com.oak.finance_manager.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository  userRepository ,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRequestDTO data){
        User newUser = new User();

        newUser.setName(data.name());
        newUser.setEmail(data.email());
        newUser.setPassword(passwordEncoder.encode(data.password()));
        newUser.setEmail_verified(false);

        User savedUser = userRepository.save(newUser);

        return new UserResponseDTO(savedUser.getId());
    }
}
