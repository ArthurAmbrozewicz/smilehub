package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Paciente;

public record PacienteResumoDTO(Long id, String nome) {

    public static PacienteResumoDTO from(Paciente paciente) {
        return new PacienteResumoDTO(paciente.getId(), paciente.getNome());
    }
}
