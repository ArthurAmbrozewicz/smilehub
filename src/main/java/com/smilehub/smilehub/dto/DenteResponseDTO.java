package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Dente;

public record DenteResponseDTO(
        Long id,
        Integer numero,
        String status,
        String observacoes
) {

    public static DenteResponseDTO from(Dente dente) {
        return new DenteResponseDTO(
                dente.getId(),
                dente.getNumero(),
                dente.getStatus(),
                dente.getObservacoes()
        );
    }
}
