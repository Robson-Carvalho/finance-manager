package com.oak.finance_manager.repositories;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.user.UserRequestDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get user successfully from DB")
    void findUserByEmailCase1() {
        UserRequestDTO data = new UserRequestDTO("robson", "robson@gmail.com", "123456");
        this.createUser(data);

        Optional<User> foundedUser = this.userRepository.findByEmail(data.email());

        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from DB when user not exists")
    void findUserByEmailCase2() {
        Optional<User> foundedUser = this.userRepository.findByEmail("robson@gmail");

        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private void createUser(UserRequestDTO data){
        User user = new User();
        user.setName(data.name());
        user.setEmail(data.email());
        user.setPassword(passwordEncoder.encode(data.password()));

        this.entityManager.persist(user);
    }
}
