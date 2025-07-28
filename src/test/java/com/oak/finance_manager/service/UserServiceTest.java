package com.oak.finance_manager.service;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.domain.user.UserRequestDTO;
import com.oak.finance_manager.domain.user.UserResponseDTO;
import com.oak.finance_manager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    void createUser_ShouldEncodePasswordAndSaveUser() {
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO("John Doe", "john@example.com", "password123");
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponseDTO responseDTO = userService.createUser(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(savedUser.getId(), responseDTO.id());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
}