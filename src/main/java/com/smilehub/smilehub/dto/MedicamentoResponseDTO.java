package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Medicamento;
import java.time.LocalDateTime;

public record MedicamentoResponseDTO(
        Long id,
        String nome,
        String dosagem,
        String frequencia,
        String duracao
) {

    public static MedicamentoResponseDTO from(Medicamento medicamento) {
        return new MedicamentoResponseDTO(
                medicamento.getId(),
                medicamento.getNome(),
                medicamento.getDosagem(),
                medicamento.getFrequencia(),
                medicamento.getDuracao()
        );
    }
}
