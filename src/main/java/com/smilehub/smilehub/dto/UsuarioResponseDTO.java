package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        PerfilResponseDTO perfil,
        LocalDateTime dataCriacao,
        LocalDateTime ultimoLogin
) {

    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                PerfilResponseDTO.from(usuario.getPerfil()),
                usuario.getDataCriacao(),
                usuario.getUltimoLogin()
        );
    }
}
