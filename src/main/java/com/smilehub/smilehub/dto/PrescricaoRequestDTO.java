package com.smilehub.smilehub.dto;

import java.util.List;

public record PrescricaoRequestDTO(
        Long consultaId,
        String observacoes,
        List<MedicamentoItemDTO> medicamentos
) {
}
