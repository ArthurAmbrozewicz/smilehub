package com.smilehub.smilehub.dto;

public record PacienteRequestDTO(
        String nome,
        String email,
        String cpf,
        String telefone,
        Long usuarioId
) {
}
