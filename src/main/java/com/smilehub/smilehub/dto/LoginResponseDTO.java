package com.smilehub.smilehub.dto;

public record LoginResponseDTO(
        String token,
        Long id,
        String email,
        String dtype
) {
}
