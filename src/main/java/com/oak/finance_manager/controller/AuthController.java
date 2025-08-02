package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.LoginRequestDTO;
import com.oak.finance_manager.dto.LoginResponseDTO;
import com.oak.finance_manager.dto.RegisterResponseDTO;
import com.oak.finance_manager.dto.RegisterRequestDTO;
import com.oak.finance_manager.exceptions.EmailAlreadyExistsException;
import com.oak.finance_manager.exceptions.InactiveAccountException;
import com.oak.finance_manager.exceptions.UserNotFoundException;
import com.oak.finance_manager.infra.security.TokenService;
import com.oak.finance_manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {
        User user = this.userService.findByEmail(body.email());

        if(user == null || !passwordEncoder.matches(body.password(), user.getPassword())){
            throw new UserNotFoundException("Invalid email or password");
        }

        if(!user.isEmail_verified()){
            throw new InactiveAccountException();
        }

        String token = this.tokenService.getToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(user.getId(), user.getName(), user.getEmail(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        User user = this.userService.findByEmail(body.email());

        if(user != null){
            throw new EmailAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setName(body.name());
        newUser.setEmail(body.email());
        newUser.setPassword(passwordEncoder.encode(body.password()));

        String id =  this.userService.create(body.name(), body.email(), body.password());

        String token = this.tokenService.getToken(newUser);

        return ResponseEntity.ok(new RegisterResponseDTO(id, body.email(), token));
    }
}
