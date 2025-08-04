package com.oak.finance_manager.dto.auth;

import java.util.UUID;

public record LoginResponseDTO(UUID id, String name, String email, String token) {
}
