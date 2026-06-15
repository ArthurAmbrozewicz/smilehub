package com.smilehub.smilehub.dto;

public record AdministradorEditarDTO(
        String nome,
        String email,
        String senha,
        Boolean ativo
) {
}
