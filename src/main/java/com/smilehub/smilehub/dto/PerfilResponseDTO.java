package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Perfil;

public record PerfilResponseDTO(Long id, String nome) {

    public static PerfilResponseDTO from(Perfil perfil) {
        return new PerfilResponseDTO(perfil.getId(), perfil.getNome());
    }
}
