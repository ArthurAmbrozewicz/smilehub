package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Ativo;
import com.smilehub.smilehub.entities.Dentista;
import java.time.LocalDateTime;

public record DentistaResponseDTO(
        Long id,
        String nome,
        String email,
        String dtype,
        String cpf,
        String cro,
        LocalDateTime dataCriacao,
        LocalDateTime ultimoLogin,
        Ativo ativo
) {

    public static DentistaResponseDTO from(Dentista dentista) {
        return new DentistaResponseDTO(
                dentista.getId(),
                dentista.getNome(),
                dentista.getEmail(),
                "DENTISTA",
                dentista.getCpf(),
                dentista.getCro(),
                dentista.getDataCriacao(),
                dentista.getUltimoLogin(),
                dentista.getAtivo()
        );
    }
}
