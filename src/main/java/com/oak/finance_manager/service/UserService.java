package com.oak.finance_manager.service;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.user.UserUpdateDTO;
import com.oak.finance_manager.exceptions.user.EmailAlreadyExistsException;
import com.oak.finance_manager.exceptions.user.UserNotFoundException;
import com.oak.finance_manager.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String create(String name,String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        return savedUser.getId().toString();
    }

    public User findByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public User findById(String id) {
        Optional<User> user = this.userRepository.findById(UUID.fromString(id));
        return user.orElse(null);
    }

    public void delete(String id) {
        userRepository.deleteById(UUID.fromString(id));

        if(userRepository.existsById(UUID.fromString(id))){
            throw new UserNotFoundException(id);
        }
    }

    public void update(String id, UserUpdateDTO dto) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(UserNotFoundException::new);

        user.setName(dto.name());
        user.setEmail(dto.email());
        if (!dto.password().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        } else {
            user.setPassword(dto.password());
        }
        user.setEmail_verified(dto.email_verified());

        userRepository.save(user);
    }

}