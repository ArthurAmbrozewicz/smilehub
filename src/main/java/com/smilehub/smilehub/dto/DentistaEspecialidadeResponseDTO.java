package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.DentistaEspecialidade;
import java.time.LocalDateTime;

public record DentistaEspecialidadeResponseDTO(
        Long id,
        DentistaResumoDTO dentista,
        EspecialidadeResumoDTO especialidade,
        LocalDateTime dataCadastro
) {

    public static DentistaEspecialidadeResponseDTO from(DentistaEspecialidade vinculo) {
        return new DentistaEspecialidadeResponseDTO(
                vinculo.getId(),
                DentistaResumoDTO.from(vinculo.getDentista()),
                EspecialidadeResumoDTO.from(vinculo.getEspecialidade()),
                vinculo.getDataCadastro()
        );
    }
}
