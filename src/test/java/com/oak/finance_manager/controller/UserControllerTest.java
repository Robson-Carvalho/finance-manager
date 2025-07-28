package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.UserResponseDTO;
import com.oak.finance_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(UserControllerIntegrationTest.TestConfig.class) // Importa a configuração de teste
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService; // Injeta o mock configurado

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary // Substitui o bean real
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserResponseDTO responseDTO = new UserResponseDTO(userId);

        when(userService.createUser(any())).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "John Doe",
                        "email": "john@example.com",
                        "password": "password123"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }
}