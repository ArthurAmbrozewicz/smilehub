package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Medicamento;
import com.smilehub.smilehub.entities.Prescricao;
import java.time.LocalDateTime;
import java.util.List;

public record PrescricaoResponseDTO(
        Long id,
        Long consultaId,
        PacienteResumoDTO paciente,
        DentistaResumoDTO dentista,
        String observacoes,
        LocalDateTime dataEmissao,
        List<MedicamentoResponseDTO> medicamentos
) {

    public static PrescricaoResponseDTO from(Prescricao prescricao, List<Medicamento> medicamentos) {
        return new PrescricaoResponseDTO(
                prescricao.getId(),
                prescricao.getConsulta().getId(),
                PacienteResumoDTO.from(prescricao.getConsulta().getPaciente()),
                DentistaResumoDTO.from(prescricao.getConsulta().getDentista()),
                prescricao.getObservacoes(),
                prescricao.getDataEmissao(),
                medicamentos.stream().map(MedicamentoResponseDTO::from).toList()
        );
    }
}
