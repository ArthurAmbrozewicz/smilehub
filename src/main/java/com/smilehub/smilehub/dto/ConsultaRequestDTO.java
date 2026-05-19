package com.smilehub.smilehub.dto;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(
        Long pacienteId,
        Long dentistaId,
        Long usuarioId,
        Long motivoCancelamentoId,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String status
) {
}
