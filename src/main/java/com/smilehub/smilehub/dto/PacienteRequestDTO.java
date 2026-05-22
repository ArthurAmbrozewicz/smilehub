package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Ativo;

public record PacienteRequestDTO(
        String nome,
        String email,
        String senha,
        String cpf,
        String telefone,
        Ativo ativo
) {
}
