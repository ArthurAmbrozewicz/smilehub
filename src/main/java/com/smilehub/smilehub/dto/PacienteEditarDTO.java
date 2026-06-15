package com.smilehub.smilehub.dto;

public record PacienteEditarDTO(
        String nome,
        String email,
        String cpf,
        String telefone,
        Boolean ativo
) {
}
