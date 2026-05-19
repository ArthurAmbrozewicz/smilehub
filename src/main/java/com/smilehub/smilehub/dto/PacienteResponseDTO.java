package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Paciente;
import java.time.LocalDateTime;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        LocalDateTime dataCriacao,
        UsuarioResumoDTO usuario,
        String telefone
) {

    public static PacienteResponseDTO from(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getCpf(),
                paciente.getDataCriacao(),
                UsuarioResumoDTO.from(paciente.getUsuario()),
                paciente.getTelefone()
        );
    }
}
