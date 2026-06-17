package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Material;
import com.smilehub.smilehub.services.EstoqueService;

public record MaterialResponseDTO(
        Long id,
        String nome,
        int quantidade,
        int quantidadeInicial,
        boolean estoqueBaixo,
        boolean ativo,
        UsuarioResumoDTO usuario
) {

    public static MaterialResponseDTO from(Material material) {
        return new MaterialResponseDTO(
                material.getId(),
                material.getNome(),
                material.getQuantidade(),
                material.getQuantidadeInicial(),
                EstoqueService.estaComEstoqueBaixo(material),
                material.isAtivo(),
                UsuarioResumoDTO.from(material.getUsuario())
        );
    }
}
