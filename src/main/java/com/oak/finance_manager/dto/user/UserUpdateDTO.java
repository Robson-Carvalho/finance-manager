package com.oak.finance_manager.dto.user;

public record UserUpdateDTO(String name, String email, String password, Boolean email_verified) {
}
