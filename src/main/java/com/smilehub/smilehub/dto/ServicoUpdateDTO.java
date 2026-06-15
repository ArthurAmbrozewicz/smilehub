package com.smilehub.smilehub.dto;

import java.math.BigDecimal;

public record ServicoUpdateDTO(
        String nome,
        BigDecimal valor,
        Boolean ativo
) {
}
