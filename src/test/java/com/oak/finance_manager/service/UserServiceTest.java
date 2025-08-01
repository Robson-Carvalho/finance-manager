package com.oak.finance_manager.service;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.domain.user.UserRequestDTO;
import com.oak.finance_manager.exceptions.EmailAlreadyExistsException;
import com.oak.finance_manager.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("Should create user successfully when everything is ok")
    void createUserCase1() {
        UserRequestDTO data = new UserRequestDTO("robson", "robson@gmail.com", "123456");
        User savedUser = new User();
        savedUser.setName(data.name());
        savedUser.setEmail(data.email());
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode(data.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.findByEmail(data.email())).thenReturn(Optional.of(savedUser));

        userService.createUser(data);

        verify(passwordEncoder).encode(data.password());
        verify(userRepository).save(any(User.class));

        Optional<User> response = userRepository.findByEmail(data.email());
        assertThat(response).isPresent();
        assertThat(response.get().getEmail()).isEqualTo(data.email());
    }

    @Test
    @DisplayName("Should not create the user when the email is already in use")
    void createUserCase2() {
        UserRequestDTO data = new UserRequestDTO("robson", "robson@gmail.com", "123456");

        // Configura o mock para simular que o email já existe
        when(userRepository.existsByEmail(data.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(data))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email já cadastrado");


        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}