package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Notificacao;
import java.time.LocalDateTime;

public record NotificacaoResponseDTO(
        Long id,
        String descricao,
        LocalDateTime data,
        Long usuarioId,
        boolean lida
) {

    public static NotificacaoResponseDTO from(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getDescricao(),
                notificacao.getData(),
                notificacao.getUsuario().getId(),
                notificacao.isLida()
        );
    }
}
