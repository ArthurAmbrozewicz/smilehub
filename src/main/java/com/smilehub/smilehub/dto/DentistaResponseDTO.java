package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Dentista;
import java.time.LocalDateTime;
import java.util.List;

public record DentistaResponseDTO(
        Long id,
        String nome,
        String email,
        String dtype,
        String cpf,
        String cro,
        LocalDateTime dataCriacao,
        LocalDateTime ultimoLogin,
        boolean ativo,
        List<EspecialidadeResumoDTO> especialidades
) {

    public static DentistaResponseDTO from(Dentista dentista) {
        return from(dentista, List.of());
    }

    public static DentistaResponseDTO from(Dentista dentista, List<EspecialidadeResumoDTO> especialidades) {
        return new DentistaResponseDTO(
                dentista.getId(),
                dentista.getNome(),
                dentista.getEmail(),
                "DENTISTA",
                dentista.getCpf(),
                dentista.getCro(),
                dentista.getDataCriacao(),
                dentista.getUltimoLogin(),
                dentista.isAtivo(),
                especialidades
        );
    }
}
