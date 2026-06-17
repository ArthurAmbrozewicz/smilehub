package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Servico;
import com.smilehub.smilehub.entities.ServicoMaterial;
import java.math.BigDecimal;
import java.util.List;

public record ServicoResponseDTO(
        Long id,
        String nome,
        BigDecimal valor,
        boolean ativo,
        UsuarioResumoDTO usuario,
        List<ServicoMaterialItemDTO> materiais
) {

    public static ServicoResponseDTO from(Servico servico) {
        return from(servico, List.of());
    }

    public static ServicoResponseDTO from(Servico servico, List<ServicoMaterial> materiais) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getNome(),
                servico.getValor(),
                servico.isAtivo(),
                UsuarioResumoDTO.from(servico.getUsuario()),
                materiais.stream().map(ServicoMaterialItemDTO::from).toList()
        );
    }
}
