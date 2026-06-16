package com.smilehub.smilehub.dto;

public record DentistaEditarDTO(
        String nome,
        String email,
        String cpf,
        String cro,
        String senha,
        Boolean ativo
) {
}
