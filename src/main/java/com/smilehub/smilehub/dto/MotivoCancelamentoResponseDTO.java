package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.MotivoCancelamento;
import java.time.LocalDateTime;

public record MotivoCancelamentoResponseDTO(
        Long id,
        String descricao,
        LocalDateTime dataCriacao,
        UsuarioResumoDTO usuarioCriacao
) {

    public static MotivoCancelamentoResponseDTO from(MotivoCancelamento motivo) {
        return new MotivoCancelamentoResponseDTO(
                motivo.getId(),
                motivo.getDescricao(),
                motivo.getDataCriacao(),
                UsuarioResumoDTO.from(motivo.getUsuarioCriacao())
        );
    }
}
