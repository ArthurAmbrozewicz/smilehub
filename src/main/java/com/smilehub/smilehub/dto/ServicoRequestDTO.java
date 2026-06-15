package com.smilehub.smilehub.dto;

import java.math.BigDecimal;

public record ServicoRequestDTO(
        String nome,
        BigDecimal valor,
        Boolean ativo
) {
}
