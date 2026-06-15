package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Consulta;
import com.smilehub.smilehub.entities.ConsultaServico;
import java.time.LocalDateTime;
import java.util.List;

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
        String status,
        List<ConsultaServicoResponseDTO> servicos
) {

    public static ConsultaResponseDTO from(Consulta consulta, List<ConsultaServico> servicos) {
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
                consulta.getStatus(),
                servicos.stream().map(ConsultaServicoResponseDTO::from).toList()
        );
    }
}
