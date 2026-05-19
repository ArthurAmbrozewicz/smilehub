package com.smilehub.smilehub.dto;

public record UsuarioRequestDTO(
        String nome,
        String email,
        String cpf,
        String senha,
        Long perfilId
) {
}
