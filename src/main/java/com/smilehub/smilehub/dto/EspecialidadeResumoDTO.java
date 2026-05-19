package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Especialidade;

public record EspecialidadeResumoDTO(Long id, String nome) {

    public static EspecialidadeResumoDTO from(Especialidade especialidade) {
        return new EspecialidadeResumoDTO(especialidade.getId(), especialidade.getNome());
    }
}
