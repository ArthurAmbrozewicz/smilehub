package com.smilehub.smilehub.dto;

import com.smilehub.smilehub.entities.Dente;
import com.smilehub.smilehub.entities.Odontograma;
import java.time.LocalDateTime;
import java.util.List;

public record OdontogramaResponseDTO(
        Long id,
        PacienteResumoDTO paciente,
        DentistaResumoDTO dentista,
        LocalDateTime dataUltimaAlteracao,
        String observacoes,
        List<DenteResponseDTO> dentes
) {

    public static OdontogramaResponseDTO from(Odontograma odontograma, List<Dente> dentes) {
        return new OdontogramaResponseDTO(
                odontograma.getId(),
                PacienteResumoDTO.from(odontograma.getPaciente()),
                DentistaResumoDTO.from(odontograma.getDentista()),
                odontograma.getDataUltimaAlteracao(),
                odontograma.getObservacoes(),
                dentes.stream().map(DenteResponseDTO::from).toList()
        );
    }
}
