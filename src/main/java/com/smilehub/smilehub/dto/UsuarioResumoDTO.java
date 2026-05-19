package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Usuario;

public record UsuarioResumoDTO(Long id, String nome) {

    public static UsuarioResumoDTO from(Usuario usuario) {
        return new UsuarioResumoDTO(usuario.getId(), usuario.getNome());
    }
}
