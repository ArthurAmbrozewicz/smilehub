package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Ativo;

public record DentistaRequestDTO(
        String nome,
        String email,
        String senha,
        String cpf,
        String cro,
        Ativo ativo
) {
}
