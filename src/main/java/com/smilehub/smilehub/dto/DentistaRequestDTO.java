package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Ativo;

public record DentistaRequestDTO(
        String nome,
        String cpf,
        String email,
        String cro,
        Long usuarioId,
        Ativo ativo
) {
}
