package com.smilehub.smilehub.dto;

import java.math.BigDecimal;

public record ConsultaServicoItemDTO(
        Long servicoId,
        BigDecimal valor
) {
}
