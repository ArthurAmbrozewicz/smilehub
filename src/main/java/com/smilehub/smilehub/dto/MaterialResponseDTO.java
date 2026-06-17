package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Material;

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
                calcularEstoqueBaixo(material),
                material.isAtivo(),
                UsuarioResumoDTO.from(material.getUsuario())
        );
    }

    private static boolean calcularEstoqueBaixo(Material material) {
        if (material.getQuantidadeInicial() <= 0) {
            return false;
        }

        double limite = material.getQuantidadeInicial() * 0.10;
        return material.getQuantidade() <= limite;
    }
}
