package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.ConsultaServico;
import java.math.BigDecimal;

public record ConsultaServicoResponseDTO(
        Long id,
        Long servicoId,
        String servicoNome,
        BigDecimal valor
) {

    public static ConsultaServicoResponseDTO from(ConsultaServico consultaServico) {
        return new ConsultaServicoResponseDTO(
                consultaServico.getId(),
                consultaServico.getServico().getId(),
                consultaServico.getServico().getNome(),
                consultaServico.getValor()
        );
    }
}
