package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Servico;
import java.math.BigDecimal;

public record ServicoResponseDTO(
        Long id,
        String nome,
        BigDecimal valor,
        boolean ativo,
        UsuarioResumoDTO usuario
) {

    public static ServicoResponseDTO from(Servico servico) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getValor(),
                servico.isAtivo(),
                UsuarioResumoDTO.from(servico.getUsuario())
        );
    }
}
