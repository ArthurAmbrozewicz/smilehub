package com.smilehub.smilehub.dto;

import java.math.BigDecimal;
import java.util.List;

public record ServicoUpdateDTO(
        String nome,
        BigDecimal valor,
        Boolean ativo,
        List<ServicoMaterialItemDTO> materiais
) {
}
