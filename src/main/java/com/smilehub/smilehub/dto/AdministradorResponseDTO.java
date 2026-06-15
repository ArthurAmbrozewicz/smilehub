package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Administrador;
import java.time.LocalDateTime;

public record AdministradorResponseDTO(
        Long id,
        String nome,
        String email,
        String dtype,
        LocalDateTime dataCriacao,
        LocalDateTime ultimoLogin,
        boolean ativo
) {

    public static AdministradorResponseDTO from(Administrador administrador) {
        return new AdministradorResponseDTO(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail(),
                "ADMINISTRADOR",
                administrador.getDataCriacao(),
                administrador.getUltimoLogin(),
                administrador.isAtivo()
        );
    }
}
