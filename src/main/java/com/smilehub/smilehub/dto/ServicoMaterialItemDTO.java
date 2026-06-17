package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.ServicoMaterial;

public record ServicoMaterialItemDTO(
        Long materialId,
        String materialNome,
        Integer quantidade
) {

    public static ServicoMaterialItemDTO from(ServicoMaterial servicoMaterial) {
        return new ServicoMaterialItemDTO(
                servicoMaterial.getMaterial().getId(),
                servicoMaterial.getMaterial().getNome(),
                servicoMaterial.getQuantidade()
        );
    }
}
