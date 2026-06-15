package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Usuario;
import com.smilehub.smilehub.util.UsuarioDtypeUtil;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String dtype,
        LocalDateTime dataCriacao,
        LocalDateTime ultimoLogin,
        boolean ativo
) {

    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                UsuarioDtypeUtil.resolverDtype(usuario),
                usuario.getDataCriacao(),
                usuario.getUltimoLogin(),
                usuario.isAtivo()
        );
    }

}
