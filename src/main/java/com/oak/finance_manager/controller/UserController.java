package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.user.UserUpdateDTO;
import com.oak.finance_manager.exceptions.EmailAlreadyExistsException;
import com.oak.finance_manager.exceptions.UnauthorizedException;
import com.oak.finance_manager.exceptions.UserNotFoundException;
import com.oak.finance_manager.infra.security.TokenService;
import com.oak.finance_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @DeleteMapping("/{token}/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String token,  @PathVariable String id) {
        String email = tokenService.validateToken(token);

        if(email == null){
            throw new UnauthorizedException("Invalid token");
        }

        User user = userService.findById(id);

        if(user==null){
            throw new UserNotFoundException("User not found");
        }

        userService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{token}/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String token, @PathVariable String id, @RequestBody UserUpdateDTO userUpdateDTO) {
        String email = tokenService.validateToken(token);

        if(email == null){
            throw new UnauthorizedException("Invalid token");
        }

        if(!email.equals(userUpdateDTO.email())){
            if(userService.findByEmail(userUpdateDTO.email()) != null){
                throw new EmailAlreadyExistsException();
            }
        }

        User user = userService.findById(id);

        if(user==null){
            throw new UserNotFoundException();
        }

        userService.update(user.getId().toString(), userUpdateDTO);

        return ResponseEntity.ok().build();
    }

}
