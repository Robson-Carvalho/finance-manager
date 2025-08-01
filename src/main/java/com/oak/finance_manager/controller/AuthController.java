package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.LoginRequestDTO;
import com.oak.finance_manager.dto.LoginResponseDTO;
import com.oak.finance_manager.dto.RegisterResponseDTO;
import com.oak.finance_manager.dto.RegisterRequestDTO;
import com.oak.finance_manager.exceptions.EmailAlreadyExistsException;
import com.oak.finance_manager.infra.security.TokenService;
import com.oak.finance_manager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!passwordEncoder.matches(body.password(), user.getPassword())){
            throw new EmailAlreadyExistsException();
        }

        String token = this.tokenService.getToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(user.getId(), user.getName(), user.getEmail(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = this.userRepository.findByEmail(body.email());

        if(user.isEmpty()){
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            this.userRepository.save(newUser);

            String token = this.tokenService.getToken(newUser);
            return ResponseEntity.ok(new RegisterResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), token));
        }

        return ResponseEntity.badRequest().build();
    }
}
