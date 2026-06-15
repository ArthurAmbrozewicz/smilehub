package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Paciente;
import java.time.LocalDateTime;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String dtype,
        String cpf,
        String telefone,
        LocalDateTime dataCriacao,
        LocalDateTime ultimoLogin,
        boolean ativo
) {

    public static PacienteResponseDTO from(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                "PACIENTE",
                paciente.getCpf(),
                paciente.getTelefone(),
                paciente.getDataCriacao(),
                paciente.getUltimoLogin(),
                paciente.isAtivo()
        );
    }
}
