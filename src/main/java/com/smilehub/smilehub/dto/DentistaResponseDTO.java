package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Ativo;
import com.smilehub.smilehub.entities.Dentista;
import java.time.LocalDateTime;

public record DentistaResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String cro,
        LocalDateTime dataCriacao,
        UsuarioResumoDTO usuario,
        Ativo ativo
) {

    public static DentistaResponseDTO from(Dentista dentista) {
        return new DentistaResponseDTO(
                dentista.getId(),
                dentista.getNome(),
                dentista.getCpf(),
                dentista.getEmail(),
                dentista.getCro(),
                dentista.getDataCriacao(),
                UsuarioResumoDTO.from(dentista.getUsuario()),
                dentista.getAtivo()
        );
    }
}
