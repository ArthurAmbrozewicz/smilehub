package com.smilehub.smilehub.dto;

import java.util.List;

public record OdontogramaSalvarDTO(
        String observacoes,
        List<DenteItemDTO> dentes
) {
}
