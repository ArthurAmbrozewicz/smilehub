package com.smilehub.smilehub.dto;

public record LoginResponseDTO(
        String token,
        Long id,
        String nome,
        String email,
        String dtype
) {
}
