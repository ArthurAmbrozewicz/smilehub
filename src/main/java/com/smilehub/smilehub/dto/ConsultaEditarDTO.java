package com.smilehub.smilehub.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultaEditarDTO(
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        List<ConsultaServicoItemDTO> servicos
) {
}
