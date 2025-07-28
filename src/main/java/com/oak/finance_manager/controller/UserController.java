package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.user.UserRequestDTO;
import com.oak.finance_manager.domain.user.UserResponseDTO;
import com.oak.finance_manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO body) {
        UserResponseDTO response = this.userService.createUser(body);
        return ResponseEntity.ok(response);
    }

}
