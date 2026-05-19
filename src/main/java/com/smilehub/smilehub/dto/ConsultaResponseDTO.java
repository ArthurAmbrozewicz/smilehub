package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Consulta;
import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        PacienteResumoDTO paciente,
        DentistaResumoDTO dentista,
        UsuarioResumoDTO usuario,
        MotivoCancelamentoResumoDTO motivoCancelamento,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        LocalDateTime dataRegistro,
        String status
) {

    public static ConsultaResponseDTO from(Consulta consulta) {
        return new ConsultaResponseDTO(
                consulta.getId(),
                PacienteResumoDTO.from(consulta.getPaciente()),
                DentistaResumoDTO.from(consulta.getDentista()),
                UsuarioResumoDTO.from(consulta.getUsuario()),
                consulta.getMotivoCancelamento() != null
                        ? MotivoCancelamentoResumoDTO.from(consulta.getMotivoCancelamento())
                        : null,
                consulta.getDescricao(),
                consulta.getDataInicio(),
                consulta.getDataFim(),
                consulta.getDataRegistro(),
                consulta.getStatus()
        );
    }
}
