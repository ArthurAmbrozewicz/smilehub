package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Dentista;

public record DentistaResumoDTO(Long id, String nome) {

    public static DentistaResumoDTO from(Dentista dentista) {
        return new DentistaResumoDTO(dentista.getId(), dentista.getNome());
    }
}
