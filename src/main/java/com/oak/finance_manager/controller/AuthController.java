package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.auth.LoginRequestDTO;
import com.oak.finance_manager.dto.auth.LoginResponseDTO;
import com.oak.finance_manager.dto.auth.RegisterRequestDTO;
import com.oak.finance_manager.dto.auth.RegisterResponseDTO;
import com.oak.finance_manager.dto.user.UserUpdateDTO;
import com.oak.finance_manager.exceptions.EmailAlreadyExistsException;
import com.oak.finance_manager.exceptions.InactiveAccountException;
import com.oak.finance_manager.exceptions.UnauthorizedException;
import com.oak.finance_manager.exceptions.UserNotFoundException;
import com.oak.finance_manager.infra.security.TokenService;
import com.oak.finance_manager.service.EmailService;
import com.oak.finance_manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
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

        emailService.sendActivationEmail(token, body.email());

        return ResponseEntity.ok(new RegisterResponseDTO(id, body.email(), token));
    }

    @GetMapping("/activate_account/{token}")
    public ResponseEntity<Void> activateAccount(@PathVariable String token) {
        String email = tokenService.validateToken(token);

        if(email == null){
            throw new UnauthorizedException("Invalid token");
        }

        User user = this.userService.findByEmail(email);
        user.setEmail_verified(true);

        userService.update(user.getId().toString(), new UserUpdateDTO(user.getName(), user.getEmail(), user.getPassword(), user.isEmail_verified()));

        return ResponseEntity.ok().build();
    }

}
