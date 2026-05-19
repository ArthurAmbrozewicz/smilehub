package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.MotivoCancelamento;

public record MotivoCancelamentoResumoDTO(Long id, String descricao) {

    public static MotivoCancelamentoResumoDTO from(MotivoCancelamento motivo) {
        return new MotivoCancelamentoResumoDTO(motivo.getId(), motivo.getDescricao());
    }
}
