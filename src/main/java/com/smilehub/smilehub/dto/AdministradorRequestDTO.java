package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Ativo;

public record AdministradorRequestDTO(
        String nome,
        String email,
        String senha,
        Ativo ativo
) {
}
