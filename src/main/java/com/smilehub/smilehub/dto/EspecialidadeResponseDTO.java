package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Especialidade;
import java.time.LocalDateTime;

public record EspecialidadeResponseDTO(
        Long id,
        String nome,
        UsuarioResumoDTO usuario,
        LocalDateTime dataCriacao,
        boolean ativo
) {

    public static EspecialidadeResponseDTO from(Especialidade especialidade) {
        return new EspecialidadeResponseDTO(
                especialidade.getId(),
                especialidade.getNome(),
                UsuarioResumoDTO.from(especialidade.getUsuario()),
                especialidade.getDataCriacao(),
                especialidade.isAtivo()
        );
    }
}
