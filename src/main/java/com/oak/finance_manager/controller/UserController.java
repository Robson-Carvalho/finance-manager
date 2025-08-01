package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.domain.user.UserResponseDTO;
import com.oak.finance_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String email) {
        Optional<User> user = this.userService.getUserByEmail(email);

        if(!user.isEmpty()){
            return ResponseEntity.ok(new UserResponseDTO(user.get().getId().toString(), user.get().getName(), user.get().getEmail()));
        }

        return ResponseEntity.badRequest().build();
    }
}
