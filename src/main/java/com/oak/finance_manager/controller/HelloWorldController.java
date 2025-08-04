package com.oak.finance_manager.controller;

import com.oak.finance_manager.dto.HelloWorldDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

    @GetMapping
    public ResponseEntity<HelloWorldDTO> HelloWorld() {
        return ResponseEntity.ok(new HelloWorldDTO("Welcome to Finance Manager"));
    }
}
